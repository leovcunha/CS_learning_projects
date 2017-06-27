package db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses the Builder Pattern 
 * Handles all column operations and conditions of select clauses delegated from RequestHandler and build resulting table.
 * @author leovcunha
 *
 */
class SelectTableBuilder {

 private String[] conditions;
 private String[] tableNames;
 private String[] columnOperations;
 
    // simplifies parsing.
    
    private final Pattern OP_PATTERN = Pattern.compile("(\\w+|\\*)(?:\\s*([\\+\\*\\/\\-]?)\\s*(\\w+)(?:\\s+as\\s+(\\w+))?)?"),
         COND_PATTERN = Pattern.compile("(\\w+)\\s*([\\>\\=\\<\\!]{1,2})\\s*(\\'*\\w+\\'*)");
 
 
 SelectTableBuilder(String[] cOperations, String[] tNames) {
  this.tableNames = tNames;
  this.columnOperations = cOperations;
 }
 
 /**
  * sets the conditions queried by the user
  * @param array of conditions string
  * @return SelectTableBuilder with condition set
  */
 SelectTableBuilder setCondition(String[] condit) {
  this.conditions = condit;
  return this;
  
 }
 
  /**
   * builder
   * @param db
   * @return resulting Table
   * @throws IllegalArgumentException
   */
 Table build(Database db) {
  for (int i = 0; i < tableNames.length; i++) {
	  if (!db.containsTable(tableNames[i])) throw new IllegalArgumentException("table not found");
  }
  Table t = this.colOperations(this.joinTablenames(db));

  if (this.conditions != null) return this.applyCondition(t);
   
  return t;
 }
 
 private Table joinTablenames(Database db) {

  Table t = db.getTable(tableNames[0]);
  
  if (this.tableNames.length == 1)
   return t;
  else {
   for (int i = 1; i < tableNames.length; i++) {
     Joiner jj = new Joiner(t, db.getTable(tableNames[i]));
     t = jj.getJoined();  
   }
   return t;
  }
 }
 /**
  * A column expression is an expression of the form <operand0> <arithmetic operator> <operand1> as <column alias>, 
  * or it may just be a single operand. There are a few special cases for column expressions, listed below.
  * If a lone * is supplied instead of a list of column expressions, all columns of the result of the join should be selected.
  * If only a single operand is given, it must be a column name. The new column shares its name with the original column.
  * If two operands are given, the left must be a column name while the right could be either a column name or a literal. 
  * An alias must always be provided when there are two operands.
  * If a column is created as the result of a column expression that was not just a column name, the name of the new column 
  * is given by the as keyword. You do not have to handle the case where a select statement creates duplicate columns.
  * @param t - a joined table
  * @return Table with operated columns
  */
 private Table colOperations(Table t) {
  
  if (columnOperations[0].matches("\\s*\\*\\s*")) //Select * from...
   return t;
  
  Table opTable = null;
  List<MappedColumns> lmappedColumns = new ArrayList<MappedColumns>();
  List<String> opHeaders = new ArrayList<String>();
  

  for (int i = 0; i < columnOperations.length; i++) {
   Matcher m = OP_PATTERN.matcher(columnOperations[i]);

   if (!m.matches()) 
         throw new RuntimeException(String.format("bad column operation: %s\n", columnOperations[i]));
   
   MappedColumns mc = new MappedColumns();
   mc.mapColumns(m.group(1), m.group(2), m.group(3), m.group(4), t);
   lmappedColumns.add(mc);
  }
  
  for (Row r: t) {
   List<Object> tempRow = new ArrayList<Object>();
   
   for (MappedColumns mCol: lmappedColumns) {
    if (opTable == null) {
       opHeaders.add(mCol.header);
     }
    if (mCol.operation == null )
     tempRow.add(r.getCell(mCol.columnA));
    else if (mCol.constantB){
    	if (mCol.cType.matches("[Ss]tring"))
    	tempRow.add(operateCells(r.getCell(mCol.columnA), mCol.colBstring, mCol.operation, mCol.cType));
    	else
    	tempRow.add(operateCells(r.getCell(mCol.columnA), mCol.columnB, mCol.operation, mCol.cType));	
    } else 
     tempRow.add(operateCells(r.getCell(mCol.columnA), r.getCell(mCol.columnB), mCol.operation, mCol.cType));
   }
   if (opTable == null) {
     String[] headArray = new String[opHeaders.size()];
     headArray = opHeaders.toArray(headArray);  
     opTable = Table.createTable(headArray);     
   }
   opTable.insertInto(tempRow.toArray());
  } 
  
  return opTable;
 }
 
