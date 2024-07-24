<%@ page import="java.util.Date" %>
<%@ page import="java.util.UUID" %><%--
  Created by IntelliJ IDEA.
  User: yuanxuteng
  Date: 13/04/2023
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String a = "abc";
%>
<html>
  <head>
    <title>Welcome Children's Hospital Los Angeles</title>
  </head>
  <body>
  <h1>Children's Hospital Los Angeles</h1>
  <%
    Date date = new Date();
    out.print("<h2 align=\"center\">"+"Your Access Time (Server's time)</h2>");
    out.println( "<h2 align=\"center\">" +date.toString()+"</h2>");
    out.println("<h2> Your IP address is "+ request.getRemoteAddr() +"</h2>");
  %>

    <form action="userLogin" method="post" id="loginForm">
      User name: <input type="text" name="userid" id="uid" value="${messageModel.object.userName}"><br>
      Password : <input type="password" name="password" id="uPassword" value=""><br>
      <%  UUID uuid = UUID.randomUUID();
          String salt = uuid.toString().replaceAll("-", "").substring(0, 16);
          HttpSession saltSession = request.getSession();
          saltSession.setAttribute("salt", salt);
      out.println("<input type=\"hidden\" id=\"uSalt\" value=\""+salt+"\">");
      %>
      <span id="msg" style="color: red">${messageModel.msg}</span><br>
      <button type="button" id="loginButton">Login</button>
    </form>
  <a href="auditLogin.jsp">To Access Audit Log, Click Here! </a>
  </body>
  <script type="text/javascript" src="js/jQuery.js"></script>
  <script type="text/javascript" src="js/loginVerifyPlusSha256.js"></script>
  <script>
  </script>
</html>
