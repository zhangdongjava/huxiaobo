package com.findu.utils;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class NetUtils {
	private static Logger logger = Logger.getLogger(NetUtils.class);
	
	public static String getHttpRequestBody(HttpServletRequest request){
		String result = null;
		 try {
			request.setCharacterEncoding("UTF-8");
			int contentLen = request.getContentLength();
			if(contentLen > 0){
				InputStream is = request.getInputStream();
				byte[] message = new byte[contentLen];
				int readLen = 0;
				int readLengthThisTime = 0;
				while (readLen != contentLen) {
					 readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					 if (readLengthThisTime == -1) {// Should not happen.
                         break;
					 }
					 readLen += readLengthThisTime;
				}
				if(readLen >= contentLen)
					result = new String(message, "UTF-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return null;
		}
		logger.info("request 信息：" + result); 
		return result;
	}
}
