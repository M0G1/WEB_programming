package model;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;

public class Order implements OrderListInterface, List<Item> {
    UUID id;
    String address;
    List<Item> itemList;

    public Order() {
        this.address = "";
        itemList = new ArrayList<>();
    }

    public Order(String address) {
        this.address = address;
        this.id = UUID.randomUUID();
        itemList = new ArrayList<>();
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
//================================================List=Interface========================================================

    @Override
    public void sort(Comparator<? super Item> c) {
        itemList.sort(ItemComparator.getInstance());
    }


    @Override
    public int size() {
        return itemList.size();
    }


    @Override
    public Iterator<Item> iterator() {
        return itemList.iterator();
    }

    @Override
    public Object[] toArray() {
        return itemList.toArray();
    }


    @Override
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return itemList.contains(o);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return itemList.toArray(a);
    }

    @Override
    public boolean add(Item item) {
        return itemList.add(item);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return itemList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Item> c) {
        return itemList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Item> c) {
        return itemList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return itemList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return itemList.retainAll(c);
    }

    @Override
    public void clear() {
        itemList.clear();
    }

    @Override
    public int indexOf(Object o) {
        return itemList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return itemList.lastIndexOf(o);
    }

    @Override
    public List<Item> subList(int fromIndex, int toIndex) {
        return itemList.subList(fromIndex, toIndex);
    }

    @Override
    public boolean remove(Object o) {
        boolean res = itemList.remove(o);
        return res;
    }


    @Override
    public Item get(int index) {
        return itemList.get(index);
    }

    @Override
    public Item set(int index, Item element) {
        return itemList.set(index, element);
    }

    @Override
    public void add(int index, Item element) {
        itemList.add(index, element);
    }

    @Override
    public Item remove(int index) {
        Item temp = itemList.remove(index);
        return temp;
    }

    @Override
    public ListIterator<Item> listIterator() {
        return itemList.listIterator();
    }

    @Override
    public ListIterator<Item> listIterator(int index) {
        return itemList.listIterator(index);
    }


    //==========================================Task====================================================

    //==========================================Static=Read=Write=Method================================================
    public static void writeList(@NotNull Writer out, @NotNull Order order, char separator) throws IOException {
        out.write(order.id.toString() + "\n" + order.getAddress() + "\n" +
                order.size() + "\n");
        if (order.size() > 0) {
            for (Item item : order)
                Item.writeItem(out, item, separator);
        }
        out.write("\n");
    }

    public static Order readOrder(@NotNull BufferedReader in, char separator) throws IOException, ParseException {

        String id = in.readLine();
        String addr = in.readLine();
        int size = Integer.parseInt(in.readLine());

        Order answer = new Order(addr);
        answer.id = UUID.fromString(id);
        String orders = in.readLine();

        StringReader ordersReaders = new StringReader(orders);
        for (int i = 0; i < size; ++i) {
            Item item = Item.readItem(ordersReaders, separator);
            answer.add(item);
        }
        return answer;
    }

    @Override
    public Order sortAndSaveUnique(Order order) throws RemoteException {
        List<Item> surfaceCopy = (List<Item>) ((ArrayList<Item>) order.itemList).clone();
        surfaceCopy.sort(ItemComparator.getInstance());
        List<Item> sortedAndUniq = new ArrayList<>();
        Item prev = surfaceCopy.get(0);
        sortedAndUniq.add(prev);

        for (int i = 1; i < surfaceCopy.size(); ++i) {
            Item cur = surfaceCopy.get(i);
            if (!cur.equals(prev))
                sortedAndUniq.add(cur);
            prev = cur;
        }
        Order ans = new Order(order.getAddress());
        ans.itemList = sortedAndUniq;
        return ans;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int deep) {
        StringBuilder orderStr = new StringBuilder();
        for (Item item : itemList) {
            orderStr.append('\n');
            for (int i = 0; i < deep + 1; ++i)
                orderStr.append('\t');
            orderStr.append(item.toString());
        }
        return "Order{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", itemList=" + orderStr.toString() +
                "}\n";
    }


}
