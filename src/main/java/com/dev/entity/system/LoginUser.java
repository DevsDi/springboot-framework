package com.dev.entity.system;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: LoginUser
 * @Description: 登录用户信息
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:40:43
 */
public class LoginUser {

    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private String userCode;
    @Getter
    @Setter
    private String sex;
    @Getter
    @Setter
    private String mobile;
    @Getter
    @Setter
    private String psncode;
    @Getter
    @Setter
    private String psnname;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String deptCode;
    @Getter
    @Setter
    private String deptName;

}
