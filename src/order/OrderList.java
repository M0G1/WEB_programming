package order;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.UnaryOperator;

public class OrderList implements OrderListInterface {

    String address;
    List<Order> orderList;
    int size;

    public OrderList() {
        this.address = "";
        orderList = new ArrayList<>();
        size = 0;
    }

    public OrderList(String address) {
        this.address = address;
        orderList = new ArrayList<>();
        size = 0;
    }

//    public OrderList(String address, int size) {
//        this.address = address;
//        this.orderList = new ArrayList<>(size);
//        this.size = size;
//    }


//==============================================Getters=and=Setters=====================================================

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//================================================List=Interface========================================================

    @Override
    public void replaceAll(UnaryOperator<Order> operator) {
        return;
    }

    @Override
    public void sort(Comparator<? super Order> c) {
        orderList.sort(OrderComparator.getInstance());
    }

    @Override
    public Spliterator<Order> spliterator() {
        return orderList.spliterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return orderList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return orderList.contains(o);
    }

    @Override
    public Iterator<Order> iterator() {
        return orderList.iterator();
    }

    @Override
    public Object[] toArray() {
        return orderList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return orderList.toArray(a);
    }

    @Override
    public boolean add(Order order) {
        boolean res = orderList.add(order);
        size = orderList.size();
        return res;
    }

    @Override
    public boolean remove(Object o) {
        boolean res = orderList.remove(o);
        size = orderList.size();
        return res;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return orderList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Order> c) {
        return orderList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Order> c) {
        boolean res = orderList.addAll(index, c);
        size = orderList.size();
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = orderList.retainAll(c);
        size = orderList.size();
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return orderList.retainAll(c);
    }

    @Override
    public void clear() {
        orderList.clear();
    }

    @Override
    public Order get(int index) {
        return orderList.get(index);
    }

    @Override
    public Order set(int index, Order element) {
        return orderList.set(index, element);
    }

    @Override
    public void add(int index, Order element) {
        orderList.add(index, element);
        ++size;
    }

    @Override
    public Order remove(int index) {
        Order temp = orderList.remove(index);
        size = orderList.size();
        return temp;
    }

    @Override
    public int indexOf(Object o) {
        return orderList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return orderList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Order> listIterator() {
        return orderList.listIterator();
    }

    @Override
    public ListIterator<Order> listIterator(int index) {
        return orderList.listIterator(index);
    }

    @Override
    public List<Order> subList(int fromIndex, int toIndex) {
        return orderList.subList(fromIndex, toIndex);
    }

    //==========================================Task====================================================


    @Override
    public void sortAndSaveUnique() throws RemoteException {
        if (orderList.size() > 1) {
            this.sort(OrderComparator.getInstance());
            List<Order> ans = new ArrayList<>();
            Order prev = orderList.get(0);
            ans.add(prev);

            for (int i = 1; i < orderList.size(); ++i) {
                Order cur = orderList.get(i);
                if (!cur.equals(prev))
                    ans.add(cur);
                prev = cur;
            }

            this.orderList = ans;
            this.size = ans.size();
        }
    }

    //==========================================Static=Read=Write=Method====================================================
    public static void writeList(@NotNull Writer out, @NotNull OrderList orderList, char separator) throws IOException {
        out.write(orderList.getAddress() + "\n" +
                orderList.size() + "\n");
        if (orderList.size > 0) {
            for (Order order : orderList)
                Order.writeOrder(out, order, separator);
        }
        out.write("\n");
    }

    public static OrderList readOrders(@NotNull Reader in, char separator) throws IOException, ParseException {

        BufferedReader reader = new BufferedReader(in);
        DateFormat df = DateFormat.getDateInstance();

        String addr = reader.readLine();
        int size = Integer.parseInt(reader.readLine());

        OrderList answer = new OrderList(addr);
        String orders = reader.readLine();

        StringReader ordersReaders = new StringReader(orders);
        for (int i = 0; i < size; ++i) {
            Order order = Order.readOrder(ordersReaders, separator);
            answer.add(order);
        }
        return answer;
    }


}
