package onlineorder.model;

import java.util.Date;

/**
 * @author qianzhihao
 * @version 2017/12/14
 */
public class Order {
    private int order_id;

    private int user_id;

    private Date orderTime;

    private int quantity;

    private double price;

    private String name;

    private boolean in_stock;

    public Order() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIn_stock() {
        return in_stock;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    @Override
    public String toString() {
        return "<tr>" +
                "<td>" + order_id + "</td>" +
                "<td>" + user_id + "</td>" +
                "<td>" + orderTime + "</td>" +
                "<td>" + quantity + "</td>" +
                "<td>" + price + "</td>" +
                "<td>" + name + "</td>" +
                "<td>" + (in_stock==true?'Y':'N') + "</td>"
                +"</tr>";
    }
}
