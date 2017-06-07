package db;

import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {

    String[] sch1 = {"a String", "b String", "c int"};
    Object[] a1 = {"a", "xxx", 3};
    Object[] b1 = {"b", "fff", 4};    
    Table table1 = new Table(sch1 );
    
    String[] sch2 = {"x String", "b String", "d int"};
    Object[] a2 = {"a", "xxx", 3};
    Object[] b2 = {"b", "fff", 4};
    Table table2 = new Table(sch2 );
    
    TableFactory tf = new SelectTableFactory();
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testJoiner() {
      Joiner jj = new Joiner(table1, table2, tf);
      assertEquals(jj.joined.getColumnsName().get(0), "b");
    }

}
