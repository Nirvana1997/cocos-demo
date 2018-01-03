package onlineorder.dao.impl;

import onlineorder.dao.DataHelper;
import onlineorder.dao.OrderDao;
import onlineorder.model.Order;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public class OrderDaoImpl implements OrderDao{
    private static OrderDao orderDao = new OrderDaoImpl();

    public static OrderDao getInstance(){
        return orderDao;
    }

    private OrderDaoImpl() {
    }

    @Override
    public List<Order> findByUserId(int userId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        List<Order> list = new ArrayList();
        connection = DataHelperImpl.getBaseDaoInstance().getConnection();

        try {
            stmt = connection.prepareStatement("select * from `order` where user_id = ?");
            stmt.setInt(1, userId);
            result = stmt.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setName(result.getString("name"));
                order.setPrice(result.getDouble("price"));
                order.setOrderTime(result.getDate("order_time"));
                order.setQuantity(result.getInt("quantity"));
                order.setOrder_id(result.getInt("order_id"));
                order.setUser_id(result.getInt("user_id"));
                order.setIn_stock(result.getBoolean("in_stock"));
                list.add(order);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
