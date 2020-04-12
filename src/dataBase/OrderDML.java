package dataBase;

import com.sun.istack.internal.NotNull;
import model.Item;
import model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderDML {
    private static final String O_TABLE_NAME = "\"Order\"";
    private static final String ORDER_ID = "order_id";
    private static final String ADDRESS = "address";

    public static boolean insert(@NotNull Connection connection, @NotNull Order order) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String insert_request = "INSERT INTO " + O_TABLE_NAME + " VALUES(" +
                    "uuid('" + order.getId().toString() + "'), '" +
                    order.getAddress()
                    + "');";
            res = stmt.execute(insert_request);
            for (Item item : order) {
//                UUID item_id = ItemDML.getIdExistItem(connection, item);
//                if (item_id == null)
//                    ItemDML.insertItem(connection, item);
//                else
//                    item.setId(item_id);
                ItemDML.insertItem(connection, item);
                OrderItemDML.insert(connection, order.getId(), item.getId());
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
            String select_request = "SELECT " + ORDER_ID + " FROM " + O_TABLE_NAME +
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

    /**
     * удаляет список с заказами и элементы заказа из БД
     */
    public static boolean deleteOrder(@NotNull Connection connection, @NotNull Order order, boolean isDeleteItems) {
        boolean res = false;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            Statement stmt = connection.createStatement();
            UUID res_id = getIdExistOrder(connection, order);
            if (res_id == null)
                return true;
            String delete_request = "DELETE FROM " + O_TABLE_NAME +
                    "WHERE " + ADDRESS + "='" + order.getAddress() + "';";
            if (isDeleteItems)
                for (Item item : order)
                    ItemDML.deleteItem(connection, item);
            OrderItemDML.deleteOrder(connection, order.getId());
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
            String update_request = "UPDATE " + O_TABLE_NAME +
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

    public static Order getOrder(@NotNull Connection connection, @NotNull UUID orderUUID) {
        Order res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + O_TABLE_NAME +
                    " WHERE " + ORDER_ID + " =uuid('" + orderUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            if (resSet.next()) {
                res = new Order();
                res.setAddress(resSet.getString(ADDRESS));
                Item[] items = OrderItemDML.getItem(connection, orderUUID);
                res.addAll(Arrays.asList(items));
            }
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static List<UUID> getOrder(@NotNull Connection connection, @NotNull String address) {
        List<UUID> res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + O_TABLE_NAME +
                    " WHERE " + ADDRESS + "like '%" + address + "%';";
            ResultSet resSet = stmt.executeQuery(select_request);

            res = new ArrayList<>();
            for (int i = 0; resSet.next(); ++i)
                res.add(UUID.fromString(resSet.getString(ORDER_ID)));
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
