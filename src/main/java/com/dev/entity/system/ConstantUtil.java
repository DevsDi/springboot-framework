package com.dev.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @ClassName: ConstantUtil
 * @Description: 提示消息
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:40:21
 */
@Configuration
@PropertySource(name = "", value = "classpath:message.properties")
public class ConstantUtil {

    public static final String CURRENT_USER_ID = "current_user_id";


    private static String ITSM_MESSAGE;

    @Value("${ITSM_MESSAGE}")
    public void setItsmMessage(String itsmMessage) {
        ITSM_MESSAGE = itsmMessage;
    }

    @AllArgsConstructor
    public enum ResponseEnum {

        /**
         * 通用
         */
        OK(0 , "success"),
        FAIL(1 , "error"),
        SYSTEM_ERROR(1 , ITSM_MESSAGE),
        NETWORKEXCEPTION(1 , "网络异常，请稍后重试"),
        EHREXCEPTION(1 , "调用EHR服务失败"),
        FILEEXCEPTION(1 , "调用文件服务失败"),
        SMSEXCEPTION(1 , "调用短信服务失败"),
        ORDER_CREARE_ERROR(1 , "新增租赁单据失败"),
        PARAMETER_NOT_NULL(1 , "新增租赁单据失败");


        @Setter
        @Getter
        private int code;

        @Setter
        @Getter
        private String message;

    }


}
