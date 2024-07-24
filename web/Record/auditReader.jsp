<%@ page import="com.audit.BlockCheck" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.mysql.cj.Session" %>
<%@ page import="com.audit.AuditLog" %>
<%@ page import="com.audit.Block" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %><%--
  Created by IntelliJ IDEA.
  User: yuanxuteng
  Date: 29/04/2023
  Time: 01:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession session2 = request.getSession();
    int returnCode = (int) session2.getAttribute("returnCode");
    String newStr = (String) session2.getAttribute("newStr");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Welcome Auditor, ${user.userName}</h1>
<h2>Blockchain State Code: <%= returnCode%></h2>
<text> -1="Empty Log"，0="Missing block or files broken"，1="OK"，2="Blockchain Hash verify failed"</text>
<pre>
        <%= newStr %>
</pre>
<h3>Check Log Block Signature:</h3>
<form name="myForm" onsubmit="return validateForm()" action="checkSig" method="post">
    Block ID:<input type="text" name="logID" value="${LogIdrec}">
    <input type="submit" value="Submit">
</form>
<span id="msg" style="color: red">${recMsg}</span><br>
</body>
<script>
    function validateForm() {
        var x = document.forms["myForm"]["logID"].value;
        if (isNaN(x) || x == "") {
            alert("logID must be digits");
            return false;
        }
    }
</script>

</html>