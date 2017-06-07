package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Row {

    private List<Object> columns;

    Row(Object[] a) {
      columns = new ArrayList<Object>(Arrays.asList(a));
    }
    
    Object getCell(int i) {
    	return columns.get(i);
    }
    
    List<Object> getColumns() {
    	return columns;
    }
    
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