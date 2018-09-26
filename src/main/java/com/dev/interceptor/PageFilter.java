package com.dev.interceptor;

import java.util.ArrayList;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dev.entity.system.BaseRequest;
import com.dev.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @ClassName: PageFilter
 * @Description: 分页插件
 * @author: wen.dai
 * @date: 2018年5月22日 下午6:45:45
 */
@Aspect
@Component
public class PageFilter {

    @Around("execution(* ele.me.hero.service.impl..*.*(..))")
    public Object InterceptorService(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object object : args) {
            if (object instanceof Map) {
                Map map = (Map) object;
                if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
                    try {
                        int pageNo = Integer.parseInt(map.get("pageNo").toString());
                        int pageSize = Integer.parseInt(map.get("pageSize").toString());
                        //设置当前页和每页显示的条数
                        PageHelper.startPage(pageNo , pageSize);
                        Object result = pjp.proceed();
                        Page page = (Page) result;
                        long totalNum = page.getTotal();
                        map.put("totalNum" , totalNum);
                        return result;
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }

                }
            }
            if (object instanceof BaseRequest.BuilderPage) {
                try {
                    BaseRequest.BuilderPage request = (BaseRequest.BuilderPage) object;
                    //默认当前页数是第一页，默认每页显示的条数是10条
                    int pageNo = request.getPageNo() == 0 ? 1 : request.getPageNo();
                    int pageSize = request.getPageSize() == 0 ? 10 : request.getPageSize();
                    PageHelper.startPage(pageNo , pageSize);
                    Object result = pjp.proceed();
                    if (StringUtil.isNotNull(result) && !StringUtil.isEmptyList(result)) {
                        Page page = (Page) result;
                        long totalNum = page.getTotal();
                        request.setTotalNum(totalNum);
                        return result;
                    }
                    return new ArrayList<>();
                } catch (Exception e) {
                    throw new RuntimeException("分页插件报错");
                }

            }
        }

        return pjp.proceed();
    }
}
