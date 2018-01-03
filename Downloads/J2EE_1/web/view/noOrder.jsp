<%--
  Created by IntelliJ IDEA.
  User: qianzhihao
  Date: 2018/1/3
  Time: 上午10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>NoOrder</title>
</head>
<body>
<h1>Sorry, you don't have any order,<%=session.getAttribute("username")%>.</h1><br>
<%
    int user = pageContext.getServletContext().getAttribute("user")==null?0:(int)pageContext.getServletContext().getAttribute("user");
    int all = pageContext.getServletContext().getAttribute("all")==null?0:(int)pageContext.getServletContext().getAttribute("all");
%>
游客人数：<%=all-user%><br>
登录人数：<%=user%><br>
总人数：<%=all%><br>
<form method="GET" action="<%=response.encodeURL(request.getContextPath() + "/Login")%>">
    </p>
    <input type="submit" name="Logout" value="Logout">
</form>
</body>
</html>
