package dataBase;

import com.sun.istack.internal.NotNull;
import model.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.UUID;

public class ItemDML {
    private static final String TABLE_NAME = "\"Item\"";
    private static final String ITEM_ID = "item_id";
    private static final String NAME = "item_name";
    private static final String COUNT = "item_count";
    private static final String PRICE = "price";
    private static final String DATE_OF_RECEIPT = "\"dateOfReceipt\"";

    public static boolean insertItem(@NotNull Connection connection, @NotNull Item item) {
        boolean res = false;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String insert_request = "INSERT INTO " + TABLE_NAME + " VALUES(" +
                    "uuid('" + item.getId().toString() + "')," +
                    '\'' + item.getName() + "'," +
                    item.getCount() + "," +
                    item.getPrice() + "," +
                    "DATE '" + dateFormat.format(item.getDateOfReceipt()) + "');";
            Statement stmt = connection.createStatement();
            res = stmt.execute(insert_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    /**
     * return: первый id  с такими параметрами полей в записи таблицы как у item, если есть иначе null
     */
    public static UUID getIdExistItem(@NotNull Connection connection, @NotNull Item item) {
        UUID res = null;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String select_request = "SELECT " + ITEM_ID + " FROM " + TABLE_NAME +
                    "WHERE " + NAME + "='" + item.getName() +
                    "' AND " + COUNT + '=' + item.getCount() +
                    " AND " + PRICE + '=' + item.getPrice() +
                    " AND " + DATE_OF_RECEIPT + "= DATE '" + dateFormat.format(item.getDateOfReceipt()) + "';";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(select_request);
            if (resultSet.next())
                res = UUID.fromString(resultSet.getString(ITEM_ID));
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static boolean deleteItem(@NotNull Connection connection, @NotNull Item item) {
        boolean res = false;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            UUID res_id = getIdExistItem(connection, item);
            if (res_id == null)
                return true;
            String delete_request = "DELETE FROM " + TABLE_NAME +
                    "WHERE " + NAME + "='" + item.getName() +
                    "' AND " + COUNT + '=' + item.getCount() +
                    " AND " + PRICE + '=' + item.getPrice() +
                    "AND " + DATE_OF_RECEIPT + "= DATE '" + dateFormat.format(item.getDateOfReceipt()) + "';";
            Statement stmt = connection.createStatement();
            res = stmt.execute(delete_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    /**
     * элемент полностью заменяется на данный, если такой есть
     */
    public static int updateItem(@NotNull Connection connection, @NotNull Item oldItem, @NotNull Item newItem) {
        int res = -1;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            UUID res_id = getIdExistItem(connection, oldItem);
            if (res_id == null)
                return res;
            String update_request = "UPDATE " + TABLE_NAME +
                    "SET " +
                    ITEM_ID + "= uuid('" + newItem.getId().toString() + "')," +
                    NAME + "='" + newItem.getName() + "'," +
                    COUNT + '=' + newItem.getCount() +
                    PRICE + '=' + newItem.getPrice() + ',' +
                    DATE_OF_RECEIPT + "= DATE '" + dateFormat.format(newItem.getDateOfReceipt()) + ' ' +
                    "WHERE " + ITEM_ID + "=uuid('" + res_id.toString() + "');";
            Statement stmt = connection.createStatement();
            res = stmt.executeUpdate(update_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static Item getItem(@NotNull Connection connection, @NotNull UUID itemUUID) {
        Item res = null;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + ITEM_ID + " =uuid('" + itemUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            if (resSet.next()) {
                res = new Item();
                res.setId(UUID.fromString(resSet.getString(ITEM_ID)));
                res.setCount(resSet.getInt(COUNT));
                res.setName(resSet.getString(NAME));
                res.setPrice(resSet.getDouble(PRICE));
                res.setDateOfReceipt(resSet.getDate(DATE_OF_RECEIPT));
            }
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

}
