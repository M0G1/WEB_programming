package tesAndPointfLaunch;

import model.Item;
import model.Order;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;


public class Lab1Test {
    public static void main(String[] args) {
        //System.out.println("Is list instance of Serializable" + new ArrayList() instanceof Serializable);

        testOrderList();
    }


    public static void testOrderList() {
        try {
            Order order = new Order("adr"),
                    newOrder;
//            for (int i = 0; i < 2; ++i)
//                orderList.add(createOrder());
//
//            for (int i = 0; i < 2; ++i)
//                orderList.add((Order) orderList.get(i).clone());
//
//
            File file = new File("ord.txt");
//
//            FileWriter writer = new FileWriter(file);
//            OrderList.writeList(writer, orderList, ';');
//            writer.close();

            FileReader reader = new FileReader(file);
            newOrder = Order.readOrders(reader, ';');
            reader.close();

            System.out.println("orderList " + Arrays.toString(newOrder.toArray()));
            newOrder = order.sortAndSaveUnique(newOrder);
            System.out.println("newOrderList " + Arrays.toString(newOrder.toArray()));

            Item item = newOrder.get(0);
            Item item2 = (Item) item.clone();

            System.out.println(item);
            System.out.println(item2);
            System.out.println("equals " + item.equals(item2));

        } catch (IOException | CloneNotSupportedException | ParseException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static void testOrder() {
        try {
            Item r = createOrder();
            File file = new File("ord.txt");

            FileWriter writer = new FileWriter(file);

            Item.writeOrder(writer, r, ';');
            Item.writeOrder(writer, r, ';');
            writer.close();

            FileReader reader = new FileReader(file);
            Item r1 = Item.readOrder(reader, ';'),
                    r2 = Item.readOrder(reader, ';');

            reader.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static Item createOrder() {
        String[] dataStr = {"1 th", "2 th", "3 th", "4 th"};
        Random rnd = new Random();
        Date data = new Date(System.currentTimeMillis()
                + 365L * 24L * 60L * 60L * 1000L);
        return new Item(
                dataStr[rnd.nextInt(dataStr.length)],
                rnd.nextInt(100) + 1,
                rnd.nextInt(10) + 1,
                data
        );
    }
}
