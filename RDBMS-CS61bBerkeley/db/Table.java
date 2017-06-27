package db;

import java.util.*;
import java.lang.IllegalArgumentException;

class Table implements Iterable<Row>{
  /**
   * Represents a table with columns and rows. Each column may have a different type (float, int or string)
   */
  //private final String tblName;
  private final List<String> columnsHeader;
  private final List<String> tblSchema; //types
  private List<Row> rows; // a list containing each row of the table 
  
  private Table(String [] columnsInfo) {
    this.rows = new ArrayList<Row>();
    this.columnsHeader = new ArrayList<String>(Arrays.asList(columnsInfo));
    this.tblSchema = new ArrayList<String>();
    for (int i = 0; i < columnsInfo.length; i++) {
      tblSchema.add(columnsInfo[i].split(" ")[1]);
    }     
  } 
  /**
   * Static Factory method
   * @param columns
   * @return Table created
   */
  static Table createTable(String[] columns) {
     return new Table(columns);
   }

  // Getters: ====================
  /**
   * @return columns name
   */  
  List<String> getColumnsHeader() {
    return columnsHeader;
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
   return rows.get(i).getColumns();
  }
  
   /**
   * @return list of objects in a specific column  with name cName [name type] format
   */ 
  List<Object> getColumn(String cName) {
   int i = this.columnsHeader.indexOf(cName);
   List<Object> xCol = new ArrayList<Object>();
   for (Row r: rows) {
    xCol.add(r.getCell(i));
   }
   return xCol;
  }
  
   /**
   * @return indexes of specified columns [name type] format
   */  
  List<Integer> getCIndexes(List<String> colTitles) {
   List<Integer> indexes = new ArrayList<Integer>();
   for (String s: colTitles) {
 for(int i = 0; i< this.getColumnsHeader().size(); i++) {
  if (this.getColumnsHeader().get(i).contains(s))
   indexes.add(i);
 }
   }
   return indexes;
  }
  
  int columnIndex(String colTitle) {

   for(int i = 0; i< this.getColumnsHeader().size(); i++) {
  if (this.getColumnsHeader().get(i).matches(colTitle + "\\s+\\w+"))
   return i;

   }
   return -1;
  }
  
  /**
  * internal method that check types of each literal provided by a new row match the table schema
  * @param object array
  * @return true or false 
 **/  
  private boolean checkSchema(Object[] newRow) {
    boolean cS = true;
    List<String> comp = this.getTblSchema();
        
    if (newRow.length != comp.size()) return false;
    for (int i = 0; i < comp.size(); i++) {
      if (newRow[i] == SpecialValues.NAN || newRow[i] == SpecialValues.NOVALUE) {
        	continue;  
      }	
      else if (comp.get(i).equals("int")) {
        cS = cS && (newRow[i] instanceof Integer);    
      }
      else if (comp.get(i).equals("float")) {
        cS = cS && ((newRow[i] instanceof Float) || (newRow[i] instanceof Double));

      }
      else if (comp.get(i).matches("[Ss]tring")) {
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
   else throw new IllegalArgumentException("row provided doesn't match table columns");
  }
  
  /** Print should return the String representation of the table, or an appropriate error message otherwise.
    */
  String print() {
    StringBuilder S = new StringBuilder(this.getColumnsHeader().toString().replace("[", "").replace("]", ""));
    S.append("\n");
    for (Row r: this.rows) {
      S.append(r.toString());
      S.append("\n");
    } 
    return S.toString();
  }

  @Override
  public Iterator<Row> iterator() {
    return rows.iterator();
  }  
}