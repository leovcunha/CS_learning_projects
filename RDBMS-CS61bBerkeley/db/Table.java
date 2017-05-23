package db;
import java.util.*;
import java.lang.IllegalArgumentException;

class Table {
  /**
     * Represents a table with columns and rows. Each column may have different types
     */
  //private final String tblName;
  private final String[] columnsName;
  private final String[] tblSchema; //types
  private List<Row> rows; // a list containing each row of the table 

  Table(String [] columnsInfo) {
    this.rows = new ArrayList<String>();
    this.tblName = name;
    this.columnsName = columnsInfo;
    this.tblSchema = new String[columnsInfo.length];
    for (int i = 0; i < columnsInfo.length; i++) {
      tblSchema[i] = columnsInfo[i].split(" ")[1];
    }     
  } 
  /**
   * Inner class represents each row of the table 
   */
  private class Row {

    private List<Object> columns;

    Row(Object[] a) {
      columns = new ArrayList<Object>();
      columns = Arrays.asList(a);
    }
    
    @Override
    public String toString() {
      return this.columns.toString().replace(" ", "")
                    .replace("[", "").replace("]", "");
    }
  }
  // Getters: ====================
  /**
   * @return table name
   */
  String getTblName() {
    return tblName;
  }
  /**
   * @return columns name
   */  
  String[] getColumnsName() {
    return columnsName;
  }
  /**
   * @return table schema
   */  
  String[] getTblSchema() {
    return tblSchema;
  }  
  /**
  * internal method that check types of each literal provided by a new row match the table schema
  * @param object array
  * @return true or false 
 **/  
  private boolean checkSchema(Object[] newRow) {
    //throw new RuntimeException("not implemented");
    boolean cS = true;
    String[] comp = this.getTblSchema();
        
    if (newRow.length != comp.length) return false;
    for (int i = 0; i < comp.length; i++) {
      if (comp[i].equals("int")) {
        cS = cS && (newRow[i] instanceof Integer);
    
      }
      else if (comp[i].equals("float")) {
        cS = cS && (newRow[i] instanceof Float);

      }
      else if (comp[i].equals("String")) {
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
    System.out.println(Arrays.toString(this.getColumnsName()).replace("[", "").replace("]", ""));
    for (Row r: this.rows) {
      System.out.println(r.toString());
    } 
  }
  
  public static void main(String[] args) { }  
}