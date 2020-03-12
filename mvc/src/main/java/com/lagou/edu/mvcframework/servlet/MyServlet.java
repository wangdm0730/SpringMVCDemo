package com.lagou.edu.mvcframework.servlet;

import com.lagou.edu.mvcframework.annotations.LagouAutowired;
import com.lagou.edu.mvcframework.annotations.LagouController;
import com.lagou.edu.mvcframework.annotations.LagouRequestMapping;
import com.lagou.edu.mvcframework.annotations.LagouService;
import com.lagou.edu.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyServlet extends HttpServlet {

    //缓存配置文件
    private static Properties properties = new Properties();

    // 缓存扫描到的类的全限定类名
    private static List<String> classNameList = new ArrayList<>();
    // ioc容器
    private static Map<String,Object> ioc = new HashMap<>();

     // 存储url和Method之间的映射关系
    List<Handler> UrlAndMethodList = new ArrayList<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1 加载配置文件 springmvc.properties

        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoad(contextConfigLocation);


        // 2 扫描相关的类，扫描注解
        doScan(properties.getProperty("scanPackage"));


        // 3 初始化bean对象（实现ioc容器，基于注解）
        doInstance();

        // 4 实现依赖注入
        doAutowird();

        // 5 构造一个HandlerMapping处理器映射器，将配置好的url和Method建立映射关系
        initHandlerMapping();

        System.out.println("lagou mvc 初始化完成....");

        // 等待请求进入，处理请求
    }

    /**
     * 加载配置的方法
     * @param contextConfigLocation
     */
    private void doLoad(String contextConfigLocation) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.out.println("配置文件加载流失败");
            e.printStackTrace();
        }
    }

    /*
        构造一个HandlerMapping处理器映射器
        最关键的环节
        目的：将url和method建立关联
     */
    private void initHandlerMapping() {

        if (ioc.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry : ioc.entrySet()){
            // 获取ioc中当前遍历的对象的class类型
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(LagouController.class)){
                continue;
            }
            String baseurl = "";
            if(aClass.isAnnotationPresent(LagouRequestMapping.class)){
                LagouRequestMapping annotation = aClass.getAnnotation(LagouRequestMapping.class);
                String value = annotation.value();
                baseurl = value;
            }
            // 获取方法
            Method[] methods = aClass.getMethods();
            for (Method method:methods){
                if(!method.isAnnotationPresent(LagouRequestMapping.class)){
                    continue;
                }
                LagouRequestMapping annotation = method.getAnnotation(LagouRequestMapping.class);
                String value = annotation.value();
                String url = baseurl+value;
                // 把method所有信息及url封装为一个Handler
                Handler handler = new Handler(entry.getValue(), method, Pattern.compile(url));
                // 计算方法的参数位置信息  // query(HttpServletRequest request, HttpServletResponse response,String name)
                Parameter[] parameters = method.getParameters();
                for (int i=0 ; i<parameters.length;i++) {
                    if(parameters[i].getType() == HttpServletRequest.class||parameters[i].getType()==HttpServletResponse.class){
                        // 如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                        handler.getParamIndexMapping().put(parameters[i].getType().getSimpleName(),i);
                    }else{
                        handler.getParamIndexMapping().put(parameters[i].getName(),i);
                    }
                }

                // 建立url和method之间的映射关系（map缓存起来）
                UrlAndMethodList.add(handler);
            }
        }
    }

    //  实现依赖注入
    private void doAutowird() {
        if(ioc.isEmpty()){
            return;
        }
        // 有对象，再进行依赖注入处理
        // 遍历ioc中所有对象，查看对象中的字段，是否有@LagouAutowired注解，如果有需要维护依赖注入关系
        for (Map.Entry<String, Object> stringObjectEntry : ioc.entrySet()) {
            // 获取bean对象中的字段信息
            Field[] declaredFields = stringObjectEntry.getValue().getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(!declaredField.isAnnotationPresent(LagouAutowired.class)){
                    continue;
                }
                //需要注入的id
                String beanName = declaredField.getAnnotation(LagouAutowired.class).value();
                if("".equals(beanName)){
                    //如果没有赋值value，就按照类型来注入
                    beanName = declaredField.getType().getName();
                }
                //开启赋值
                declaredField.setAccessible(true);
                try {
                    //依赖注入Lagouwired注解的属性
                    declaredField.set(stringObjectEntry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // ioc容器
    // 基于classNames缓存的类的全限定类名，以及反射技术，完成对象创建和管理
    private void doInstance() {
        if(classNameList.isEmpty()){return;}
        for (String s : classNameList) {
            try {
                Class<?> aClass = Class.forName(s);
                //区分controller service
                if(aClass.isAnnotationPresent(LagouController.class)){
                    // controller的id此处不做过多处理，不取value了，就拿类的首字母小写作为id，保存到ioc中
                    String lowerFirst = lowerFirst(aClass.getSimpleName());
                    Object o = aClass.newInstance();
                    ioc.put(lowerFirst,o);
                }else if(aClass.isAnnotationPresent(LagouService.class)){
                    //获取service注解
                    LagouService serviceAnnotation = aClass.getAnnotation(LagouService.class);
                    String value = serviceAnnotation.value();
                    //如果注解中有值按照value来注入，如果是空  还是按照首字母小写的方式来注入
                    if(!"".equals(value)){
                        ioc.put(value,aClass.newInstance());
                    }else{
                        ioc.put(lowerFirst(aClass.getSimpleName()),aClass.newInstance());
                    }
                    // service层往往是有接口的，面向接口开发，此时再以接口名为id，放入一份对象到ioc中，便于后期根据接口类型注入
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        ioc.put(anInterface.getName(),aClass.getNestHost());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    // 首字母小写方法
    public String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }




    // 扫描类
    // scanPackage: com.lagou.demo  package---->  磁盘上的文件夹（File）  com/lagou/demo
    public void doScan(String scanPackage) {
        if(StringUtils.isEmpty(scanPackage)){return;}
        String packPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("\\.", "/");
        File file = new File(packPath);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if(file1.isDirectory()){
                //com.lagou.demo.controller
                String filePath = scanPackage + "." + file.getName();
                doScan(filePath);
            }else if(file1.getName().endsWith(".class")){
                String className = packPath + "." + file1.getName().replaceAll(".class", "");
                //缓存扫描到的ClassName
                classNameList.add(className);
            }
        }
    }
    // 加载配置文件


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理请求：根据url，找到对应的Method方法，进行调用
        // 获取uri
        String requestURI = req.getRequestURI();
//        Method method = handlerMapping.get(requestURI);// 获取到一个反射的方法
        // 反射调用，需要传入对象，需要传入参数，此处无法完成调用，没有把对象缓存起来，也没有参数！！！！改造initHandlerMapping();
//        method.invoke() //


        // 根据uri获取到能够处理当前请求的hanlder（从handlermapping中（list））

        // 参数绑定
        // 获取所有参数类型数组，这个数组的长度就是我们最后要传入的args数组的长度


        // 根据上述数组长度创建一个新的数组（参数数组，是要传入反射调用的）

        // 以下就是为了向参数数组中塞值，而且还得保证参数的顺序和方法中形参顺序一致


        // 遍历request中所有参数  （填充除了request，response之外的参数）






        // 最终调用handler的method属性


    }

}
