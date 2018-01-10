package onlineorder.service;

import onlineorder.model.Order;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
@Remote
public interface OrderService {
    List<Order> getOrderListByUserId(int userId);
}
