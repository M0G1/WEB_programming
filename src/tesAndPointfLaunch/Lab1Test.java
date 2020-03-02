package tesAndPointfLaunch;

import order.Order;
import order.OrderList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;


public class Lab1Test {
    public static void main(String[] args) {
        testOrderList();
    }

    public static void testOrderList() {
        try {
            OrderList orderList = new OrderList("adr"),
                    newOrderList;
            for (int i = 0; i < 3; ++i)
                orderList.add(createOrder());

            File file = new File("ord.txt");

            FileWriter writer = new FileWriter(file);
            OrderList.writeList(writer, orderList, ';');
            writer.close();

            FileReader reader = new FileReader(file);
            newOrderList = OrderList.readOrders(reader, ';');
            reader.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static void testOrder() {
        try {
            Order r = createOrder();
            File file = new File("ord.txt");

            FileWriter writer = new FileWriter(file);

            Order.writeOrder(writer, r, ';');
            Order.writeOrder(writer, r, ';');
            writer.close();

            FileReader reader = new FileReader(file);
            Order r1 = Order.readOrder(reader, ';'),
                    r2 = Order.readOrder(reader, ';');

            reader.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static Order createOrder() {
        String[] dataStr = {"1 th", "2 th", "3 th", "4 th"};
        Random rnd = new Random();
        Date data = new Date(System.currentTimeMillis()
                + 365L * 24L * 60L * 60L * 1000L);
        return new Order(
                dataStr[rnd.nextInt(dataStr.length)],
                rnd.nextInt(100) + 1,
                rnd.nextInt(10) + 1,
                data
        );
    }
}
