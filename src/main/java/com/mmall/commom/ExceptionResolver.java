package com.mmall.commom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component//将bean注入
//全局异常
/*
*Spring MVC全局异常优点
* Spring 及Spring MVC包扫描隔离
 */
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{} Exception",httpServletRequest.getRequestURI(),e);//打印出真实的异常
        ModelAndView modelAndView=new ModelAndView(new MappingJacksonJsonView());
        //当使用Jackson2.X的时候使用MappingJackson2JsonView,当前小于2.0故使用MappingJacksonJsonView
        //开始封装json
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务端异常信息");
        modelAndView.addObject("data",e.toString());
        return modelAndView;
    }
}
