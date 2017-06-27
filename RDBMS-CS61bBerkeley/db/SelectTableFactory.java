package db;

class SelectTableFactory implements TableFactory {

  public Table createTable(String[] columns) {
    return new Table(columns);
  }
  
  Table createSelTable(String exprs, String tables, String conds) {
    Table t = this.createTable(exprs.split(","));
    return t;
  }
  
    
    
  private void applyCondition(Table t) {
 
  }
  private void applyOperation(Table t) {}
  
  }