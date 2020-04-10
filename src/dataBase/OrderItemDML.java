package dataBase;

import model.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.UUID;

public class OrderItemDML {

    private static final String TABLE_NAME = "\"OrderItem\"";
    private static final String ORDER_ID = "order_id";
    private static final String ITEM_ID = "item_id";

    public static boolean insert(Connection connection, UUID orderId, UUID itemId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String insert_request = "INSERT INTO " + TABLE_NAME + " VALUES(" +
                    "uuid('" + itemId.toString() + "'), '," +
                    "uuid('" + orderId.toString() + "'), ');";

            res = stmt.execute(insert_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static boolean delete(Connection connection, UUID orderId, UUID itemId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
