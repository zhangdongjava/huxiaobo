package com.findu.dao;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;

public class MultiTableInterceptor extends EmptyInterceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2199737207064368842L;
	
	private static Logger logger = Logger.getLogger(MultiTableInterceptor.class);

	@Override
	public String onPrepareStatement(String sql) {
		// TODO Auto-generated method stub
		ThreadContext tc = ThreadContext.getThreadContext();
		try {
			if(tc.dao != null){
				String tableName = tc.dao.getEntityTable(tc.entity);
				String realTableName = tc.dao.getTargetTableName(tc.entity);
				if(realTableName != null && realTableName.trim().length() > 0){
					return sql.replace(tableName, realTableName);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return sql;
		}
		return sql;
	}

}
