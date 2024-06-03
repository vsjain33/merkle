import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

interface DatabaseConnection {
    Connection connect() throws SQLException;
}

class MySQLDatabaseConnection implements DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;

    public MySQLDatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
