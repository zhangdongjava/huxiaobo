package com.findu.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 数据工具类
 * 
 * @author ll
 *
 */
public class DataUtils {
	private static Logger logger = Logger.getLogger(DataUtils.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	static{
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.DEFAULT_PATTERN));
	}
	/**
	 * MD5 16位加密
	 * 
	 * @param string
	 * @return
	 */
	public static String MD5(String string) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString().substring(8, 24);
//			System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
//			System.out.println("md5 32bit: " + buf.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}

	public static String UTF8String(String string) {
		if (string == null) {
			string = "";
		}
		try {
			string = new String(string.getBytes("ISO8859_1"), "UTF-8");
			return string;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return string;
	}

	public static boolean isMobileNumber(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	/**
	 * 判断字符是否含有中文
	 * @param s
	 * @return
	 */
	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}
	
	
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
	    // DES算法要求有一个可信任的随机数源
	    SecureRandom sr = new SecureRandom();

	    // 从原始密匙数据创建DESKeySpec对象
	    DESKeySpec dks = new DESKeySpec(key);

	    // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    SecretKey securekey = keyFactory.generateSecret(dks);

	    // Cipher对象实际完成加密操作
	    Cipher cipher = Cipher.getInstance("DES");

	    // 用密匙初始化Cipher对象
	    cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

	    // 执行加密操作
	    return cipher.doFinal(src);
	}

	public final static String encrypt(String password, String key) {

	    try {
	        return byte2String(encrypt(password.getBytes(), key.getBytes()));
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error(e.getMessage());
	    }
	    return null;
	}
	
	public final static boolean CopyBean(Object src, Object dst){
		try {
			PropertyUtils.copyProperties(dst, src);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static <T> T Json2Object(String json, Class<T> cls){
		try {
			T obj = (T) objectMapper.readValue(json, cls);
			logger.info("json -> java bean: " + obj);
			Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
			for (ConstraintViolation<T> constraintViolation : constraintViolations) { 
				logger.warn(String.format("bean 校验失败，对象属性[%s], 错误信息[%s], 错误值[%s]"
						, constraintViolation.getPropertyPath(), constraintViolation.getMessage()
						, constraintViolation.getInvalidValue().toString()));
		    }
			if(constraintViolations.size() > 0)
				return null;
			else
				return obj;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private static String byte2String(byte[] b) {
		BigInteger bi = new BigInteger(1, b);
		return bi.toString(16).toLowerCase();
	}
}
