package order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    transient static final private OrderComparator comparator = new OrderComparator();

    public static OrderComparator getInstance() {
        return comparator;
    }

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
