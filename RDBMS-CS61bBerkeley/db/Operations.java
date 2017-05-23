public class Operations {
    /**
     * TAkes string query and convert into object with information on which methods must be operated on DB
     */
	private final String dbname;
	private Map<String, Table> tables;
	
	
	/**
	 * read a table from file .tbl giving it the name <table name> 
	 * @return empty string on success If a table with the same name already exists, it should be replaced
	 * @throws error message 
	**/		
	public static String loadTable(String fileName, String tbName)
	
	/**
	* Write the contents of a database table to the file <table name>.tbl. If the TBL file already exists, it should be overwritten.
	* @return empty String on success 
	*  @throws appropriate error message otherwise.
	**/		
	public static String storeTable(String tbName)
	
	/**
	* print <table name>
	*  Print should return the String representation of the table 
	*  @throws appropriate error message otherwise.
	**/	
	public static String printTable(String tbName)
		
		
		/**
	* drop <table name> Delete the table from the database.
	*  @return the empty String on success
	*  @throws appropriate error message otherwise.
	**/	
	public static String printTable(String tbName)
}	
}	