 private class MappedColumns {
  int columnA;    
  int columnB;
  String colBstring;
  boolean constantB = false;
  String operation;
  String cType;
  String header;
  
   void setColumnA(int A) {
   this.columnA = A;
  }
  void setColumnB(int B) {
   this.columnB = B;
  } 
  void setColumnB(String B) {
	   this.colBstring = B;
	  } 
  void setOperation(String op) {
   this.operation = op;
  }
  void setConstantB(boolean B) {
   this.constantB = B;
  }
  void setcType(String cT) {
   this.cType = cT;
  }
  void mapColumns(String cA, String Op, String cB, String asName, Table t) {
   this.setColumnA(t.columnIndex(cA));
     // select single column  
   if (Op == null && cB == null && (asName == null || asName.equals(" "))) {
     this.header = t.getColumnsHeader().get(this.columnA);
   }
   else if (cB == null || asName == null)
     throw new RuntimeException("missing operand or alias");
   
   else {
     this.setOperation(Op);
     
     if (cB.matches("[0-9]+")) { //operation with constant
       this.setColumnB(Integer.parseInt(cB));
       this.setcType(t.getTblSchema().get(this.columnA));
       this.header = asName + " " + this.cType;
       this.setConstantB(true);
     }
     else if (cB.matches("\\'\\w+\\'")) {
    	 this.setColumnB(cB);
         this.setcType(t.getTblSchema().get(this.columnA));
         this.header = asName + " " + this.cType;
         this.setConstantB(true);
     }
     else {      
      this.setColumnB(t.columnIndex(cB));
       
       if (t.getTblSchema().get(this.columnA).matches("[fF]loat") || t.getTblSchema().get(this.columnB).matches("[fF]loat")) {
         this.header = asName + " float";
         this.setcType("float");
       } 
       else if (t.getTblSchema().get(this.columnA).equals("int") && t.getTblSchema().get(this.columnB).equals("int")) {
         this.header = asName + " int";
         this.setcType("int");
       } 
       else {
         this.header = asName + " string";
         this.setcType("string"); 
       }     
     } 
   }
  }
 }
 
 private Object operateCells(Object A, Object B, String op, String type) {
	 A = SpecialValues.ifSpecGetVal(A);
	 B = SpecialValues.ifSpecGetVal(B); 
  
    if ((type.matches("[Ss]tring")) && (op.equals("+")))  {
     if (A.equals(0.0)) A = "";
     if (B.equals(0.0)) B = "";
     String A1 = (String) A;
     String B1 = (String) B; 
     return ("'"+(String) A1.replace("'","") +(String) B1.replace("'","")+"'");     
    }
    else if (type.equals("float")) {
     Float A1 = (Float) A;
     Float B1 = (Float) B;
     if (op.equals("+")) return  (A1 + B1);
     else if (op.equals("-")) return (A1 - B1);
     else if (op.equals("*")) return (A1 * B1);
     else if (op.equals("/")) {
    	 if (B1 == 0) return SpecialValues.NAN;
    	 else return (A1 / B1);
     }
    } 
    else if (type.equals("int")) {
     Integer A1 = (Integer) A;
     Integer B1 = (Integer) B;
     if (op.equals("+")) return  (A1 + B1);
     else if (op.equals("-")) return (A1 - B1);
     else if (op.equals("*")) return (A1 * B1);
     else if (op.equals("/")) {
    	 if (B1 == 0) return SpecialValues.NAN;
    	 else return (A1 / B1);
     }
    }
    else throw new IllegalArgumentException("illegal operation");
    return "error";
    

   
 }
 /**
  *A condition statement is a comparison of rows in the given tables. There are two kinds of conditions: unary and binary. 
  * Unary conditions are of the form <column name> <comparison> <literal>, while binary conditions are of the form 
  * <column0 name> <comparison> <column1 name>. The difference is that unary conditions involve only one column, while
  * binary conditions involve two columns. You may assume that in a unary condition, the literal is always the right operand.
  * In order to be included in the resulting table of a select statement, a row must pass all conditional statements listed in
  * the select. For example, if we do select * from t1 where y > 5 and x > 4, we will return only rows that match both of these
  * conditions. 
  */
 private Table applyCondition(Table t) {
   Table condTable;
  
   List<MappedColumns> mappedConditions = new ArrayList<MappedColumns>();
     
   for (int i = 0; i < conditions.length; i++) {
     Matcher m = COND_PATTERN.matcher(conditions[i]);
     
     if (!m.matches()) 
         throw new RuntimeException(String.format("bad condition formed: %s\n", columnOperations[i])); 

     MappedColumns mc = new MappedColumns();
     mc.mapColumns(m.group(1), m.group(2), m.group(3), " ", t);
     mappedConditions.add(mc);
   }
   String[] headArray = new String[t.getColumnsHeader().size()];
   headArray = t.getColumnsHeader().toArray(headArray); 
   condTable = Table.createTable(headArray);
   
   for (Row r: t) {
     
     for (MappedColumns mCol: mappedConditions) {
    	 
    	 if (mCol.constantB && mCol.cType.matches("[Ss]tring")) {
    		 if (testCondition(r.getCell(mCol.columnA), mCol.colBstring, mCol.operation, mCol.cType))
    			 condTable.insertInto(r.getColumns().toArray());
    	 }	 
    	 else if (mCol.constantB) {	
    		 if (testCondition(r.getCell(mCol.columnA), mCol.columnB, mCol.operation, mCol.cType))
    			 condTable.insertInto(r.getColumns().toArray());
    	 }
    	 else {
    		 if (testCondition(r.getCell(mCol.columnA), r.getCell(mCol.columnB), mCol.operation, mCol.cType))
    			 condTable.insertInto(r.getColumns().toArray());
    	 }
     }
   }
  return condTable;
 }

