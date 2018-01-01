package onlineorder.dao;

import onlineorder.model.Order;

import java.util.List;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public interface OrderDao {
    public List<Order> findByUserId(int userId);
}
