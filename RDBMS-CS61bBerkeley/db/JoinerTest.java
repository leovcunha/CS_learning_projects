package db;

import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {

    String[] sch1 = {"a String", "b String", "c int"};
    Table table1 = Table.createTable(sch1 );
    
    String[] sch2 = {"x String", "b String", "d int"};
    Table table2 = Table.createTable(sch2 );
    
    String[] sch3 = {"x String", "y String", "z int"};
    Table table3 = Table.createTable(sch3 );
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testJoiner() {
      Object[] a1 = {"a", "xxx", 3};
      Object[] b1 = {"b", "fff", 4};  
      Object[] a2 = {"a", "xxx", 3};
      Object[] b2 = {"b", "ggg", 4};
      table1.insertInto(a1);
      table1.insertInto(b1);
      table2.insertInto(a2);
      table2.insertInto(b2);
      Joiner jj = new Joiner(table1, table2);
      assertEquals(jj.getJoined().getColumnsHeader().get(0), "b String");
    }
    
    @Test
    public void testCartesian() {
      Object[] a1 = {"a", "xxx", 3};
      Object[] b1 = {"b", "fff", 4};  
      Object[] a2 = {"a", "xxx", 3};
      Object[] b2 = {"b", "ggg", 4};
      table1.insertInto(a1);
      table1.insertInto(b1);
      table3.insertInto(a2);
      table3.insertInto(b2);
      Joiner jj = new Joiner(table1, table3);
      jj.getJoined().print();
      assertEquals(jj.getJoined().getRowSize(), 4);
    }


}
