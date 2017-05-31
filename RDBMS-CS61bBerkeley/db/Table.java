package db;

import java.util.*;
import java.lang.IllegalArgumentException;

class Table {
  /**
   * Represents a table with columns and rows. Each column may have different types
   */
  //private final String tblName;
  private final List<String> columnsName;
  private final List<String> tblSchema; //types
  private List<Row> rows; // a list containing each row of the table 

  Table(String [] columnsInfo) {
    this.rows = new ArrayList<Row>();
    this.columnsName = new ArrayList<String>(Arrays.asList(columnsInfo));
    this.tblSchema = new ArrayList<String>();
    for (int i = 0; i < columnsInfo.length; i++) {
      tblSchema.add(columnsInfo[i].split(" ")[1]);
    }     
  } 
  /**
   * Inner class represents each row of the table 
   */
  class Row {

    private List<Object> columns;

    Row(Object[] a) {
      columns = new ArrayList<Object>(Arrays.asList(a));
    }
    
    Object getCell(int i) {
    	return columns.get(i);
    }
    
    @Override
    public String toString() {
      return this.columns.toString().replace(" ", "")
                    .replace("[", "").replace("]", "");
    }
  }
  // Getters: ====================
  /**
   * @return columns name
   */  
  List<String> getColumnsName() {
    return columnsName;
  }
  /**
   * @return table schema
   */  
  List<String> getTblSchema() {
    return tblSchema;
  }  
  /**
   * @return number of rows in Table
   */    
  int getRowSize() {
	return this.rows.size();
  }
  
  /**
   * @return list of objects in a specific row with index i in Table
   */ 
  List<Object> getRow(int i) {
	  return rows.get(i).columns;
  }
  
  List<Object> getColumn(String cName) {
	  int i = this.columnsName.indexOf(cName);
	  List<Object> xCol = new ArrayList<Object>();
	  for (Row r: rows) {
		  xCol.add(r.getCell(i));
	  }
	  return xCol;
  }
  /**
  * internal method that check types of each literal provided by a new row match the table schema
  * @param object array
  * @return true or false 
 **/  
  private boolean checkSchema(Object[] newRow) {
    //throw new RuntimeException("not implemented");
    boolean cS = true;
    List<String> comp = this.getTblSchema();
        
    if (newRow.length != comp.size()) return false;
    for (int i = 0; i < comp.size(); i++) {
      if (comp.get(i).equals("int")) {
        cS = cS && (newRow[i] instanceof Integer);
    
      }
      else if (comp.get(i).equals("float")) {
        cS = cS && (newRow[i] instanceof Float);

      }
      else if (comp.get(i).equals("String")) {
        cS = cS && (newRow[i] instanceof String);   

      }
      else return false;
      if (!cS) return cS; //check if false after every column
    }
    return cS;
  }
  
  /**
  * Insert the given row (the list of literals) to the named table.
  * @param provided values must match the columns of that table
  * @return empty String on success
  * @throws exception if type different from tblSchema
 **/ 
  String insertInto (Object[] a) {
    
   if (this.checkSchema(a)) {
      Row aIns= new Row(a);
      this.rows.add(aIns);
      return "";
   }
   else throw new IllegalArgumentException("ERROR: invalid value assigned to column");
  }
  
  /** Print should return the String representation of the table, or an appropriate error message otherwise.
    */
  void print() {
    System.out.println(this.getColumnsName().toString().replace("[", "").replace("]", ""));
    for (Row r: this.rows) {
      System.out.println(r.toString());
    } 
  }
  
  public static void main(String[] args) { }  
}