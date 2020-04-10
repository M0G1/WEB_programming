package dataBase;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Date;

public class DataBase {
    private static final String CLASS_FOR_LOADING = "org.postgresql.Driver";
    private static final String DATA_BASE_LOCATION = "jdbc:postgresql://localhost:5432/pickup_points";
    private static final String[] USER_INFO = {"ssau_web", "ssau_web"};

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(CLASS_FOR_LOADING);
            connection = DriverManager.getConnection(DATA_BASE_LOCATION, USER_INFO[0], USER_INFO[1]);
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            System.err.println("Class for name: " + CLASS_FOR_LOADING + " not found");

        } catch (SQLException e) {
            System.err.println("Connection to" + DATA_BASE_LOCATION + " with owner " + USER_INFO[0] + " failed");
        }
        return connection;
    }

}
