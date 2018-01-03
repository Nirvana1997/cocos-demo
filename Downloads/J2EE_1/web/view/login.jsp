<%@ page import="java.io.Console" %><%--
  Created by IntelliJ IDEA.
  User: qianzhihao
  Date: 2018/1/1
  Time: 上午11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script language="JavaScript" type="text/JavaScript">
    function showTips(tip){
        alert(tip);
        return true;
    }
</script>
<html>
<head>
    <title>Login</title>
    <form method="post" action="<%=response.encodeURL(request.getContextPath() + "/Login")%>">
        login:<input type="text" name="login" value="">
        password:<input type="password" name="password" value="">
        <input type="submit" name="Submit" value="Submit">
    </form>
    <%
        int user = pageContext.getServletContext().getAttribute("user")==null?0:(int)pageContext.getServletContext().getAttribute("user");
        int all = pageContext.getServletContext().getAttribute("all")==null?0:(int)pageContext.getServletContext().getAttribute("all");
    %>
    游客人数：<%=all-user%><br>
    登录人数：<%=user%><br>
    总人数：<%=all%><br>
</head>
<%
    if(session==null||session.getAttribute("loginResult")!=null) {
        System.out.println(session.getAttribute("loginResult"));
        int result = (int) session.getAttribute("loginResult");
        if (result == 0) {
            out.println("<body onLoad=\"showTips('密码错误')\">");
        }else if(result<0){
            out.println("<body onLoad=\"showTips('账号不存在')\">");
        }else {
            out.println("<body>");
        }
    }else {
        System.out.println("loginResult-null");
        out.println("<body>");
    }
%>

</body>
</html>
