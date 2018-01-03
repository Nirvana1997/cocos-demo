package onlineorder.service.impl;

import onlineorder.dao.impl.UserDaoImpl;
import onlineorder.factory.DaoFactory;
import onlineorder.service.LoginService;

/**
 * @author qianzhihao
 * @version 2018/1/2
 */
public class LoginServiceImpl implements LoginService{
    private static LoginService loginService = new LoginServiceImpl();

    private LoginServiceImpl(){

    }

    public static LoginService getInstance(){
        return loginService;
    }

    @Override
    public int getUserIdAndJudgePassword(String user, String password) {
        return DaoFactory.getUserDao().getUserIdAndJudgePassword(user,password);
    }
}
