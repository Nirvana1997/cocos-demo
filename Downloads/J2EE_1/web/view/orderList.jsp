<%--
  Created by IntelliJ IDEA.
  User: qianzhihao
  Date: 2018/1/2
  Time: 下午3:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="order" uri="/WEB-INF/tlds/orderInfo.tld" %>
<html>
<head>
    <title>OrderList</title>
</head>
<body>
<h2>Welcome!<%=session.getAttribute("username")%></h2>
<br>
<table width="100%" border="1" cellpadding="0" cellspacing="1">
    <thead align="center">OrderList</thead>
    <tbody>
    <tr>
        <th>Order Id</th>
        <th>User Id</th>
        <th>Name</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Order Time</th>
        <th>In Stock</th>
    </tr>
    <order:orderInfo/>
    </tbody>
</table>
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
