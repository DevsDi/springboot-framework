package com.dev.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dev.entity.system.BaseResponse;
import com.dev.entity.system.ConstantUtil;
import com.dev.entity.system.LoginUser;
import com.dev.mapper.RedisMapper;
import com.dev.service.BasicService;
import com.dev.util.FileUtils;
import com.dev.util.StringUtil;


/**
 * @ClassName: BasicController
 * @Description: 基础信息服务
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:39:29
 */
@RestController
@RequestMapping("/be")
public class BeController {


    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private BasicService basicService;
    
    @Autowired
    private RedisMapper redisMapper;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "It works";

    }
  

    @RequestMapping(value = "/exportList", method = RequestMethod.GET)
    public BaseResponse exportListExcelWithHeaders(
            @RequestParam("token") String token ,
            @RequestParam("uuid") String uuid ,
            HttpServletRequest httpRequest , HttpServletResponse httpResponse) {
        BaseResponse<Map<String, Object>> baseResponse = new BaseResponse(ConstantUtil.ResponseEnum.OK.getCode() , ConstantUtil.ResponseEnum.OK.getMessage());

        String fileName = "tenancy.xlsx";//导出文件名

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("type_name" , "tenancy_export");
        List<Map<String, Object>> exportMaps = new ArrayList<Map<String,Object>>();//所有字段对应关系
        Map<String, Object> allColsMap = new HashMap<>();
        for (Map<String, Object> map : exportMaps) {
            allColsMap.put(StringUtil.getNotNullStr(map.get("ecode")) , map.get("ename"));
        }

        String paramMapStr = redisMapper.getValue(uuid);

        if (StringUtil.isNull(paramMapStr)) {
            baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("导出条件异常，请重试");
            return baseResponse;
        }
        Map<String, Object> paramMap = JSON.parseObject(paramMapStr , Map.class);
        logger.info("查询条件=====>>" + paramMapStr);

        List<Object> list = new ArrayList<>();//根据查询条件查询列表

        List<String> conditionList = (List<String>) paramMap.get("columns");//获取自定义导出列名
        conditionList.add(0 , "tenancy_no");

        String[] fieldNames = conditionList.toArray(new String[conditionList.size()]);//表头
        StringBuffer showNamesBuffer = new StringBuffer();
        for (int i = 0; i < fieldNames.length; i++) {
            showNamesBuffer.append("," + allColsMap.get(fieldNames[i]));
        }

        String[] showNames = showNamesBuffer.substring(1).split(",");//字段名称
        String[][] exportInfo = {showNames , fieldNames};//表头，字段名

        try {
            File file = basicService.exportListExcelWithHeaders(exportInfo , list , fileName , httpRequest , httpResponse);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResponse;
    }
    
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public BaseResponse downloadFile(@RequestParam("annexid") int annexid,@RequestParam("token") String token, HttpServletResponse response) {
        BaseResponse<Map<String, Object>> baseResponse = new BaseResponse(ConstantUtil.ResponseEnum.OK.getCode() , ConstantUtil.ResponseEnum.OK.getMessage());

		//1.查询用户登录情况
		Map<String, String> map=new HashMap<>();//2.查询附件存在情况
        String fid=StringUtil.getNotNullStr(map.get("fid"));
        String annex_name=StringUtil.getNotNullStr(map.get("annex_name"));
        String annexTempPath="";//设置附件下载的临时路径
        
//        String fileurl = fileSystemServiceImpl.getFile(fid , true);
        String fileurl ="";//文件服务器返回url
        File tempFile=FileUtils.saveUrlAs(fileurl, annexTempPath,annex_name);
        
         try {
 
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(annexTempPath+File.separator+annex_name));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
        	response.setHeader("Content-type", "text/html;charset=UTF-8");
        	response.setCharacterEncoding("gbk");

            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(StringUtil.getNotNullStr(map.get("annex_name")).getBytes("utf-8"),"ISO-8859-1"));
            response.addHeader("Content-Length", "" + tempFile.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
        	baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("下载失败");
            return baseResponse;
        }

        tempFile.delete();
        return baseResponse;
    }
}
