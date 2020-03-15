<%@ page language="java" contentType="text/html; charset=GB18030" pageEncoding="GB18030" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div align="center">
    <h1 >信息列表</h1>
    <div style="width:15%;text-align:right;"><a href="../../insert.jsp">新增</a></div>
    <table >
        <tr>
            <td>id</td>
            <td>住址</td>
            <td>姓名</td>
            <td>电话</td>
            <td align="center" colspan="2">操作</td>
        </tr>
        <c:forEach items="${data}" var="item">
            <tr>
                <td>${item.id}</td>
                <td>${item.address}</td>
                <td>${item.name}</td>
                <td>${item.phone}</td>
                <td><a href="/resume/queryResumeById.json?id=${item.id}">修改</a></td>
                <td><a href="/resume/deleteResume.json?id=${item.id}">删除</a></td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>