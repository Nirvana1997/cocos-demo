package onlineorder.action.business;

import onlineorder.model.Order;

import java.io.Serializable;
import java.util.List;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public class OrderBean implements Serializable{

    private static final long serialVersionUID = 9196299059263041126L;

    private List<Order> orderList;

    private List<Order> getOrderList(){
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setOrderList(Order order,int index){
        orderList.set(index,order);
    }
}
