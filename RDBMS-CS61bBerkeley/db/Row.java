package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Row {

    private List<Object> columns;

    Row(Object[] a) {
      columns = new ArrayList<Object>(Arrays.asList(a));
    }
     /**
   * @return specific cell of a row 
   */ 
    Object getCell(int i) {
     return columns.get(i);
    }
     /**
   * @return list of objects in the row
   */ 
    List<Object> getColumns() {
     return columns;
    }
   /**
   * @param list of indexes of required columns of the row 
   * @return list of objects in a specific row with indexes provided
   */ 
    List<Object> getColumns(List<Integer> ind) {
     List<Object> cols = new ArrayList<Object>();
     for (int i=0; i < ind.size(); i++) {
      cols.add(columns.get(ind.get(i)));
     }
     return cols;
    }
    
    @Override
    public String toString() {
      return this.columns.toString().replace(" ", "")
                    .replace("[", "").replace("]", "");
    }
  }