package onlineorder.service.impl;

import onlineorder.factory.DaoFactory;
import onlineorder.model.Order;
import onlineorder.service.OrderService;

import java.util.List;

/**
 * @author qianzhihao
 * @version 2018/1/3
 */
public class OrderServiceImpl implements OrderService{
    private OrderServiceImpl(){

    }

    private static OrderService orderService = new OrderServiceImpl();

    public static OrderService getInstance(){
        return orderService;
    }

    @Override
    public List<Order> getOrderListByUserId(int userId) {
        return DaoFactory.getOrderDao().findByUserId(userId);
    }
}
