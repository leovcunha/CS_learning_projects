package db;
import java.util.*;

public class Database {
    /**
     * Represents a collection of named relations (tables)
     */
 private Map<String, Table> tables;
 
 
 public Database() {
        // YOUR CODE HERE
   this.tables = new HashMap<String, Table>();
 }
 
 /**
  * dispatch transaction to requestHandler 
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
 
