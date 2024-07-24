<%@ page import="org.apache.ibatis.session.SqlSession" %>
<%@ page import="com.util.getSqlSession" %>
<%@ page import="com.mapper.userMapper" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static com.service.getPatients.getPatients" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.util.Merkle" %>
<%@ page import="com.sun.org.apache.xpath.internal.operations.Bool" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<User> patientList = getPatients();
%>
<html>
<script type="text/javascript" src="js/jQuery.js"></script>
<head>
    <title>Welcome Administrator</title>
</head>
<body>
    <h1>Welcome Administrator, ${user.userName}</h1>
    <%--    添加一个新的record--%>
    <h3>Add a Patient Record:</h3><br>
    <form method="post" action="newRecord">
        Select Patient: <select name="pName"><br>
        <c:forEach var = "item" items="<%=patientList%>">
            <option value="${item.userId}">id: ${item.userId}, Name:${item.userName}</option>
        </c:forEach>
        </select><br>
        Record:<br>
        <textarea name="pRecord" cols="150" rows="20"></textarea><br>
        <input type="submit" value="Submit Record">
    </form>
    <%--查询病历--%>
    <h3>Read Record:</h3><br>
    <form method="post" action="findRecordByUid">
        Select Patient: <select name="pName"><br>
        <c:forEach var = "item" items="<%=patientList%>">
            <option value="${item.userId}">id: ${item.userId}, Name:${item.userName}</option>
        </c:forEach>
    </select><br>
        <input type="submit" value="Submit Finding">
    </form>
    <h6>Select Record</h6>
    <form action="readRecord" method="get">
        <c:forEach var = "item" items="${pRrdList}">
            <input type="radio" name="rec" value="${item.recordId}">${item.recordId}; Creat Time:${item.creatTime}<br>
        </c:forEach>
        <input type="submit" value="Read" onclick="return validateForm()">
    </form>
    <h3>integrity: ${sessionScope.integrity}</h3>
</body>

<%--这个JS是防止表单空提交--%>
<script>
    function validateForm() {
        var radios = document.getElementsByName("rec");
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) {
                return true;
            }
        }
        alert("Please select a record!");
        return false;
    }
</script>

</html>
