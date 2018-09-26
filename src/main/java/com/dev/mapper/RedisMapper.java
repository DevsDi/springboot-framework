package com.dev.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisMapper {

    @Autowired
    private StringRedisTemplate template;

    public void setKeyForever(String key , String value) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key , value);
    }

    //将某个值保存在redis当中。
    public void setKey(String key , String value , long timeout , TimeUnit unit) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key , value , timeout , unit);
    }

    public String getValue(String key) {
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    //查看某个值redis中存不存在
    public boolean hasKey(String key) {
        return template.hasKey(key);
    }

    public void deleteKey(String key) {
        template.delete(key);
    }


}
