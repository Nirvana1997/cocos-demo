package onlineorder.dao;

/**
 * @author qianzhihao
 * @version 2018/1/1
 */
public interface UserDao {
    /**
     * 判断密码是否正确，正确则返回userId，错误则返回0,用户名不存在返回-1
     * @param user
     * @param password
     * @return
     */
    int getUserIdAndJudgePassword(String user,String password);
}
