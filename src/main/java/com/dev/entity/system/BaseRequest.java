package com.dev.entity.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BaseRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Builder<T>{
        // 请求实体
        @Setter @Getter
        private T requestData;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class BuilderPage<T>{

        //默认的页数是1条
        @Setter @Getter
        private int pageNo=1;

        //默认每页显示的条数是10条
        @Setter @Getter
        private int pageSize=10;

        //使用分页查询的时候会在dao层中拦截到查询结果的总条数，映射到这个字段上
        @Setter @Getter
        private long totalNum;

        // 请求实体
        @Setter @Getter
        private T requestData;
    }

}
