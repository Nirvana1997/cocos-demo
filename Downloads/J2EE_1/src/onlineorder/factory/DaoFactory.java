package onlineorder.factory;

import onlineorder.dao.OrderDao;
import onlineorder.dao.impl.OrderDaoImpl;

/**
 * @author qianzhihao
 * @version 2017/12/31
 */
public class DaoFactory {
    public static OrderDao getOrderDao(){
        return OrderDaoImpl.getInstance();
    }
}
