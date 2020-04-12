package dataBase;

import com.sun.istack.internal.NotNull;
import model.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.UUID;

public class OrderItemDML {

    private static final String TABLE_NAME = "\"OrderItem\"";
    private static final String ORDER_ID = "order_id";

    private static final String ITEM_TABLE_NAME = "\"Item\"";
    private static final String ITEM_ID = "item_id";
    private static final String NAME = "item_name";
    private static final String COUNT = "item_count";
    private static final String PRICE = "price";
    private static final String DATE_OF_RECEIPT = "\"dateOfReceipt\"";

    public static boolean insert(@NotNull Connection connection, @NotNull UUID orderId, @NotNull UUID itemId) {
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

    /**
     * удаляет все связи список-элемент из БД для заданного списка
     */
    public static boolean deleteOrder(@NotNull Connection connection, @NotNull UUID orderId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + ORDER_ID + "=uuid(" + orderId.toString() + "') ;";
            //autocommit
            stmt.execute(delete_request);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static boolean delete(@NotNull Connection connection, @NotNull UUID orderId, @NotNull UUID itemId) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + ORDER_ID + "=uuid(" + orderId.toString() + "') AND " +
                    ITEM_ID + "=uuid(" + itemId.toString() + "');";
            //autocommit
            stmt.execute(delete_request);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static long getCountOfLinksWithOrder(@NotNull Connection connection, @NotNull UUID itemUUID) {
        long res = 0L;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT count(*) FROM" + TABLE_NAME +
                    " WHERE " + ITEM_ID + "=uuid(" + itemUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            res = resSet.getLong("count");
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static UUID[] getItemUUID(@NotNull Connection connection, @NotNull UUID orderUUID) {
        UUID[] res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT " + ITEM_ID + " FROM" + TABLE_NAME +
                    " WHERE " + ORDER_ID + "=uuid(" + orderUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            res = new UUID[resSet.getFetchSize()];
            for (int i = 0; resSet.next(); ++i)
                res[i] = UUID.fromString(resSet.getString(ITEM_ID));
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static Item[] getItem(@NotNull Connection connection, @NotNull UUID orderUUID) {
        Item[] res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + ITEM_TABLE_NAME +
                    "WHERE " + ITEM_ID + " in " +
                    "SELECT " + ITEM_ID + " FROM" + TABLE_NAME +
                    " WHERE " + ORDER_ID + "=uuid(" + orderUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            res = new Item[resSet.getFetchSize()];
            for (int i = 0; resSet.next(); ++i)
                res[i] = new Item(
                        UUID.fromString(resSet.getString(ITEM_ID)),
                        resSet.getString(NAME),
                        resSet.getDouble(PRICE),
                        resSet.getInt(COUNT),
                        resSet.getDate(DATE_OF_RECEIPT)
                        );

            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
