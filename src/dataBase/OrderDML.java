package dataBase;

import com.sun.istack.internal.NotNull;
import model.Item;
import model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.UUID;

public class OrderDML {
    private static final String TABLE_NAME = "\"Order\"";
    private static final String ORDER_ID = "order_id";
    private static final String ADDRESS = "address";

    public static boolean insertOrder(@NotNull Connection connection, @NotNull Order order) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String insert_request = "INSERT INTO " + TABLE_NAME + " VALUES(" +
                    "uuid('" + order.getId().toString() + "'), '" +
                    order.getAddress()
                    + "');";
            res = stmt.execute(insert_request);
            for (Item item : order) {
                UUID item_id = ItemDML.getIdExistItem(connection, item);
                if (item_id == null)
                    ItemDML.insertItem(connection, item);
                else
                    item.setId(item_id);
                //добавить связи между ними
            }
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    /**
     * return: первый id  с такими параметрами полей в записи таблицы как у order, если есть иначе null
     */
    public static UUID getIdExistOrder(@NotNull Connection connection, @NotNull Order order) {
        UUID res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT " + ORDER_ID + " FROM " + TABLE_NAME +
                    "WHERE " + ADDRESS + "='" + order.getAddress() + "';";
            ResultSet resultSet = stmt.executeQuery(select_request);
            if (resultSet.next())
                res = UUID.fromString(resultSet.getString(ORDER_ID));
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static boolean deleteOrder(@NotNull Connection connection, @NotNull Order order) {
        boolean res = false;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            Statement stmt = connection.createStatement();
            UUID res_id = getIdExistOrder(connection, order);
            if (res_id == null)
                return true;
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + ADDRESS + "='" + order.getAddress() + "';";
            for (Item item : order) {

            }
            res = stmt.execute(delete_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }


    public static int updateAddressOrder(@NotNull Connection connection, @NotNull Order order, @NotNull String address) {
        int res = -1;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            Statement stmt = connection.createStatement();
            UUID res_id = getIdExistOrder(connection, order);
            if (res_id == null)
                return res;
            String update_request = "UPDATE " + TABLE_NAME +
                    "SET " + ADDRESS + "= '" + address + "' " +
                    "WHERE " + ORDER_ID + "=uuid('" + order.getId().toString() + "')," + "';";
            res = stmt.executeUpdate(update_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
