package com.findu.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findu.dao.TUserDao;
import com.findu.model.TUser;
import com.findu.utils.DataUtils;
import com.findu.utils.DateUtils;
import com.findu.utils.SysParamUtils;

@Service
public class AuthService {
	private static Logger logger = Logger.getLogger(AuthService.class);
	
	@Autowired
	private TUserDao userDao;
	
	/**
	 * 检查token是否有效
	 * @param timestamp
	 * @param auth
	 * @return
	 */
	public boolean verifyToken(String timestamp, String auth) {
		if(timestamp == null || timestamp.trim().isEmpty())
			return false;
		
		if(auth == null || auth.trim().isEmpty())
			return false;
		
		try {
			logger.info(String.format("request auth, timestamp %s, auth %s", timestamp, auth));
			Date date = DateUtils.parseStrDate(null, timestamp);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			logger.info(String.format("timestamp %s, current %s"
					, date.toString(), calendar.getTime().toString()));
			long thisTime = calendar.getTimeInMillis();
			calendar.setTime(date);
			calendar.add(Calendar.SECOND, Integer.parseInt(SysParamUtils.TOKEN_VALID_TIME));
			if(thisTime > calendar.getTimeInMillis()){
				logger.warn(String.format("current time ms %s is bigger than timestampe ms %s, timestamp is invalid"
						, thisTime, calendar.getTimeInMillis()));
				return false;
			}
			
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return false;
		}
		
		String sysToken = DataUtils.MD5(timestamp + SysParamUtils.TOKEN_KEY);
		if(!sysToken.equalsIgnoreCase(auth)){
			logger.warn("auth is invalid");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 检查支付模块二次健全的code是否有效
	 * @param userId
	 * @param once
	 * @param code
	 * @param timestamp
	 * @return
	 */
	public boolean verifyCode(String userId, String once, String code, String timestamp){
		// to do: 校验once有效性
		
		TUser user = new TUser();
		user.setUserId(Long.parseLong(userId));
		user = userDao.get(user, user.getUserId());

		if(user == null){
			logger.warn(String.format("user %s is invalid", userId));
			return false;
		}
		
		//进行des加密算法
		String password = user.getPayPassword();
		if(password == null || password.length() <= 0)
			password = user.getPassword();
		String encryptString= DataUtils.encrypt(password, SysParamUtils.DES_KEY_WORD);
		String myCode = DataUtils.MD5(once + encryptString + timestamp);
		if(!code.equals(myCode)){
			logger.warn(String.format("bill auth error: code [%s], request code [%s]"
					, code, myCode));
			return false;
		}
		
		return true;
	}
}
