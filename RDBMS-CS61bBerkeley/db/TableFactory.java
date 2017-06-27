package db;

interface TableFactory {
  Table createTable(String[] columns);
 
}