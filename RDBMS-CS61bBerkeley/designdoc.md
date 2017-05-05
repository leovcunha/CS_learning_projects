/**
*Did you write prototype code to test your ideas?


Have you written any JUnit tests? Do you plan to do so?

What abstract data types (maps, sets, lists) do you primarily rely on?

Do you have nested generic declarations, e.g. Map<Integer, List<Integer>>? Instructor note: This is an indication you should make a new class, see lecture slide link.

Have you considered all operations from the spec? Which have you not yet considered?

What are some earlier design ideas that were rejected? WHY?

What helper methods have you thought about defining?

How do you handle having three different types, two of each are primitive (int and float) and one of which is a reference type (String)? Do you use generics? Something else?

Do you have any Maps whose keys are mutable? For example, Map<List, String>. Instructor note: This is not a good idea. Maps assume that keys are immutable.

Optional Part II
Look over the example design doc, and try to identify flaws with this design. Some questions you might consider:

Does the choice of storage lend itself to a simple join method? Why or why not?
Are abstraction barriers strong between the classes?
Is the use of generics a good one?
Is the use of subtype polymorphism appropriate?
What are two improvements?
*
*
*
*
*
*
*
*
Database ---------- Operations -> methods: readTable, storeTable, dropTable)
	|						|	
.	|						|
	|						Interface> Table Factory
	Tables                NewTableFactory, LoadTableFactory, SelectTableFactory   //creationals
	|	// methods of tables: print, insert into 				|
	|															|--- helper class  : Joiner
	Rows ---- List<column> [a, 1, 2, 3.14]						|
				|												|--- methods: applyCondition
				|												--------------applyoperation	
				|
			  Column<type>  {
				  type columndata
				  
			  }
			  

	Overall Strategy:
	
	Divide operations in separate class that interact with tables/data
*/	



package db;

public class Database {
    /**
     * Represents a collection of named relations (tables)
     */
	private final String dbname;
	private Map<String, Table> tables;
	
	
    public Database() {
        // YOUR CODE HERE
    }

    public String transact(String query) {Stri
        return "YOUR CODE HERE";
    }
}	

class Table {
	 /**
     * Represents a table with columns and rows. Each column may have different types
     */
	private final String tblName;
	private List<Row> rows; // a list containing each row of the table 

    public Table() {
        // YOUR CODE HERE
    }	
	
	
}

class Row{
	 /**
     * Represents each row of the table obeying tblSchema of columns 
     */
	private tblSchema;
	private List<type>;
	private int Index;
	

	 
	 
}

class Column{
	 /**
     * Represents schema of names and types of columns of the table 
     */

	 private String type;
	
    public Column() {
        // YOUR CODE HERE
    }	
	 
	 
}

public interface TableFactory {
	
	public Table createTable()
	
}

public class NewTableFactory implements TableFactory

public class SelectTableFactory implements TableFactory


RowBuilder {
	private List<column> data;	
	public RowBuilder(Column c, int size ) {
		for (i = 0; i<size; i++) {
			this.data.add()
		}
	
		
	}
}


