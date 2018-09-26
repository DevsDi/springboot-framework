package com.dev.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class DateUtil {

    //将Date类型转换成字符串格式
    public static String dateToString(Date d, String format){
        return new SimpleDateFormat(format).format(d);
    }

    //将字符串类型转换成日期格式
    public static Date stringToDate(String s,String format) throws ParseException {
        return new SimpleDateFormat(format).parse(s);
    }
    
    public static Long getDaysBetween(Date startDate, Date endDate) {  
        Calendar fromCalendar = Calendar.getInstance();  
        fromCalendar.setTime(startDate);  
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        fromCalendar.set(Calendar.MINUTE, 0);  
        fromCalendar.set(Calendar.SECOND, 0);  
        fromCalendar.set(Calendar.MILLISECOND, 0);  
  
        Calendar toCalendar = Calendar.getInstance();  
        toCalendar.setTime(endDate);  
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        toCalendar.set(Calendar.MINUTE, 0);  
        toCalendar.set(Calendar.SECOND, 0);  
        toCalendar.set(Calendar.MILLISECOND, 0);  
  
        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);  
    } 
    
//    public static void main(String[] args) {
//		try {
//	    	Date rent_enddate = DateUtil.stringToDate("2018-07-16", "yyyy-MM-dd");
//			System.out.println(getDaysBetween(new Date(),rent_enddate));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//    }
}
