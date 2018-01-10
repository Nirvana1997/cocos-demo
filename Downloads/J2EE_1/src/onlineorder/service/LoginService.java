package onlineorder.service;

import javax.ejb.Remote;

/**
 * @author qianzhihao
 * @version 2018/1/2
 */
@Remote
public interface LoginService {
    /**
     * 判断密码是否正确，正确则返回userId，错误则返回0,用户名不存在返回-1
     * @param user
     * @param password
     * @return
     */
    int getUserIdAndJudgePassword(String user,String password);
}
