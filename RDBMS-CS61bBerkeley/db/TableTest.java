package db;


import static org.junit.Assert.*;
import org.junit.Test;

public class TableTest {

    /*
     * Testing Strategy:
     * 
     * 
     */
    
    String[] sch = {"a String", "b String", "c int"};
    //String af = "'a', 'xxx', 3";
    Object[] b = {"b", "fff", 4};
    Object[] c = {"c", "ggg", 5};
    Object[] d = {"d", "hhh", 6};
    Object[] e = {"e", "iii", 7};
    Object[] f = {"f", "jjj", 8};
    
    Table table = Table.createTable(sch );



    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test    
    public void testInsertInto() {
      //Object[] a = af.split(",");
      //System.out.println(table.print());
      //assertEquals(table.insertInto(a), "");   
      assertEquals(table.insertInto(b), "");    
      assertEquals(table.insertInto(c), "");    
      assertEquals(table.insertInto(d), "");    
      assertEquals(table.insertInto(e), "");    
      assertEquals(table.insertInto(f), "");    
    }   





}


