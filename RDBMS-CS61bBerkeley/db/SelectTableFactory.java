package db;
class SelectTableFactory implements TableFactory {

  public Table createTable(String[] columns) {
	  
	return new Table(columns);
	}
  
  private void applyCondition(Table t) {
	  
  }
  private void applyOperation(Table t) {}
  
}