package tesAndPointfLaunch;

import order.OrderList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.Arrays;

public class ClientLab1 {
    //source https://javarush.ru/groups/posts/2283-rmi-praktika-ispoljhzovanija

    public static final String UNIQUE_BINDING_NAME = "server.OrderListInterface";

    //сначала откуда, а потом куда
    public static void main(String[] args) {

        FileWriter writer;
        FileReader reader;
        try {

            reader = new FileReader(new File(args[0]));
            writer = new FileWriter(new File(args[1]));

            try {
                // получаем доступ к регистру удаленных объектов.
                final Registry registry = LocateRegistry.createRegistry(666);
                /*
                    Получаем из регистра нужный объект
                    Работа RMI основана на использовании прокси,
                    поэтому удаленный вызов доступен только для методов интерфейсов,
                    а не классов.*/
                OrderList serverOrderList = (OrderList) registry.lookup(UNIQUE_BINDING_NAME);

                OrderList readedOrderList = OrderList.readOrders(reader, ';');
                //передаем на сервер
                serverOrderList.addAll(readedOrderList);
                writer.write("ServerOrderList before: " + Arrays.toString(serverOrderList.toArray()) + "\n");
                //удаленный вызов метода на сервере
                serverOrderList.sortAndSaveUnique();
                //записываем в файл тут)
                OrderList.writeList(writer, serverOrderList, ';');

            } catch (RemoteException e) {
                writer.write("Exception: " + e.getMessage());
            } catch (NotBoundException e) {
                writer.write("Object under name " + UNIQUE_BINDING_NAME + " is not binded\n");
                writer.write(e.getMessage());
            } catch (ParseException e) {
                writer.write("Can not read the OrderList from " + args[0]);
                writer.write(e.getMessage());
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
