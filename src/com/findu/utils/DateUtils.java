package com.findu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期util
 * @author ll
 *
 */
public class DateUtils {

	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss (Z)";
	
	/**初始化日期格式**/
	public static final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
	
	public static final int SecondsOfDay = 86400;
	
	/**
	 * 将字符串转换为Date类型的日期
	 * @param format
	 * @param timestemp
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStrDate(String format, String timestamp) throws ParseException{
		if(format != null)
			sdf.applyPattern(format);
		else
			sdf.applyPattern(DEFAULT_PATTERN);
		return sdf.parse(timestamp);
	}
	
	/**
	 * 将Date 转换为指定格式的日期
	 * @param format
	 * @param dt
	 * @return
	 */
	public static String formatDate(String format, Date dt){
		if(format != null)
			sdf.applyPattern(format);
		else
			sdf.applyPattern(DEFAULT_PATTERN);
		return sdf.format(dt);
	}
	
	public static long getIntervalSeconds(Date d1, Date d2){
		return (d1.getTime() - d2.getTime()) / 1000;
	}
}