 private boolean testCondition(Object A, Object B, String op, String type) {
	 A = SpecialValues.ifSpecGetVal(A);
	 B = SpecialValues.ifSpecGetVal(B);
     
	 if (type.matches("[Ss]tring")) {
		 if (A.equals(0.0)) A = "";
		 if (B.equals(0.0)) B = "";
         String A1 = (String) A;
         String B1 = (String) B;
         
         if (op.equals("==")) return  (A1.compareTo(B1) == 0);
         else if (op.equals("!=")) return (A1.compareTo(B1) != 0);
         else if (op.equals("<")) return (A1.compareTo(B1) < 0);
         else if (op.equals(">")) return (A1.compareTo(B1) > 0);
         else if (op.equals("<=")) return (A1.compareTo(B1) <= 0);
         else if (op.equals(">=")) return (A1.compareTo(B1) >= 0);
         else throw new IllegalArgumentException("illegal condition");         
     }
	 else if (type.equals("float")) {
         Float A1 = (Float) A;
         Float B1 = (Float) B;
         if (op.equals("==")) return  (A1 == B1);
         else if (op.equals("!=")) return (A1 != B1);
         else if (op.equals("<")) return (A1 < B1);
         else if (op.equals(">")) return (A1 > B1);
         else if (op.equals("<=")) return (A1 <= B1);
         else if (op.equals(">=")) return (A1>= B1);
         else throw new IllegalArgumentException("illegal condition");
         
     } 
	 else if (type.equals("int")) {
         Integer A1 = (Integer) A;
         Integer B1 = (Integer) B;
         if (op.equals("==")) return  (A1 == B1);
         else if (op.equals("!=")) return (A1 != B1);
         else if (op.equals("<")) return (A1 < B1);
         else if (op.equals(">")) return (A1 > B1);
         else if (op.equals("<=")) return (A1 <= B1);
         else if (op.equals(">=")) return (A1>= B1);
         else throw new IllegalArgumentException("illegal condition");
         
     } else throw new IllegalArgumentException("illegal condition");
           
 }
 
 
 public static void main(String args[]) {
   
   Database db = new Database();
   db.transact("create table teste1 (a int, b String, c String)");
   db.transact("insert into teste1 values 1, 'xixi', 'alfa'");
   db.transact("insert into teste1 values 1, 'xoxo', 'gama'");
   db.transact("create table teste2 (a int, f int, g String)");
   db.transact("insert into teste2 values 1, 37, 'delta'");
   db.transact("insert into teste2 values 1, 45, 'epsilon'");
   db.transact("insert into teste2 values 1, 32, 'phi'"); 
   String[] cOperations = {"b", "c + g as sumStr", "f + 5 as sumInt"};
   String[] tNames = {"teste1", "teste2"};
   String[] conds = {"b < 'xoxo'"};
   Table sT = new SelectTableBuilder(cOperations, tNames).setCondition(conds).build(db);
   
   
   System.out.println(sT.print());
   /*Table tS = sT.colOperations(tT);
   System.out.println(tS.print());
   Table tC = sT.applyCondition(tS);
   System.out.println(tC.print());
   */
      

  }
 
 
 
 }
