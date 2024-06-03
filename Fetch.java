import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

interface DataFetcher<T> {
    List<T> fetchTableData(String tableName, String column) throws SQLException;
}

class SQLDataFetcher<T> implements DataFetcher<T> {
    private final DatabaseConnection databaseConnection;
    private final RowMapper<T> rowMapper;

    public SQLDataFetcher(DatabaseConnection databaseConnection, RowMapper<T> rowMapper) {
        this.databaseConnection = databaseConnection;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> fetchTableData(String tableName, String column) throws SQLException {
        List<T> data = new ArrayList<>();
        try (Connection connection = databaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT " + column + " FROM " + tableName)) {
            while (resultSet.next()) {
                data.add(rowMapper.mapRow(resultSet));
            }
        }
        return data;
    }
}

interface RowMapper<T> {
    T mapRow(ResultSet resultSet) throws SQLException;
}
