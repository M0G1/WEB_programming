package order;

import java.util.*;
import java.util.function.UnaryOperator;

public class OrderList implements List<Order> {

    UUID id;
    String address;
    List<Order> orderList;
    int size;

    public OrderList(String address) {
        id = UUID.randomUUID();
        this.address = address;
        orderList = new ArrayList<>();
        size = 0;
    }

    public OrderList(UUID id, int size) {
        this.id = id;
        this.orderList = new ArrayList<>(size);
        this.size = size;
    }


//==============================================Getters=and=Setters=====================================================


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
