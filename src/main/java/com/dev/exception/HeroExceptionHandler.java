package com.dev.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.entity.system.BaseResponse;
import com.dev.entity.system.ConstantUtil;

/**
 * @ClassName: HeroExceptionHandler
 * @Description: HeroExceptionHandler
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:44:39
 */
@ControllerAdvice
public class HeroExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseResponse heroRuntimeException(MissingServletRequestParameterException e) {
        logger.error("请求参数异常: " + e.getMessage());
        return new BaseResponse(ConstantUtil.ResponseEnum.SYSTEM_ERROR.getCode() , e.getMessage());
    }

    @ExceptionHandler(HeroException.class)
    @ResponseBody
    public BaseResponse heroExceptionHandler(HeroException e) {
        logger.error("房屋租赁系统异常" , e);
        return new BaseResponse(ConstantUtil.ResponseEnum.SYSTEM_ERROR.getCode() , ConstantUtil.ResponseEnum.SYSTEM_ERROR.getMessage());
    }

    @ExceptionHandler(HeroRuntimeException.class)
    @ResponseBody
    public BaseResponse heroRuntimeException(HeroRuntimeException e) {
        logger.error("房屋租赁系统异常" , e);
        return new BaseResponse(ConstantUtil.ResponseEnum.SYSTEM_ERROR.getCode() , e.getMessage());
    }
}
