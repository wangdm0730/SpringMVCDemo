<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<body>
</body>
<head>
</head>
<body>
<div align="center">
    <h1>添加信息</h1>
    <a href="/resume/queryAll.json">返回主页</a>
    <form id="form0" action="/resume/saveResume.json" method="post">
        <div>
            id<input type="text" id="id" name="id" />
        </div>
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
            <button type="submit" id="submit">添&nbsp;&nbsp;&nbsp;加</button>
        </div>
    </form>
</div>
</body>
</html>