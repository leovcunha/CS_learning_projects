package db;

import java.util.*;
    /**
     * Represents a collection of named relations (tables)
     */
public class Database {

 private Map<String, Table> tables;

 public Database() {
        // YOUR CODE HERE
   this.tables = new HashMap<String, Table>();
 }
 
 /**
  * receives transaction request from user and dispatch to requestHandler 
  * @param a string, to be treated as a query command
  * @return request result or error
 **/
 public String transact(String query) {
   
   try {
     return RequestHandler.eval(query, this);
     
   }
   catch (RuntimeException e) {
     return new String("ERROR: " + e.getMessage() );
   }
        
        
 }
 
 /**
  * Get a Table instance from db
 **/ 
    Table getTable(String tbName) {
      if (!this.containsTable(tbName)) throw new RuntimeException("table doesn't exist");
      return tables.get(tbName);
    }
    
    boolean containsTable(String tbName) {
      return tables.containsKey(tbName);
    }
    
  /**
  * save a Table instance in db
 **/    
    void putTable(String tbName, Table t) {
      tables.put(tbName, t);
    } 
    
  /**
  * remove a Table instance from db
 **/        
    String dropTable(String tbName) {
      tables.remove(tbName);
      return "";
    } 
  
}
 
