package production.threads;

import production.utility.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectThread extends DatabaseThread implements Runnable {
    private Connection connection;
    @Override
    public void run() {
        try {
            connection = DatabaseUtils.connectToDatabase();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Connection getConnection() {
        return connection;
    }
}
