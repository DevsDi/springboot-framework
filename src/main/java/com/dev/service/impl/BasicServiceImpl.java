package com.dev.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.entity.system.LoginUser;
import com.dev.exception.HeroRuntimeException;
import com.dev.mapper.BasicMapper;
import com.dev.service.BasicService;
import com.dev.util.ExportUtil;

@Service("basicService")
public class BasicServiceImpl implements BasicService {

    @Autowired
    BasicMapper basicMapper;

    @Override
    public List<Map<String, Object>> getData(Map<String, Object> paramMap) throws HeroRuntimeException {
        return basicMapper.getData(paramMap);
    }


    @Override
    public File exportListExcelWithHeaders(String[][] exportInfo , List<Object> list , String fileName , HttpServletRequest httpRequest , HttpServletResponse httpResponse) throws Exception {
        String agent = httpRequest.getHeader("USER-AGENT");
        String codedfilename = null;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/vnd.ms-excel; charset=UTF-8");
        if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie
            codedfilename = java.net.URLEncoder.encode(fileName , "UTF8");
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等
            codedfilename = new String(fileName.getBytes("UTF-8") , "iso-8859-1");
        }
        httpResponse.setHeader("Content-disposition" , "attachment; filename=\"" + codedfilename + "\"");// 组装附件名称和格式
        ServletOutputStream outputStream = httpResponse.getOutputStream();

        ExportUtil exportUtil = new ExportUtil<>();
        return exportUtil.exportListExcelWithHeaders(exportInfo , list , fileName , outputStream);
    }

}
