package com.findu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;


/**
 * 定义获取系统参数的属性
 * @author ll
 *
 */
public class SysParamUtils {

	/**用于获取计算token的密钥**/
	public static String TOKEN_KEY = "TOKEN_KEY";
	/**用于获取token的有效时间**/
	public static String TOKEN_VALID_TIME = "TOKEN_VALID_TIME";
	/**私有家谱家庭数量限制*/
	public static String PRIVATE_G_F_N = "PRIVATE_G_F_N";
	/**共有家谱家庭数量限制*/
	public static String PUBLIC_G_F_N = "PUBLIC_G_F_N";
	/**DES加密秘钥**/
	public static String DES_KEY_WORD = "DES_KEY_WORD";
	/**共有家谱向上查询最大级数*/
	public static String PUBLIC_G_UP_MAX = "PUBLIC_G_UP_MAX";
	/**共有家谱向下查询最大级数*/
	public static String PUBLIC_G_DOWN_MAX = "PUBLIC_G_DOWN_MAX";
	/**缩略图URL基地址*/
	public static String THUMNAIL_BASE_URL = "THUMNAIL_BASE_URL";
	/**按时间范围查询的最大天数*/
	public static String QUERY_DAYS_MAX = "QUERY_DAYS_MAX";
	/**分页查询时一页数据的最大条数*/
	public static String PAGE_MAX_SIZE = "PAGE_MAX_SIZE";
	
	private static Map<String, String> paramsMap = new HashMap<String,String>();
	
	public static void initSystemParams(ApplicationContext context){
		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		String sql = "select name,value from t_system_param";
		Query query = session.createSQLQuery(sql);
		ArrayList<Object> data = (ArrayList<Object>) query.list();
		session.flush();
		for (Iterator iterator = data.iterator();iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next(); 
			paramsMap.put(objects[0].toString().toUpperCase(),(String)objects[1]);
		}
		getSystemParams();
		
	}
	
	public static void getSystemParams(){
		TOKEN_KEY = paramsMap.get(TOKEN_KEY);
		TOKEN_VALID_TIME = paramsMap.get(TOKEN_VALID_TIME);
		PRIVATE_G_F_N = paramsMap.get(PRIVATE_G_F_N);
		PUBLIC_G_F_N = paramsMap.get(PUBLIC_G_F_N);
		DES_KEY_WORD = paramsMap.get("DES_KEY_WORD");
		PUBLIC_G_UP_MAX = paramsMap.get(PUBLIC_G_UP_MAX);
		PUBLIC_G_DOWN_MAX = paramsMap.get(PUBLIC_G_DOWN_MAX);
		THUMNAIL_BASE_URL = paramsMap.get(THUMNAIL_BASE_URL);
		QUERY_DAYS_MAX = paramsMap.get(QUERY_DAYS_MAX);
		PAGE_MAX_SIZE = paramsMap.get(PAGE_MAX_SIZE);
		paramsMap.clear();
	}
}
