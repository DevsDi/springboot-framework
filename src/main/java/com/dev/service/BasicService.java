package com.dev.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dev.entity.system.LoginUser;
import com.dev.exception.HeroRuntimeException;

public interface BasicService {

    List<Map<String, Object>> getData(Map<String, Object> paramMap) throws HeroRuntimeException;

    File exportListExcelWithHeaders(String[][] exportInfo , List<Object> list , String fileName , HttpServletRequest HttpRequest , HttpServletResponse response) throws Exception;
}
