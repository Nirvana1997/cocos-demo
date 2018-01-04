package onlineorder.tag;

import com.sun.deploy.net.HttpResponse;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * @author qianzhihao
 * @version 2018/1/3
 */
public class CheckSession extends BodyTagSupport {
    HttpServletResponse response;

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public int doEndTag() throws JspException {
        if(null == pageContext.findAttribute("login")){
            try {
                response.sendRedirect("/Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SKIP_PAGE;
        }else {
            return EVAL_PAGE;
        }
    }
}
