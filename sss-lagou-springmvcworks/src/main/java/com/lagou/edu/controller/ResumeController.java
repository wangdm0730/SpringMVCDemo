package com.lagou.edu.controller;

import com.lagou.edu.pojo.Resume;
import com.lagou.edu.pojo.UserPO;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author wangdm
 * @description  实现简单的有登录验证的crud系统
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    private IResumeService resumeService;

    @RequestMapping(value = "/loginResume.json", method = RequestMethod.POST)
    public String login(UserPO user, Model model, HttpSession session) {
        //获取用户名和密码
        String username = user.getUsername();
        String password = user.getPassword();
        //需求：用户名密码固定是admin/admin
        if (username != null && username.equals("admin") && password != null && password.equals("admin")) {
            //将用户对象添加到Session中
            session.setAttribute("USER_SESSION", user);
            //重定向到主页面的跳转方法
            return "redirect:queryAll.json";
        }
        model.addAttribute("msg", "用户名或密码错误，请重新登录！");
        return "redirect:../login.jsp";
    }

    @RequestMapping(value = "/queryAll.json",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView queryAll(){
        List<Resume> data = resumeService.queryAll();
        // 封装了数据和页面信息的 ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        // addObject 其实是向请求域中request.setAttribute("date",date);
        modelAndView.addObject("data",data);
        // 视图信息(封装跳转的页面信息) 逻辑视图名
        modelAndView.setViewName("list");
        return modelAndView;
    }

    @RequestMapping(value = "/saveResume.json",method = {RequestMethod.POST})
    public String saveResume(Resume resume){
        Resume one = resumeService.findOne(resume.getId());
        if(one != null){
            return "error";
        }
        resumeService.saveOne(resume);
        return "redirect:queryAll.json";
    }

    @RequestMapping(value = "/deleteResume.json",method = {RequestMethod.GET})
    public String deleteResume(Long id){
        resumeService.deleteById(id);
        return "redirect:queryAll.json";
    }

    @RequestMapping(value = "/queryResumeById.json")
    public String queryResumeById(Long id,HttpServletRequest request){
        Resume resume = resumeService.findOne(id);
        Long data = resume.getId();
        return "redirect:../update.jsp?id="+data;
    }

    @RequestMapping(value = "/updateResume.json")
    public String updateResume(Resume resume){
        resumeService.updateOne(resume);
        return "redirect:queryAll.json";
    }
}
