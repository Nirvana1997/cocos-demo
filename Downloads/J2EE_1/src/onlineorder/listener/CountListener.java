package onlineorder.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

/**
 * @author qianzhihao
 * @version 2017/12/22
 */
public class CountListener implements HttpSessionListener,ServletContextListener{
    private DataSource datasource = null;

    private static int all = 0;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();

        all++;
        System.out.println("++");
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        context.setAttribute("all",all);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        if(null!=session.getAttribute("login")){
            int user = session.getServletContext().getAttribute("user")==null?0:(int)session.getServletContext().getAttribute("user");
            session.getServletContext().setAttribute("user",user-1);
        }
        all--;
        System.out.println("--");
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        context.setAttribute("all",all);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("context-init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("context-destroyed");
    }
}
