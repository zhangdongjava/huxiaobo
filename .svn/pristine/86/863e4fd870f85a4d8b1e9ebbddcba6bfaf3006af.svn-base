package com.findu.dao;

import org.hibernate.Session;

class ThreadContext {
	private static final ThreadLocal<ThreadContext> local = new ThreadLocal<>();
	
	Session session = null;
	
	TBaseDao<?> dao = null;
	
	Object entity = null;
	
	public static ThreadContext getThreadContext(){
		ThreadContext context = local.get();
		if(context == null){
			context = new ThreadContext();
			local.set(context);
		}
		return context;
	}
}
