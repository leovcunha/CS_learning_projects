package db;
import java.util.*;

/**
 * Helper class that combines two tables in a natural inner join
 * Two rows should be merged if and only if all of their shared columns have the same values
 * In the case that the input tables have no columns in common, the resulting table is what is called the Cartesian Product of the tables.
 * It is possible to join multiple tables. For example, to join tables A, B and C, you would join A with B, and then join the result of that with C. This rule can be generalized * to as many tables as desired, joining them from left to right.
 * The column order of a join is defined as follows:
 * All shared columns come first, in the relative order they were in the left table of the join (the one listed first in the select clause)
 * The unshared columns from the left table come next, in the same relative order they were before.
 * The unshared columns from the right table come last, in the same relative order they were before. 
*/
class Joiner {

  SharedOthers columnsJoined;
  Table joined;
    
  Joiner (Table t1, Table t2, TableFactory factory) {
    this.columnsJoined = joinColumns(t1, t2);
    if (columnsJoined.shared.isEmpty()) joined = cartesianProduct(t1, t2); 
    else {
      this.joined = factory.createTable(columnsJoined.toArray());
      System.out.println(joined.getColumnsName());
      this.mergeRows(joined, t1, t2, columnsJoined);
    }
    
  }
  /**
   * Private class to hold shared columns in schemas of table 1 and 2
   * also hold other columns not shared to build joined table later
   * @author Leandro
   */
  private class SharedOthers {
    List<String> shared;
    List<String> others1;
    List<String> others2;
    
    SharedOthers(List<String> sh, List<String> o1, List<String> o2) {
      this.shared = sh;
      this.others1 = o1;
      this.others2 = o2;
    }
    /**
     * Array rep that will result in schema of the new joined table
     * @return array of columns of join table in proper order
     */
    String[] toArray() {
      List<String> arrayer = new ArrayList<String>();
      arrayer.addAll(shared);
      arrayer.addAll(others1);
      arrayer.addAll(others2);
      return arrayer.toArray(new String[arrayer.size()]);
    }
      
  }  
  /**
   * Merges the rows of table 1 and 2 , considering equal values in shared columns (natural inner join)
   * Resulting row is directly inserted into resul Table passed as parameter
   * @param tResult resulting table
   * @param t1
   * @param t2
   * @param shColumns
   */
  private void mergeRows(Table tResult, Table t1, Table t2, SharedOthers shColumns) {
    List<Object> rBuilder;
    // gets col index of shared cols from t1, t2
    List<Integer> indexes1 = t1.getCIndexes(shColumns.shared);
    List<Integer> indexes2 = t2.getCIndexes(shColumns.shared); 
    List<Integer> otherIndex1 = t2.getCIndexes(shColumns.others1); 
    List<Integer> otherIndex2 = t2.getCIndexes(shColumns.others2); 

    for (Row r1: t1) {
          
         for (Row r2: t2) {
          System.out.println(shColumns.shared);
          if (r1.getColumns(indexes1).containsAll(r2.getColumns(indexes2))) {
           
           rBuilder = new ArrayList<Object>();
           rBuilder.addAll(r1.getColumns(indexes1));
           rBuilder.addAll(r1.getColumns(otherIndex1));
           rBuilder.addAll(r2.getColumns(otherIndex2));
           tResult.insertInto(rBuilder.toArray());
          }          
         }   
    } 
    
   
  }
  
  
/**
  * Define columns to be joined 
  * @return object containing shared columns, non shared in table 1 and 2
  */ 
  private SharedOthers joinColumns(Table t1, Table t2) {
    List<String> sc1 = t1.getColumnsName();
    List<String> sc2 = t2.getColumnsName();
    List<String> shared = new ArrayList<String>();
    List<String> others1 = new ArrayList<String>();
    List<String> others2 = new ArrayList<String>();
        
    for (String s: sc1) {
      if (sc2.contains(s)) {
       shared.add(s);
      }
      else others1.add(s);
    }
    
    for (String s: sc2) {
      if (!shared.contains(s)) others2.add(s);
    }
    
    return new SharedOthers(shared, others1, others2);
      
  } 
  /**
  * Computes the cartesian product over two tables and updates the joined table
  * @param exactly two tables t1 and t2
  * @return list of columns in the correct order of join operation or empty list indicating no shared columns
  */   
  
  private Table cartesianProduct(Table t1, Table t2) {
    return t2;
    
  
  }
  
  public static void main(String args[]) {
    String[] sch1 = {"a String", "b String", "c int"};
    Object[] a1 = {"a", "xxx", 3};
    Object[] b1 = {"b", "fff", 4};    
    Table table1 = new Table(sch1 );
    table1.insertInto(a1);
    table1.insertInto(b1);
    
    String[] sch2 = {"x String", "b String", "d int"};
    Object[] a2 = {"a", "xxx", 3};
    Object[] b2 = {"b", "fff", 4};
    Table table2 = new Table(sch2 );
    table2.insertInto(a2);
    table2.insertInto(b2);
    
    TableFactory tf = new SelectTableFactory();
    Joiner jj = new Joiner(table1, table2, tf);
    jj.joined.print();
  }
}