package order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Item> {
    transient static final private OrderComparator comparator = new OrderComparator();

    public static OrderComparator getInstance() {
        return comparator;
    }

    @Override
    public int compare(Item o1, Item o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
