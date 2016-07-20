package com.findu.utils;

import com.findu.push.client.FUPushClient;

import net.sf.json.JSONObject;

public class FUPushUtils {
	
	public static final String NAME = "";
	
	public static boolean pushToOne(long userId){
		
		
		String message = "您收到一条好友申请！";
		FUPushClient.pushNotificationToSingle(message, null, String.valueOf(userId));
		
//		String url = String.format("http://210.14.149.118:8085/fu-pushserver/server/push/toSingle.action");
//		String r = HttpRequest.sendPost(url, String.format("message=%s&alias=%d",message,userId));
//		System.out.println("TUISONG:"+r);
//		if (r==null) {
//			return false;
//		}
//		JSONObject objc = JSONObject.fromObject(r);
//		if(objc.getInt("code")!=0){
//			return false;
//		}
		return true;
	}
	
	public static void main(String[] args) {
		pushToOne(2);
	}
	
}
