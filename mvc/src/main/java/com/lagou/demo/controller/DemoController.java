package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.LagouAutowired;
import com.lagou.edu.mvcframework.annotations.LagouController;
import com.lagou.edu.mvcframework.annotations.LagouRequestMapping;
import com.lagou.edu.mvcframework.annotations.MySecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@LagouController
@LagouRequestMapping("/demo")
@MySecurity({"gebi","laowang"})
public class DemoController {


    @LagouAutowired
    private IDemoService demoService;


    /**
     * URL: /demo/query?name=lisi
     * @param request
     * @param response
     * @param username
     * @return
     */
    @LagouRequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response,String username) {
        return demoService.get(username);
    }

    /**
     * 通过注解提供访问权限
     * 只有 xiaobai,xiaobaitu 两个用户可以访问 （注意：xiaohei加入黑名单 ， gebi和laowang 加入白名单）
     * URL: /demo/queryByAuthorityDemo1?username=xiaobai
     * @param request
     * @param response
     * @param username
     * @return
     */
//    @MySecurity({"xiaobai","xiaobaitu"})
    @LagouRequestMapping("/queryByAuthorityDemo1")
    public String queryByAuthorityDemo1(HttpServletRequest request, HttpServletResponse response,String username) {
        return demoService.get(username);
    }
    /**
     * 通过注解提供访问权限
     * 只有 zhangsan,lisi 可以访问（注意：xiaohei加入黑名单 ， gebi和laowang 加入白名单）
     * URL: /demo/queryByAuthorityDemo2?username=zhangsan
     * @param request
     * @param response
     * @param username
     * @return
     */
//    @MySecurity({"zhangsan","lisi"})
    @LagouRequestMapping("/queryByAuthorityDemo2")
    public String queryByAuthorityDemo2(HttpServletRequest request, HttpServletResponse response,String username) {
        return demoService.get(username);
    }
}
