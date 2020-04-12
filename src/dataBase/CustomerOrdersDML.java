package dataBase;

import com.sun.istack.internal.NotNull;
import model.Item;
import model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;

public class CustomerOrdersDML {

    private static final String TABLE_NAME = "\"CustomerOrders\"";

    private static final String C_TABLE_NAME = "\"Customer\"";
    private static final String CUSTOMER_ID = "customer_id";

    private static final String O_TABLE_NAME = "\"Order\"";
    private static final String ORDER_ID = "order_id";
    private static final String ADDRESS = "address";

    public static boolean insert(@NotNull Connection connection, @NotNull UUID customerId, @NotNull UUID orderId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String insert_request = "INSERT INTO " + TABLE_NAME + " VALUES(" +
                    "uuid('" + customerId.toString() + "'), '," +
                    "uuid('" + orderId.toString() + "'), ');";

            res = stmt.execute(insert_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    /**
     * удаляет все связи список-элемент из БД для заданного списка
     */
    public static boolean deleteCustomer(@NotNull Connection connection, @NotNull UUID customerId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + ORDER_ID + "=uuid(" + customerId.toString() + "') ;";
            //autocommit
            stmt.execute(delete_request);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static boolean delete(@NotNull Connection connection, @NotNull UUID customerId, @NotNull UUID orderId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + ORDER_ID + "=uuid(" + orderId.toString() + "') AND " +
                    CUSTOMER_ID + "=uuid(" + customerId.toString() + "');";
            //autocommit
            stmt.execute(delete_request);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static Order[] getOrder(@NotNull Connection connection, @NotNull UUID customerId) {
        Order[] res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + O_TABLE_NAME +
                    "WHERE " + ORDER_ID + " in " +
                    "SELECT " + ORDER_ID + " FROM" + TABLE_NAME +
                    " WHERE " + ORDER_ID + "=uuid(" + customerId.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            res = new Order[resSet.getFetchSize()];
            for (int i = 0; resSet.next(); ++i) {
                res[i] = new Order();
                res[i].setAddress(resSet.getString(ADDRESS));
                res[i].setId(UUID.fromString(resSet.getString(ORDER_ID)));

                Item[] items = OrderItemDML.getItem(connection, res[i].getId());
                res[i].addAll(Arrays.asList(items));
            }

            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
