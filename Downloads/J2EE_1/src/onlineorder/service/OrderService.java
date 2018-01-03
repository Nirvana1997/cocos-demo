package onlineorder.service;

import onlineorder.model.Order;

import java.util.List;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public interface OrderService {
    List<Order> getOrderListByUserId(int userId);
}
