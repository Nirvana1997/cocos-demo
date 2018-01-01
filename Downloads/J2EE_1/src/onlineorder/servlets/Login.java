package onlineorder.servlets;

import com.sun.deploy.net.HttpRequest;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DataSource datasource = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();

        InitialContext jndiContext = null;

        Properties properties = new Properties();
        properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
        properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        try {
            jndiContext = new InitialContext(properties);
            datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/onlineorder");
            System.out.println("got context");
            System.out.println("About to get ds---ShowMyStock");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login="";

		Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
  
        Integer ival = new Integer(1);
        		
        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("LoginCookie")) {
                    login=cookie.getValue();
                    break;
                }
            }
        }
    
        // Logout action removes session, but the cookie remains
        if (null != request.getParameter("Logout")) {
            System.out.println("logout");
            HttpSession session = request.getSession(false);
            if (null != session) {
                System.out.println("sess not null");
                session.invalidate();
                session = null;
            }
        }
       
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");

        out.println(
                "<form method='POST' action='"
                    + response.encodeURL(request.getContextPath()+"/Login")
                    + "'>");
        out.println(
            "login: <input type='text' name='login' value='" + login + "'>");
        out.println(
            "password: <input type='password' name='password' value=''>");
        out.println("<input type='submit' name='Submit' value='Submit'>");
   
        out.println("<p>Servlet is version @version@</p>");
//    out.println("</p>You are visitor number " + webCounter);
        int all = getServletContext().getAttribute("all")==null?0:(int)getServletContext().getAttribute("all");
        int user = getServletContext().getAttribute("user")==null?0:(int)getServletContext().getAttribute("user");
        out.println("游客人数："+(all-user));
        out.println("登录用户："+user);
        out.println("总人数："+all);
       
    
    out.println("</form></body></html>");
     
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        Statement sm = null;

        try {
            connection = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String login = (String)request.getParameter("login");
        String password = (String)request.getParameter("password");

        try {
            System.out.println(login);
            stmt = connection.prepareStatement("select * from user where user_name = '"+ login +"'");
            result = stmt.executeQuery();
            if(result.next()){
                if(password.equals(result.getString("password"))){
                    System.out.println("密码正确。");

                    //统计登录用户人数+1
                    ServletContext context = getServletContext();
                    int user = context.getAttribute("user")==null?0:(int)context.getAttribute("user");
                    context.setAttribute("user",user+1);

                    setCookieAndSession(request,response,String.valueOf(result.getInt("user_id")),login);
                    response.sendRedirect(request.getContextPath() + "/ShowMyOrderServlet");
                }else {
                    System.out.println("密码错误!");
                    out.print("<body onLoad=\"checkForm()\"><script language=\"JavaScript\" type=\"text/JavaScript\">function checkForm(){ \n" +
                            "                    alert(\"密码错误!\");return true;}</script></body>");
                }

            }else {
                System.out.println("用户名不存在!");
                out.print("<body onLoad=\"checkForm()\"><script language=\"JavaScript\" type=\"text/JavaScript\">function checkForm(){ \n" +
                        "                    alert(\"用户名不存在!\");return true;}</script></body>");

            }
            out.println("<html><body>");

            out.println(
                    "<form method='POST' action='"
                            + response.encodeURL(request.getContextPath()+"/Login")
                            + "'>");
            out.println(
                    "login: <input type='text' name='login' value=''>");
            out.println(
                    "password: <input type='password' name='password' value=''>");
            out.println("<input type='submit' name='Submit' value='Submit'>");

            out.println("<p>Servlet is version @version@</p>");
//    out.println("</p>You are visitor number " + webCounter);
            int all = getServletContext().getAttribute("all")==null?0:(int)getServletContext().getAttribute("all");
            int user = getServletContext().getAttribute("user")==null?0:(int)getServletContext().getAttribute("user");
            out.println("游客人数："+(all-user));
            out.println("登录用户："+user);
            out.println("总人数："+all);


            out.println("</form></body></html>");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


	}



	private void setCookieAndSession(HttpServletRequest req,HttpServletResponse resp,String  user_id,String username)throws IOException{
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        boolean cookieFound = false;
        Cookie cookie = null;
        Cookie[] cookies = req.getCookies();
        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("LoginCookie")) {
                    cookieFound = true;
                    break;
                }
            }
        }

        if (session == null) {
            boolean isLoginAction = (null == user_id) ? false : true;

            System.out.println(user_id + " session null");
            if (isLoginAction) { // User is logging in
                if (cookieFound) { // If the cookie exists update the value only
                    // if changed
                    if (!username.equals(cookie.getValue())) {
                        cookie.setValue(username);
                        resp.addCookie(cookie);
                    }
                } else {
                    // If the cookie does not exist, create it and set value
                    cookie = new Cookie("LoginCookie", username);
                    cookie.setMaxAge(Integer.MAX_VALUE);
                    System.out.println("Add cookie");
                    resp.addCookie(cookie);
                }

                // create a session to show that we are logged in
                session = req.getSession(true);
                session.setAttribute("login", user_id);
                session.setAttribute("username",username);
            } else {
                System.out.println(user_id + " session null");
                // Display the login page. If the cookie exists, set login
                resp.sendRedirect(req.getContextPath() + "/Login");
            }
        } else {
            // 或未注销，重新加载该页面，session不为空
            System.out.println(user_id + " session");

            session.setAttribute("login",user_id);

        }
    }

}
