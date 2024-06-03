public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection dbConnection = new MySQLDatabaseConnection(
                "jdbc:mysql://localhost:3306/mydatabase", "user", "password"
            );

            // Example row mapper for a specific type
            RowMapper<MyObject> rowMapper = resultSet -> new MyObject(
                resultSet.getInt("id"),
                resultSet.getString("name")
            );

            DataFetcher<MyObject> dataFetcher = new SQLDataFetcher<>(dbConnection, rowMapper);
            ObjectHasher<MyObject> hasher = new DefaultObjectHasher<>();

            DataReconciliation reconciliation = new MerkleTreeDataReconciliation<>(dataFetcher, hasher);

            boolean result = reconciliation.reconcileTables("table1", "column1", "table2", "column2");
            System.out.println("Tables are " + (result ? "identical" : "different"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class MyObject {
    private int id;
    private String name;

    public MyObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ":" + name;
    }
}
