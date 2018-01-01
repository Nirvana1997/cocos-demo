package onlineorder.servlets;


import onlineorder.model.Order;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/*
 此Servlet未使用DAO、MVC设计，要求只有已登录的客户才能查看自己购买的股票(数据源)，
 实现：已登录用户，才能查看；
            未登录用户，转去登录；
            从登录提交到此的用户，创建session，跟踪登录状态；如果是来自初次登录的用户，则创建cookie；并查看自己购买的股票；
            通过刷新页面/或已创建session的页面访问，则查看自己购买的股票。
 */

/**
 * Servlet implementation class StockListServlet
 */
@WebServlet("/ShowMyOrderServlet")
public class ShowMyOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowMyOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
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

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        processRequest(request, response);

	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		resp.setContentType("text/html;charset=utf-8");
//		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession(false);

		//若session为null说明未登录，退回登录界面
		if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");

		} else {
			// 或未注销，重新加载该页面，session不为空
			String loginValue = (String) session.getAttribute("login");
			System.out.println(loginValue + " session");

			req.setAttribute("login", loginValue);
			getOrderList(req, resp);
			displayMyStocklistPage(req, resp);
			displayCount(req,resp);
			displayLogoutPage(req, resp);

		}

	}

	public void getOrderList(HttpServletRequest req, HttpServletResponse res) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		ArrayList list = new ArrayList();
		Statement sm = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println(req.getAttribute("login"));

			stmt = connection.prepareStatement("select * from `order` where user_id = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			while (result.next()) {
				Order order = new Order();
				order.setName(result.getString("name"));
				order.setPrice(result.getDouble("price"));
				order.setOrderTime(result.getDate("order_time"));
				order.setQuantity(result.getInt("quantity"));
				order.setOrder_id(result.getInt("order_id"));
				order.setUser_id(result.getInt("user_id"));
				order.setIn_stock(result.getBoolean("in_stock"));
				list.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		req.setAttribute("list", list);

	}

	public void displayLogoutPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		// 注销Logout
		out.println("<form method='GET' action='" + res.encodeURL(req.getContextPath() + "/Login") + "'>");
		out.println("</p>");
		out.println("<input type='submit' name='Logout' value='Logout'>");
		out.println("</form>");
		out.println("<p>Servlet is version @version@</p>");
		out.println("</body></html>");

	}

	public void displayMyStocklistPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ArrayList list = (ArrayList) req.getAttribute("list"); // resp.sendRedirect(req.getContextPath()+"/MyStockList");

        //判断是否存在无库存情况
        boolean notInStock = false;

		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<table width='650' border='0' >");
		out.println("<tr>");
		out.println("<td width='650' height='80' background='" + req.getContextPath() + "/image/top.jpg'>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p>Welcome " + req.getAttribute("username") + "</p>");

		out.println("My Stock List: <br>");
		out.println("<table border='1'>");
		out.println("<tr>");
		out.println("<th>order_id</th>");
		out.println("<th>user_id</th>");
		out.println("<th>order_time</th>");
		out.println("<th>quantity</th>");
		out.println("<th>price</th>");
		out.println("<th>name</th>");
		out.println("<th>in_stock</th>");

		for (int i = 0; i < list.size(); i++) {
			Order order = (Order) list.get(i);
            if(!order.isIn_stock()){
                notInStock = true;
            }
			out.println(order);
        }
        out.println("</table>");
		out.println("</p>");
		// 点击here，刷新该页面，会话有效
		out.println("Click <a href='" + res.encodeURL(req.getRequestURI()) + "'>here</a> to reload this page.<br>");

		if(notInStock){
            out.print("<body onLoad=\"checkForm()\"><script language=\"JavaScript\" type=\"text/JavaScript\">function checkForm(){ \n" +
                    "                    alert(\"存在缺货订单!\");return true;}</script></body>");
        }
	}

	public void displayCount(HttpServletRequest req, HttpServletResponse res) throws IOException{
	    PrintWriter out = res.getWriter();
	    int all = getServletContext().getAttribute("all")==null?0:(int)getServletContext().getAttribute("all");
	    int user = getServletContext().getAttribute("user")==null?0:(int)getServletContext().getAttribute("user");
	    out.println("游客人数："+(all-user));
	    out.println("登录用户："+user);
	    out.println("总人数："+all);
    }

}
