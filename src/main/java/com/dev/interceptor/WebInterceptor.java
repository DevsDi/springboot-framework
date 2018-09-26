package com.dev.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dev.entity.system.ConstantUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

/**
 * @ClassName: WebInterceptor
 * @Description: 拦截器
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:46:08
 */
public class WebInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
//在zuul中跨域已设置，代码中移除跨域设置    	
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,x-auth");

        if (!(handler instanceof HandlerMethod)) {//如果对象不是handlermethod直接返回
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //NeedAuth也可以加在方法上
        NeedAuth needAuth = method.getAnnotation(NeedAuth.class);
        NeedAuth classNeedAuth = handlerMethod.getBeanType().getAnnotation(NeedAuth.class);
        String tokenHeadString = request.getHeader("x-auth");

        try {
            if (!StringUtils.isEmpty(tokenHeadString) //token head String 不为空
                    && (needAuth != null || classNeedAuth != null)) {//如果是null,那么需要授权

                String token = tokenHeadString;

                if (token != null) {
                    //成功登陆,记录日志
                    //将token放入request中
                    String current_user_id = token;
                    request.setAttribute(ConstantUtil.CURRENT_USER_ID , current_user_id);
                    return true;

                } else {
                    logger.error("非法token:{}" , token);
                    throw new RuntimeException("非法token:{}");
                }
            }
            if (StringUtils.isEmpty(tokenHeadString) && (needAuth != null || classNeedAuth != null)) {
                throw new RuntimeException("token不能为空:{}");
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("非法token:{}");
        } catch (Exception e) {
            logger.error(e.getMessage() , e);
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request , HttpServletResponse response , Object handler , ModelAndView modelAndView) throws Exception {
        String UserAgent = request.getHeader("User-Agent");
        logger.info("UserAgent===" + UserAgent);
        String remoteIp = request.getRemoteHost() + "," + request.getRequestURI();
        logger.info("remoteIp" + remoteIp);
    }

    @Override
    public void afterCompletion(HttpServletRequest request , HttpServletResponse response , Object handler , Exception ex) throws Exception {
    }
}
