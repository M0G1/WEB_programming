package dataBase;

import com.sun.istack.internal.NotNull;
import model.Customer;
import model.Item;
import model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.UUID;

public class CustomerDML {
    private static final String C_TABLE_NAME = "\"Customer\"";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String NAME = "char_name";
    private static final String LOGIN = "char_login";
    private static final String PASSWORD = "char_password";

    public static boolean insertOrder(@NotNull Connection connection, @NotNull Customer customer) {
        boolean res = false;
        try {
            Statement stmt = connection.createStatement();
            String insert_request = "INSERT INTO " + C_TABLE_NAME + " VALUES(" +
                    "uuid('" + customer.getId().toString() + "'), '" +
                    customer.getName() + "','" +
                    customer.getLogin() + "','" +
                    customer.getPassword() + "');";
            res = stmt.execute(insert_request);
            for (Order order : customer) {
//                UUID item_id = ItemDML.getIdExistItem(connection, item);
//                if (item_id == null)
//                    ItemDML.insertItem(connection, item);
//                else
//                    item.setId(item_id);
                OrderDML.insert(connection, order);
                //добавить связи между ними
                CustomerOrdersDML.insert(connection, customer.getId(), order.getId());
            }
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
    public static boolean deleteOrder(@NotNull Connection connection, @NotNull Customer customer, boolean isDeleteItems) {
        boolean res = false;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String delete_request = "DELETE FROM " + C_TABLE_NAME +
                    "WHERE " + CUSTOMER_ID + "=uuid('" + customer.getId().toString() + "');";
            if (isDeleteItems)
                for (Order order : customer)
                    OrderDML.deleteOrder(connection, order, isDeleteItems);
            Statement stmt = connection.createStatement();
            res = stmt.execute(delete_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static int updateCustomer(@NotNull Connection connection, @NotNull Customer oldCustomer, @NotNull Customer newCustomer) {
        int res = -1;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String update_request = "UPDATE " + C_TABLE_NAME +
                    "SET " +
                    CUSTOMER_ID + "= uuid('" + newCustomer.getId().toString() + "')," +
                    NAME + "='" + newCustomer.getName() + "'," +
                    LOGIN + '=' + newCustomer.getLogin() +
                    PASSWORD + '=' + newCustomer.getPassword() + ',' +
                    "WHERE " + CUSTOMER_ID + "=uuid('" + oldCustomer.getId().toString() + "');";
            Statement stmt = connection.createStatement();
            res = stmt.executeUpdate(update_request);
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static Customer getCustomer(@NotNull Connection connection, @NotNull UUID customerUUID) {
        Customer res = null;
        try {
            DateFormat dateFormat = DateFormat.getDateInstance();
            Statement stmt = connection.createStatement();
            String select_request = "SELECT * FROM " + C_TABLE_NAME +
                    " WHERE " + CUSTOMER_ID + " =uuid('" + customerUUID.toString() + "');";
            ResultSet resSet = stmt.executeQuery(select_request);
            if (resSet.next()) {
                res = new Customer();
                Order[] orders = CustomerOrdersDML.getOrder(connection, customerUUID);
                res.addAll(Arrays.asList(orders));
            }
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }

    public static UUID getIdExistCustomer(Connection connection,String login,String password){
        UUID res = null;
        try {
            Statement stmt = connection.createStatement();
            String select_request = "SELECT " + CUSTOMER_ID + " FROM " + C_TABLE_NAME +
                    "WHERE " + LOGIN + "='" + login + "' AND "+
                    PASSWORD+"='" + password + "';";
            ResultSet resultSet = stmt.executeQuery(select_request);
            if (resultSet.next())
                res = UUID.fromString(resultSet.getString(CUSTOMER_ID));
            //autocommit
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return res;
    }
}
