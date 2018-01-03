package onlineorder.dao.impl;

import onlineorder.dao.DataHelper;
import onlineorder.dao.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author qianzhihao
 * @version 2018/1/1
 */
public class UserDaoImpl implements UserDao{
    private static UserDao userDao = new UserDaoImpl();

    public static UserDao getInstance(){
        return userDao;
    }

    public UserDaoImpl() {
    }

    @Override
    public int getUserIdAndJudgePassword(String user, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        connection = DataHelperImpl.getBaseDaoInstance().getConnection();

        try {
            stmt = connection.prepareStatement("SELECT * FROM user WHERE user_name=?");
            stmt.setString(1,user);
            result = stmt.executeQuery();

            if(result.next()){
                String pwd = result.getString("password");
                if(pwd.equals(password)){
                    return result.getInt("user_id");
                }else {
                    return 0;
                }
            }else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
