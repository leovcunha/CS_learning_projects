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
  * @return new database instance
  * @throws
 **/
 public String transact(String query) {
        RequestHandler req = new RequestHandler(query);
        req.processQuery();
        return req.getReturnString();
    }
 
 /**
  * Get a Table instance from db
  * @param none
  * @return a table copy
  * @throws error message 
 **/ 
    Table getTable(String tbName) {
    
    
    }
  
}
 
