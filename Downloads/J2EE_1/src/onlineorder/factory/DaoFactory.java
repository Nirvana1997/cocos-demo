package onlineorder.factory;

import onlineorder.dao.OrderDao;
import onlineorder.dao.UserDao;
import onlineorder.dao.impl.OrderDaoImpl;
import onlineorder.dao.impl.UserDaoImpl;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public class DaoFactory {
    public static OrderDao getOrderDao(){
        return OrderDaoImpl.getInstance();
    }

    public static UserDao getUserDao(){
        return UserDaoImpl.getInstance();
    }
}
