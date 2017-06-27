package db;

import static org.junit.Assert.*;

import org.junit.Test;

public class SelectTableBuilderTest {

	Database dbcreate() {
		Database db = new Database();
		db.transact("create table teste1 (a int, b int, c String)");
		db.transact("insert into teste1 values 1, 34, 'alfa'");
		db.transact("insert into teste1 values 1, 5, 'gama'");
		db.transact("create table teste2 (a int, f int, g String)");
		db.transact("insert into teste2 values 1, 37, 'delta'");
		db.transact("insert into teste2 values 1, 45, 'epsilon'");
	    db.transact("insert into teste2 values 1, 32, 'phi'"); 
	    return db;
	}


	@Test
	public void testBuilder() {
	    Database db = dbcreate();
		String[] cOperations = {"c + g as sumStr", "f + 5 as sumInt"};
		String[] tNames = {"teste1", "teste2"};
		String[] conds = {"sumInt > 40"};
		Table sT = new SelectTableBuilder(cOperations, tNames).setCondition(conds).build(db);
		assertEquals(sT.getColumnsHeader().get(0), "sumStr string");
		assertEquals(sT.getColumnsHeader().get(1), "sumInt int");
		assertEquals(sT.getRowSize(), 4);
		assertEquals(sT.getRow(0).get(0), "'alfadelta'");
	}


}
