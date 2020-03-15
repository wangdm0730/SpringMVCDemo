<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <%
        Long id = Long.parseLong(request.getParameter("id"));
        System.out.println("被修改记录的id是："+id);
    %>
</head>
<body>
<div align="center">
    <h1>修改信息</h1>
    <a href="/resume/queryAll.json">返回主页</a>
    <form id="form2" action="/resume/updateResume.json" method="post">
        <input type="hidden" id="id" name="id" value="<%=id%>"/>
        <div>
            address<input type="text" id="address" name="address" />
        </div>
        <div>
            name<input type="text" id="name" name="name" />
        </div>
        <div>
            phone<input type="text" id="phone" name="phone" />
        </div>
        <div>
            <button type="submit" id="submit">修&nbsp;&nbsp;&nbsp;改</button>
        </div>
    </form>
</div>
</body>
</html>