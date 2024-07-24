<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
  Created by IntelliJ IDEA.
  User: yuanxuteng
  Date: 19/04/2023
  Time: 00:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%--%>
<%--    HttpSession session1 = request.getSession();--%>
<%--    ArrayList<String> arrayList = GetLog.getLogByEHR((String) session1.getAttribute("recordFileName"));--%>
<%--%>--%>
<html>
<script type="text/javascript" src="js/jQuery.js"></script>
<script type="text/javascript">
        function copyToClipboard(text) {
        var textarea = document.createElement("textarea");
        textarea.value = text;
        document.body.appendChild(textarea);
        textarea.select();
        try {
            alert("Copied to Clipboard!");
            var successful = document.execCommand("copy");
            var msg = successful ? "Copyed" : "Failed";
            console.log(msg);
        } catch (err) {
            console.log("Copy failed");
        }
        document.body.removeChild(textarea);
    }
    $(document).ready(function() {
    $("#copy").click(function (){
        let ehrMess = $("#pRecord").val();
        copyToClipboard(ehrMess)
        $("#copyRecord").submit();
    });});

    $(document).ready(function() {
        $("#print").click(function (){
            $("#printRecord").submit();
        });});
</script>
<head>
    <title>Reader ${recordFileName}</title>
</head>
<body>
<text>User Name: ${user.userName}</text><br>
<text>User ID: ${user.userId}</text><br>

<form action="renameRecord" method="post">
    File Name<input name="fileName" value=${recordFileName} readonly="readonly">
    <input type="submit" value="delete">
</form>

<text>File Name: ${sessionScope.recordFileName}</text><br>
<textarea name="pRecord" cols="150" rows="30" readonly="readonly" oncut="return false" oncopy="return false" onselectstart="return false" id="pRecord">${sessionScope.plaintext}</textarea><br>

<button id="copy">Copy</button>
<form action="copyRecord" method="post" id="copyRecord">
</form>

<button id="print" onclick="print()">Print</button><br>
<form action="printRecord" method="post" id="printRecord">
</form>

<form action="backMain" method="post">
    <input type="submit" value="back">
</form>

<%--<h5>Operation History</h5>--%>
<%--<text>Time, User ID, IP Address, Operation, EHR ID</text><br>--%>
<%--<c:forEach var = "item" items="<%=arrayList%>">--%>
<%--    <text>${item.toString()}</text><br>--%>
<%--</c:forEach>--%>

</body>
</html>
