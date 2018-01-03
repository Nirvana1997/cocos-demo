package onlineorder.factory;

import onlineorder.service.LoginService;
import onlineorder.service.OrderService;
import onlineorder.service.impl.LoginServiceImpl;
import onlineorder.service.impl.OrderServiceImpl;

/**
 * @author qianzhihao
 * @version 2018/1/3
 */
public class ServiceFactory {
    public static OrderService getOrderService(){
        return OrderServiceImpl.getInstance();
    }

    public static LoginService getLoginService(){
        return LoginServiceImpl.getInstance();
    }

}
