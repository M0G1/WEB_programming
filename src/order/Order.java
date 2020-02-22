package order;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class Order implements Serializable {
    private String name;
    private int count;
    private double price;
    private Date dateOfReceipt;


    //==================================================check methods=========================================================
    private static void checkPrice(double price) throws IllegalArgumentException {
        if (price < 0)
            throw new IllegalArgumentException(String.format("Negative price: %f ", price));
    }

    private static void checkCount(int count) throws IllegalArgumentException {
        if (count < 1)
            throw new IllegalArgumentException(String.format("invalid count of products: %d ", count));
    }

    private static void checkDateOfReceipt(Date dateOfReceipt) throws IllegalArgumentException {
        if (dateOfReceipt.before(new Date()))
            throw new IllegalArgumentException(String.format("invalid date of receipt: %s ", dateOfReceipt.toString()));
    }

    private static void check(String name, double price, int count, Date dateOfReceipt) throws IllegalArgumentException {
        checkPrice(price);
        checkCount(count);
        checkDateOfReceipt(dateOfReceipt);
    }

//==================================================Constructor=========================================================

    private Order() {
    }

    ;

    public Order(@NotNull String name, double price, int count, @NotNull Date dateOfReceipt) throws IllegalArgumentException {
        check(name, price, count, dateOfReceipt);
        this.name = name;
        this.price = price;
        this.count = count;
        this.dateOfReceipt = dateOfReceipt;
    }

//==============================================Getters=and=Setters=====================================================

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws IllegalArgumentException {
        checkPrice(price);
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) throws IllegalArgumentException {
        checkCount(count);
        this.count = count;
    }

    public Date getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(Date dateOfReceipt) {
        checkDateOfReceipt(dateOfReceipt);
        this.dateOfReceipt = dateOfReceipt;
    }

//================================================Object=Method=========================================================

    @Override
    public int hashCode() {
        int hash = name.hashCode();
        long f = Double.doubleToLongBits(price);

        hash += (int) f + (int) (f >>> 32);
        hash += count;
        hash += dateOfReceipt.hashCode();

        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Order))
            return false;
        Order order = (Order) obj;
        if (order.count != this.count)
            return false;
        if (order.price != this.price)
            return false;
        if (!order.name.equals(this.name))
            return false;
        return order.dateOfReceipt.equals(dateOfReceipt);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        Formatter ans = new Formatter();
        ans.format("order name: %s", name);
        ans.format("count: %d", count);
        ans.format("price: %f", price);
        ans.format("date of receipt: %s", dateOfReceipt.toString());
        return ans.toString();
    }

//================================================Object=Method=========================================================

    public static void writeOrder(@NotNull Writer out, @NotNull Order order) {
        try {
            DateFormat df = DateFormat.getDateInstance();
            out.write(order.getName() + " , " +
                    order.getCount() + " , " +
                    order.getPrice() + " , " +
                    df.format(order.getDateOfReceipt()) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static Order readOrder(@NotNull Reader in) {
        Order ans = null;
        BufferedReader reader = null;
        try {
            DateFormat df = DateFormat.getDateInstance();
            reader = new BufferedReader(in);
            String[] data = reader.readLine().split(" , ");
            int count = Integer.parseInt(data[1]);
            double price = Double.parseDouble(data[2]);
            Date date = df.parse(data[3]);

            ans = new Order();
            ans.name = data[0];
            ans.count = count;
            ans.price = price;
            ans.dateOfReceipt = date;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return ans;
    }
}
