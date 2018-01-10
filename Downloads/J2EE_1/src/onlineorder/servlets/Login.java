package onlineorder.servlets;

import onlineorder.factory.EjbFactory;
import onlineorder.service.LoginService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    LoginService loginService = (LoginService) EjbFactory.getEJB("LoginServiceImpl", "onlineorder.service.LoginService");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        String login = "";

        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();

        Integer ival = new Integer(1);

        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("LoginCookie")) {
                    login = cookie.getValue();
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

        context.getRequestDispatcher("/view/login.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String login = (String) request.getParameter("login");
        String password = (String) request.getParameter("password");

        ServletContext context = getServletContext();

//        try {
        int result = loginService.getUserIdAndJudgePassword(login, password);
        System.out.println("result:"+result);
        request.getSession(true).setAttribute("loginResult",result);
        if (result > 0) {
            System.out.println("密码正确。");

            //统计登录用户人数+1
            int user = context.getAttribute("user") == null ? 0 : (int) context.getAttribute("user");
            context.setAttribute("user", user + 1);

            setCookieAndSession(request, response, String.valueOf(result), login);
            response.sendRedirect(request.getContextPath() + "/ShowMyOrderServlet");
        }else {
            context.getRequestDispatcher("/view/login.jsp").forward(request, response);
        }
    }


    private void setCookieAndSession(HttpServletRequest req, HttpServletResponse resp, String user_id, String username) throws IOException {
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
                session.setAttribute("username", username);
            } else {
                System.out.println(user_id + " session null");
                // Display the login page. If the cookie exists, set login
                resp.sendRedirect(req.getContextPath() + "/Login");
            }
        } else {
            // 或未注销，重新加载该页面，session不为空
            System.out.println(user_id + " session");

            session.setAttribute("login", user_id);
            session.setAttribute("username", username);

        }
    }

}
