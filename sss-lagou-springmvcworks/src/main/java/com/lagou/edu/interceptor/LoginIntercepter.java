package com.lagou.edu.interceptor;

import com.lagou.edu.pojo.UserPO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wangdm
 * @description 自定义登录用户拦截器
 */
public class LoginIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的uri:去除http:localhost:8080这部分剩下的
        String uri = request.getRequestURI();
        //除了/loginResume.json是可以公开访问的，其他的URL都进行拦截控制
        if (uri.indexOf("/loginResume.json") >= 0) {
            return true;
        }
        //获取session
        HttpSession session = request.getSession();
        UserPO user = (UserPO) session.getAttribute("USER_SESSION");
        //判断session中是否有用户数据，如果有，则返回true，继续向下执行
        if (user != null) {
            return true;
        }
        //不符合条件的给出提示信息，并转发到登录页面
        request.setAttribute("msg", "您还没有登录，请先登录！");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
        return false;
    }
}
