package com.dev.mapper;

import java.util.List;
import java.util.Map;

public interface BasicMapper {
    List<Map<String, Object>> getData(Map<String, Object> paramMap);

}
