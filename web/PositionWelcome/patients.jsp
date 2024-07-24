<%@ page import="com.service.RecordFileFinder" %>
<%@ page import="com.entity.RecordFile" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.entity.User" %>
<%@ page import="static java.lang.System.out" %>
<%@ page import="com.audit.BlockCheck" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
  Created by IntelliJ IDEA.
  User: yuanxuteng
  Date: 17/04/2023
  Time: 18:23
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpSession session1 = request.getSession();
    User user = (User) session1.getAttribute("user");

    ArrayList<RecordFile> allFile= RecordFileFinder.getAllEhrFiles();
    ArrayList<RecordFile> targetFile = new ArrayList<>();
    for(RecordFile recordFile: allFile){
        if(recordFile.getPatientId() == user.getUserId()){
            targetFile.add(recordFile);
        }
    }
    String newStr = "";
    try{
        String strings = BlockCheck.queryLog(user.getUserId(), user.getUserName(), user.getUserId()).getAuditMsg();
        String title = "<br>" + "Time" + "\t\t" + "Operator ID" + "\t" + "IP Address" + "\t" + "Operation" + "\t" + "Object EHR" + "<br>";
        newStr = title + strings.replace("||", "\t");
    }catch (Exception e){
        newStr = "System maintenance";
    }

%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" src="js/jQuery.js"></script>
<head>
    <title>Hope you get well soon, ${user.userName}</title>
</head>
<body>
    <h1>${user.userName}, Hope you get well soon</h1>
    <h3>Read Record</h3>
    <form action="readRecord" method="get">
        <c:forEach var = "item" items="<%=targetFile%>">
            <input type="radio" name="rec" value="${item.recordId}">${item.recordId}; Creat Time:${item.creatTime}<br>
        </c:forEach>
        <input type="submit" value="Read">
    </form>
<h3>This is your Audit Log:</h3>
    <pre>
        <%= newStr %>
    </pre>
</body>
</html>
