package db;
public class NewTableFactory implements TableFactory{
  
  public Table createTable(String[] columns) {
    return new Table(columns);
  }

}