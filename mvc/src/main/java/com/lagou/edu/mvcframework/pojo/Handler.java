package com.lagou.edu.mvcframework.pojo;

import javax.sound.midi.MetaEventListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 封装handler方法相关的信息
 */
public class Handler {

    private Object controller; // method.invoke(obj,)

    private Method method;

    private Pattern pattern; // spring中url是支持正则的

    private Map<String,Integer> paramIndexMapping; // 参数顺序,是为了进行参数绑定，key是参数名，value代表是第几个参数 <name,2>
    //权限验证(true：拦截  false:放行)
    private Boolean autoItecepter=false;
    //是否有MySecurity注解(true：有  false:没有)
    private Boolean hasMySecurity=false;
    //白名单
    private List<String> handlerMappingWhite;

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }

    public Boolean getAutoItecepter() {
        return autoItecepter;
    }

    public void setAutoItecepter(Boolean autoItecepter) {
        this.autoItecepter = autoItecepter;
    }

    public Boolean getHasMySecurity() {
        return hasMySecurity;
    }

    public void setHasMySecurity(Boolean hasMySecurity) {
        this.hasMySecurity = hasMySecurity;
    }

    public List<String> getHandlerMappingWhite() {
        return handlerMappingWhite;
    }

    public void setHandlerMappingWhite(List<String> handlerMappingWhite) {
        this.handlerMappingWhite = handlerMappingWhite;
    }
}
