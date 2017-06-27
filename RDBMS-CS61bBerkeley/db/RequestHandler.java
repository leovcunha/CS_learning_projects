package db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.RuntimeException;


 /**
  * TAkes string query and convert into information on which methods must be operated on DB
  */
class RequestHandler {
      
    private RequestHandler() {

    }

    // Various common constructs, simplifies parsing.
    static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
                                 LOAD_CMD   = Pattern.compile("load " + REST),
                                 STORE_CMD  = Pattern.compile("store " + REST),
                                 DROP_CMD   = Pattern.compile("drop table " + REST),
                                 INSERT_CMD = Pattern.compile("insert into " + REST),
                                 PRINT_CMD  = Pattern.compile("print " + REST),
                                 SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
                                               "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
                                 SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                                               "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                                               "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                                               "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
                                 CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                                                   REST),
                                 INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                                               "\\s*(?:,\\s*.+?\\s*)*)");

    
    static String eval(String query, Database db) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
             return createTable(m.group(1), db);
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
             return loadTable(m.group(1), db);
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
             return storeTable(m.group(1), db);
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
             return db.dropTable(m.group(1)); //see design notes
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
             return insertRow(m.group(1), db); 
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
             return db.getTable(m.group(1)).print(); //see design notes
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
             return select(m.group(1), db).print(); //print method of the table resulting of the select
        } else {
            return String.format("Malformed query: %s\n", query);
        }
    }

    private static String createTable(String expr, Database db) {
      Matcher m;
      String tName;
      String[] tColumns;
      Table t;
      
      if ((m = CREATE_NEW.matcher(expr)).matches()) {
            tName = m.group(1);
            tColumns = m.group(2).split(COMMA);
            
            if (tColumns.length < 1) throw new RuntimeException("no columns provided");
            
            t = Table.createTable(tColumns);
            db.putTable(tName, t);
            return "";
            
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
         
         
            db.putTable(m.group(1), select(m.group(2), db)); //create table in DB  with select clauses
            return "";
        } else {
            throw new RuntimeException(String.format("Malformed create: %s\n", expr));
        }
      
    }

  /**
  * read a table from file .tbl giving it the name <table name> 
  * @return empty string on success If a table with the same name already exists, it should be replaced
  * @throws error message 
 **/  
    private static String loadTable(String name, Database db) {
      try {
        String line;
        BufferedReader in  = new BufferedReader(new FileReader(name + ".tbl"));
        Table t = Table.createTable(in.readLine().split(COMMA));
        
        while((line = in.readLine()) != null) {
          t.insertInto(lineSplit(line));         
        }
        in.close();
        db.putTable(name, t);
        return "";
      }
      catch (IOException e) { return e.getMessage(); }
    }
 

     /**
      * Write the contents of a database table to the file <table name>.tbl. If the TBL file already exists, 
      * it should be overwritten.
      * @return empty String on success 
      **/  
    private static String  storeTable(String name, Database db) {

      try {
        FileWriter out = new FileWriter(name + ".tbl");
        out.write(db.getTable(name).print());
        out.close();
        return "";
      }
      catch (IOException e) { return e.getMessage(); }      
    }

    
    /**
     * gets expr convert to object array andcalls Table insert into
     * @param expr
     * @param db
     * @return empty string on success
     */
    private static String insertRow(String expr, Database db) {
      
        Matcher m = INSERT_CLS.matcher(expr);
        String tName;
        Object[] rColumns;
        
        if (!m.matches())
            throw new RuntimeException(String.format("Malformed insert: %s\n", expr));
        tName = m.group(1);
        rColumns = lineSplit(m.group(2));
        db.getTable(tName).insertInto(rColumns);
        return "";
    }

    /**
     * parses expression to SelectTableBuilder to make the table resulting from the queries
     * @param expr
     * @param db
     * @return resulting table
     */
    private static Table select(String expr, Database db) {
      Table t;
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) 
            throw new RuntimeException(String.format("Malformed select: %s\n", expr));
            //return "Error";
        
      if (m.group(3) == null) {
       t = new SelectTableBuilder(m.group(1).split(COMMA), m.group(2).split(COMMA)).build(db);
      }
      else {
       t = new SelectTableBuilder(m.group(1).split(COMMA), m.group(2).split(COMMA))
                  .setCondition(m.group(3).split(AND)).build(db);
      }    
        return t;
    }
    


    
    /**
     * Helper method that takes a string describing a row info from a file tbl or command line 
     * and parses its object array with each column of the proper type
     * @return Object array that will later feed a Row
     */
    private static Object[] lineSplit(String line) {
        String [] linesplit = line.split(COMMA);
        Object[] oRow = new Object[linesplit.length];
        
        for (int i = 0; i < oRow.length; i++) {
        //   System.out.println(linesplit[i]);
          if (linesplit[i].contains("'")) {
            oRow[i] = linesplit[i];
           // System.out.println(oRow[i] instanceof String);
          }
          else if (linesplit[i].contains(".")) {
            oRow[i] = Double.parseDouble(linesplit[i]);
           // System.out.println(oRow[i] instanceof Double);
          }
          else if (linesplit[i].contains("NOVALUE")) {
        	oRow[i] = SpecialValues.NOVALUE;
          }
          else {
           oRow[i] = Integer.parseInt(linesplit[i]);
       //    System.out.println(oRow[i] instanceof Integer);
          }
          
          
        }
        return oRow;
    }
    public static void main(String args[]) {
    	
    	//tests
    	
        Database db = new Database();
        RequestHandler.loadTable("records", db);
        System.out.println(db.getTable("records").print());
        RequestHandler.createTable("teste1 (a int, b int, c String)", db);
        RequestHandler.insertRow("teste1 values 1, 34, 'alfa'", db);
        
        System.out.println(select("* from records where Season >= 2015" , db).print());
        System.out.print(db.getTable("teste1").print());
        RequestHandler.storeTable("teste1", db);
        RequestHandler.createTable("teste2 as select * from records, teste1 where Season >= 2015" , db);
        RequestHandler.storeTable("teste2", db);
        System.out.println(db.getTable("teste2").print());
        RequestHandler.eval("load t2", db);
        System.out.println(RequestHandler.eval("print t2", db));
        RequestHandler.eval("load t1", db);
        System.out.println(RequestHandler.eval("print t1", db));
        System.out.println(RequestHandler.eval("select * from t1, t2", db));
        RequestHandler.eval("load fans", db);
        System.out.println(db.getTable("fans").print());
        System.out.println(select("Firstname, Lastname, TeamName from fans where Lastname >= 'Lee'" , db).print());
        
        
      

    }
}    


