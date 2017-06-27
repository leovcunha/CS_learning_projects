#A Simple Relational Database Management System 

Project done for learning in 6 weeks according to specification in : 
http://datastructur.es/sp17/materials/proj/proj2/proj2.html

Overall Design Strategy:

1. Divide query handling and operations in separate class called RequestHandler that interpret the query and do what was required 
through its methods 
p.s. Some methods inherents to the db, tables and rows are kept within it (print and insertInto)
2. Only database class is public , all other methods are package protected
3. new tables are created by factory method inside table class 
4. select- tables are created through select Table builder (pattern)
5. select builder calls joiner helper class to create natural inner join table passing itself for table creation
	
	
![Alt text](/RDBMS-CS61bBerkeley/Untitled%20Diagram.jpg)	
```	
Database ------ RequestHandler -> methods: loadTable, storeTable, dropTable)
|(putTable, dropTable)  |	
|			|
|		 SelectTableBuilder   //creationals--->  applyCondition, applyoperation
Tables    
|
|	// methods of tables:	helper Class: Joiner
|	print, insert into 	
|		
Rows () ---- List<Object> {a, 1, 2, 3.14}							
```																
														
###Most important Classes descriptions
```java
public class Database {
    //  Represents a collection of named relations (tables)    
	private Map<String, Table> tables; // table names are keys
	
	/**
	 * requires well formed query (asserted by handler)
	 * effects  dispatch transaction to requestHandler 
	**/
    public String transact(String query) 
}	

class Table {
	/**
    * Represents a table with columns and rows. Each column may have different types
    */
  private final List<String> columnsHeader;
  private final List<String> tblSchema; //types
  private List<Row> rows; // a list containing each row of the table 

	/** 
	 * requires package protected ,  arg string[] formed by 
	 * effects  dispatch transaction to requestHandler 
	**/	
	static createTable(String [] columnsInfo) // static factory method 

  /**
  * Insert the given row (the list of literals) to the named table.
  * @param provided values must match the columns of that table
  * @return empty String on success
  * @throws typeerror , undefined
 **/ 
   String insertInto ()
}	


class Row{
	 /**
     * represent each row of the table obeying tblSchema of columns 
     */
private List<Object> columns;

}

class RequestHandler
  /* TAkes string query and convert into information on which methods must be operated
   on DB*/


class SelectTableBuilder 
/* receive tables to be joined and call the joiner to create
apply optional conditions and operations
Builds result table
*/


class Joiner
/* Objective: get two tables, compare columns name and decide if there are shared ones
if there are shared columns , look what rows are going to be merged (get row indexes)
if there aren't shared columns or rows  to be merged */

enum SpecialValues {
	NOVALUE(0.0, "NOVALUE"), 
	NAN(Double.POSITIVE_INFINITY, "NaN");
 /* Objective: Handle special Values NOVALUE and NaN */
