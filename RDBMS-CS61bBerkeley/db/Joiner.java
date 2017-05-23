public class Joiner {
 /**
  * Helper class that combines two tables in a natural inner join
  * Two rows should be merged if and only if all of their shared columns have the same values
  * In the case that the input tables have no columns in common, the resulting table is what is called the Cartesian Product of the tables.
  * It is possible to join multiple tables. For example, to join tables A, B and C, you would join A with B, and then join the result of that with C. This rule can be generalized * to as many tables as desired, joining them from left to right.
  The column order of a join is defined as follows:

 All shared columns come first, in the relative order they were in the left table of the join (the one listed first in the select clause)
 The unshared columns from the left table come next, in the same relative order they were before.
 The unshared columns from the right table come last, in the same relative order they were before.
     */
  Table tableJoin;
  
  private Joiner (Table t1, Table t2) {
    String[] shColumns = getSharedColumns(t1, t2);
    if (shColumns.get(0) == "") tableJoin = cartesianProduct(t1, t2); 
    else {
      
    }
  }
  
  private String[] getSharedColumns(Table t1, Table t2) {
    String[] sc1 = t1.getColumnsName();
    String[] sc2 = t2.getColumnsName();
    List<String> shared = new Arraylist<String>();  
    for (s: sc1) {
      for (c: sc2) {
        if (s == c) shared.add(s);
      }
      return shared;
    }
  
  }
  
  
  private Table cartesianProduct(Table t1, Table t2)
  
  
  private Table 
  
}