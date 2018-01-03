package onlineorder.tag;

import onlineorder.action.business.OrderListBean;
import onlineorder.model.Order;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * @author qianzhihao
 * @version 2018/1/3
 */
public class OrderInfoHandler extends SimpleTagSupport{
    @Override
    public void doTag() throws JspException, IOException {
        OrderListBean listBean = (OrderListBean)getJspContext().findAttribute("orderList");
        JspWriter out = getJspContext().getOut();
        for(Order order:listBean.getOrderList()){
            out.println("<tr><td align='center'>"+order.getOrder_id()+"</td>");
            out.println("<td align='center'>"+order.getUser_id()+"</td>");
            out.println("<td align='center'>"+order.getName()+"</td>");
            out.println("<td align='center'>"+order.getPrice()+"</td>");
            out.println("<td align='center'>"+order.getQuantity()+"</td>");
            out.println("<td align='center'>"+order.getOrderTime()+"</td>");
            out.println("<td align='center'>"+(order.isIn_stock()?"Y":"N")+"</td>");
            out.println("</tr>");
        }
    }
}
