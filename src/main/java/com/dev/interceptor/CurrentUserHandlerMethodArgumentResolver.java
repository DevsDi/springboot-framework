package com.dev.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.dev.entity.system.ConstantUtil;
import com.dev.entity.system.LoginUser;
import com.dev.exception.HeroRuntimeException;

/**
 * @ClassName: CurrentUserHandlerMethodArgumentResolver
 * @Description: 拦截器必要校验
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:45:15
 */
@Component
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class clazz = methodParameter.getParameterType(); //得到注解类型
        CurrentUser currentUser = methodParameter.getParameterAnnotation(CurrentUser.class);
        if (currentUser != null) {
            //当注解的参数是User类型,才能注入参数
            if (clazz.isAssignableFrom(LoginUser.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter , ModelAndViewContainer mavContainer ,
                                  NativeWebRequest nativeWebRequest , WebDataBinderFactory binderFactory)
            throws Exception {

        String currentUserId = nativeWebRequest.getAttribute(ConstantUtil.CURRENT_USER_ID , RequestAttributes.SCOPE_REQUEST).toString();

        if (currentUserId != null) {
            Class clazz = methodParameter.getParameterType();

            if (clazz.isAssignableFrom(LoginUser.class)) {
                //这边实现自己的token校验
                LoginUser loginUser =  new LoginUser();//从其他渠道查询
                if (loginUser != null) {
                    return loginUser;
                }
                throw new HeroRuntimeException("token失效或者token不正常");
            }
        }
        throw new MissingServletRequestPartException("@CurrentUser注解 参数错误");
    }
}
