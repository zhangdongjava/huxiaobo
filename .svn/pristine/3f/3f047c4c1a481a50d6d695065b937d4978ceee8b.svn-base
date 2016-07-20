package com.findu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

public class ErrorCodeUtil {

	/**成功*/
	public static final int ERROR_SUCCESS = 200;
	/**参数错误*/
	public static final int ERROR_REQUEST_PARAMETER = 400;
	
	public static final int ERROR_UNAUTHORIZED = 401;
	/**建议修改密码*/
	public static final int ERROR_PASSWORD_FIX= 402;
	/**密码错误*/
	public static final int ERROR_PASSWORD = 403;
	/**未查询到结果*/
	public static final int ERROR_NOT_EXIST = 404;
	/**帐号错误*/
	public static final int ERROR_ACCOUNT = 405;
	/**用户已存在*/
	public static final int ERROR_USER_DUPLICATE = 406;
	/**用户不存在*/
	public static final int ERROR_USER_NULL = 407;
	/**重复操作*/
	public static final int ERROR_REPEAT = 408;
	/**你们已经是好友*/
	public static final int ERROR_FRIEND_DUPLICATE = 409;
	/**你们还不是好友*/
	public static final int ERROR_FRIEND_NULL = 410;
	/**你对该用户已发送过好友申请*/
	public static final int ERROR_FRIEND_APPLY_DUPLICATE = 411;
	
	/**查询的家谱不存在*/
	public static final int ERROR_GENEALOGY_INVALID = 420;
	/**家谱成员满*/
	public static final int ERROR_GENEALOGY_FULL = 421;
	/**需要设置中心成员*/
	public static final int ERROR_NEED_CENTER = 422;
	/**需要设置创建者*/
	public static final int ERROR_NEED_CREATOR = 423;
	
	/**系统错误*/
	public static final int ERROR_SYSTEM = 500;
	
	private static final Map<Integer, String> errorCodeMap = new HashMap<Integer, String>();
	
	public static void initSysErrorCode(ApplicationContext context){
		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		String sql = "select code,reason from t_error_code";
		Query query = session.createSQLQuery(sql);
		ArrayList<Object> data = (ArrayList<Object>) query.list();
		session.flush();
		for (Iterator iterator = data.iterator();iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next(); 
			errorCodeMap.put((Integer)objects[0],(String)objects[1]);
		}
	}
	
	public static String getErrorCodeReason(int code){
		return errorCodeMap.get(code);
	}
	
	public static boolean isSucc(int code){
		if(code >= 200 && code < 300)
			return true;
		else
			return false;
	}
}
