package onlineorder.servlets;


import onlineorder.action.business.OrderListBean;
import onlineorder.factory.EjbFactory;
import onlineorder.service.OrderService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

	OrderService orderService = (OrderService) EjbFactory.getEJB("OrderServiceImpl", "onlineorder.service.OrderService");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowMyOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {

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

		}

	}

	public void getOrderList(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession(true);
		ServletContext context = getServletContext();

        OrderListBean orderListBean = new OrderListBean();
        try {
			orderListBean.setOrderList(orderService.getOrderListByUserId(Integer.valueOf((String)req.getAttribute("login"))));
		}catch (Exception e){
            e.printStackTrace();
			System.out.println("未登录");
		}


        try {
            if (orderListBean.getSize() > 0) {
                session.setAttribute("orderList", orderListBean);
                context.getRequestDispatcher("/view/orderList.jsp").forward(req, res);
            } else {
                context.getRequestDispatcher("/view/noOrder.jsp").forward(req,res);
            }
        }catch (ServletException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
	}

}
