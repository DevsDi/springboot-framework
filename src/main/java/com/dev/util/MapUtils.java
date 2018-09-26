package com.dev.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.dev.entity.system.BaseResponse;
import com.dev.entity.system.ConstantUtil;

public class MapUtils {

    /**
     * 使用获取map中对应value
     *
     * @param map
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T getValue(Map<String, Object> map, String key,
                                 Class<T> clazz) {
        if (map != null && key != null) {
            Object value = map.get(key);
            if (value != null)
                return (T) value;

        }
        if (clazz != null && clazz.isAssignableFrom(String.class)) {
            return (T) "";
        } else {
            return null;
        }
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByValue(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(
                map.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);
                System.out.println(i+"=====>"+value);
                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
  
    //房屋租赁专用
    public static List<Map<String,Object>> getSimpleMapList(List<Map<String,Object>> list,int n){
    	List<Map<String,Object>> simpleList=new ArrayList<>();
    	Map<String, Object> simpleMap=null;
    	
    	for (Map<String, Object> map : list) {
    		simpleMap=new HashMap<>();
    		if (n!=3) {
    			simpleMap.put("id", map.get("id"));	
			}
			simpleMap.put("key", map.get("code"));
			simpleMap.put("value", map.get("code"));
			simpleMap.put("title", map.get("name"));
//			if (StringUtil.isNotNull(map.get("levelCode"))) {
//				simpleMap.put("level", map.get("levelCode"));
//			}
		
			simpleList.add(simpleMap);
		}
    	
    	return simpleList;
    }
    
    

    public static String escapeExprSpecialWord(String keyword) {  
        if (StringUtils.isNotBlank(keyword)) {  
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "{", "}", "?", "^", "|" ,"_","%"};  
            for (String key : fbsArr) {  
                if (keyword.contains(key)) {  
                    keyword = keyword.replace(key, "\\" + key);  
                }  
            }  
        }  
        return keyword;  
    }  
    
	public static Map<String, Object> prcocessParamMap(Map<String, Object> paramMap){
		
			if (paramMap != null) {
				for (String key : paramMap.keySet()) {
					paramMap.replace(key, escapeExprSpecialWord(StringUtil.getNotNullStr(paramMap.get(key))));
				}
			}
			return paramMap;
    }

	public static BaseResponse<Map<String, Object>> checkIsValidParams(Map<String, Object> tenancyMap) {
		
		BaseResponse<Map<String, Object>> baseResponse = new BaseResponse<>(ConstantUtil.ResponseEnum.OK.getCode() , ConstantUtil.ResponseEnum.OK.getMessage());
     
		if (StringUtil.isNull(tenancyMap.get("contract_no"))||tenancyMap.get("contract_no").toString().length()>30) {
      	    baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("合同编号不能为空或者长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("province"))&& tenancyMap.get("province").toString().length()>10) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("省份长度过长");
          return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("city"))&& tenancyMap.get("city").toString().length()>10) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("城市长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("resident_num"))&& tenancyMap.get("resident_num").toString().length()>10) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("常驻人数长度过长");
          return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("peak_num"))&& tenancyMap.get("peak_num").toString().length()>10) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("人数峰值长度过长");
          return baseResponse;
		}

      if (StringUtil.isNotNull(tenancyMap.get("house_addr"))&& tenancyMap.get("house_addr").toString().length()>80) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("房屋地址长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNull(tenancyMap.get("rent_enddate"))) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("止租日期不能为空");
          return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("measure"))&& tenancyMap.get("measure").toString().length()>10) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("面积长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("deposit"))&& tenancyMap.get("deposit").toString().length()>10) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("月租金长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("cash_pledge"))&& tenancyMap.get("cash_pledge").toString().length()>10) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("押金长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("lessor"))&& tenancyMap.get("lessor").toString().length()>30) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("出租方长度过长");
          return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("contact"))&& tenancyMap.get("contact").toString().length()>50) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("联系方式长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("agency_fee"))&& tenancyMap.get("agency_fee").toString().length()>10) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("中介费长度过长");
          return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("payment_mode"))&& tenancyMap.get("payment_mode").toString().length()>20) {
      	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
            baseResponse.setMessage("支付方式长度过长");
            return baseResponse;
		}
      
      if (StringUtil.isNotNull(tenancyMap.get("remark"))&& tenancyMap.get("remark").toString().length()>300) {
    	  baseResponse.setCode(ConstantUtil.ResponseEnum.FAIL.getCode());
          baseResponse.setMessage("备注长度过长");
          return baseResponse;
		}
		return baseResponse;
	}
    
}

class MapKeyComparator implements Comparator<String> {
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}

class MapValueComparator implements Comparator<Map.Entry<String, String>> {
    public int compare(Map.Entry<String, String> entry_1,
                       Map.Entry<String, String> entry_2) {
        return entry_1.getValue().compareTo(entry_2.getValue());
    }
}
