package db;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class RequestHandlerTest {
  
    Database db = new Database();  
    
    String sch1 = "'a String', 'b String', 'c int'";

    String[] sch2 = "'a String', 'b String', 'c int'";

    
    String[] sch3 = "'a String', 'b String', 'c int'";
    Table table3 = new Table(sch3 );
         
    
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
      Joiner jj = new Joiner(table1, table2, tf);
      assertEquals(jj.joined.getColumnsName().get(0), "b String");
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
      Joiner jj = new Joiner(table1, table3, tf);
      jj.joined.print();
      assertEquals(jj.joined.getRowSize(), 4);
    }
  
}
