/*
*
*
*

	Overall Design Strategy:
	
	1. Divide operations in separate class that interact with tables/data
	p.s. Some methods inerents to the tables and rows are kept within it (print and insertInto)
	2. Only database class is public , all other methods are package protected
	3. new and select- tables are created through factories accessed by the operations 
	4. select table factory calls joiner helper class to create natural inner join table passing itself for table creation
	
	
	
	
Database ---------- Operations -> methods: loadTable, storeTable, dropTable)
	|						|	
	|						|
	|						Interface> Table Factory
	Tables                NewTableFactory, SelectTableFactory   //creationals
	|	// methods of tables: 
	|	print, insert into 										|
	|															|--- helper class  : Joiner
	Rows () ---- List<Object> {a, 1, 2, 3.14}							|
																|--- methods: applyCondition
																--------------applyoperation	
			
			  			  
			 
			  


*/	



package db;

public class Database {
    /**
     * Represents a collection of named relations (tables)
     */
	private final String dbname;
	private Map<String, Table> tables; // table names are keys
	

    public Database() //constructor

    public String transact(String query) 
	/**
	 * requires well formed query (asserted by handler)
	 * effects  dispatch transaction to requestHandler 
	**/
}	

class Table {
	/**
    * Represents a table with columns and rows. Each column may have different types
    */
	private final String[] columnsName;
	private final String[] tblSchema; //types
	private List<Row> rows; // a list containing each row of the table 

    private Table(String [] columnsInfo) { //constructor
	
	static tableMaker(String [] columnsInfo) // static factory method 
	/** 
	 * requires package protected ,  arg string[] formed by 
	 * effects  dispatch transaction to requestHandler 
	**/	

  /**
  * Insert the given row (the list of literals) to the named table.
  * @param provided values must match the columns of that table
  * @return empty String on success
  * @throws typeerror , undefined
 **/ 
	public static String insertInto ()
	
	public static String getColumntype(String columnName)
	
}

class Row{
	 /**
     * representomg each row of the table obeying tblSchema of columns 
     */
//	private tblSchema;
	private Object[] columns
	
	public Object[] getRow() 
	public Object getColumn(int i) 
	

	 
	 
}


interface TableFactory {
	
	public Table createTable()
	
}

class NewTableFactory implements TableFactory

Receives table name and columns
receives rows to insert in the new table
return result table




class SelectTableFactory implements TableFactory

receive tables to be joined and call the joiner to create
apply conditions and operations
return result table





class Joiner
Objective: get two tables, compare columns name and decide if there are shared ones
if there are shared columns , look what rows are going to be merged (get row indexes)
if there aren't shared columns or rows  to be merged 


