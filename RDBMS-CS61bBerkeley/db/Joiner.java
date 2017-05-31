package db;
import java.util.*;


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
  List<String> columnsJoined;
  List rowsMerged;
  Table joined;
    
  Joiner (Table t1, Table t2, TableFactory factory) {
	columnsJoined = new ArrayList<String>();
    columnsJoined = joinColumns(t1, t2);
    if (columnsJoined.isEmpty()) joined = cartesianProduct(t1, t2); 
    else {
      joined = factory.createTable((String[]) columnsJoined.toArray());      
    }
    
  }
  
  private List<Integer> mergeRows(Table t1, Table t2, List<String> jColumns) {
	List<Integer> shRows = new ArrayList<Integer>();  

		
	}
	  
  }
  
  
/**
  * Define columns to be joined 
  * @return list of columns in pairs (t1, t2) for join operation or empty list indicating no shared columns
  */ 
  private List<String> joinColumns(Table t1, Table t2) {
    List<String> sc1 = t1.getColumnsName();
    List<String> sc2 = t2.getColumnsName();
    List<String> shared = new ArrayList<String>();
    List<String> others = new ArrayList<String>();
        
    for (String s: sc1) {
      if (sc2.contains(s)) {
    	  shared.add(s);
      }
      else others.add(s);
    }
    
    for (String s: sc2) {
      if (!shared.contains(s)) others.add(sc2.indexOf(s));
    }
    
    //return shared;
    
    if (shared.isEmpty()) return shared;
    else {
      shared.addAll(others);
      return shared;
    }
      
  } 
  /**
  * Computes the cartesian product over two tables and updates the joined table
  * @param exactly two tables t1 and t2
  * @return list of columns in the correct order of join operation or empty list indicating no shared columns
  */   
  
  private Table cartesianProduct(Table t1, Table t2) {
    
  
  }
}