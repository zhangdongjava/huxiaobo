package com.findu.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.findu.model.TBill;

@Repository
public class TBillDao extends TBaseDao<TBill>{
	
	private static Logger logger = Logger.getLogger(TBillDao.class);
	
	public TBillDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TBill))
			throw new Exception(String.format("TBillDao entity %s is invalid", entity.getClass().getName()));
		TBill bill = (TBill)entity;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bill.getCreateTime());
		return String.format("%s_%02d", getEntityTable(entity),calendar.get(Calendar.MONTH) % 12);
	}
	
	/**
	 * 根据条件查询bill信息
	 * @param bill
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TBill> list(TBill bill, Date startTime, Date endTime) {
		Session session = getSession(bill);
		ThreadContext ctx = ThreadContext.getThreadContext();
		ctx.dao = null;
		
		String sql = "";
		Calendar start = Calendar.getInstance();
		start.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		int months = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 1;
		int monthIdx = start.get(Calendar.MONTH);
		for(int i = 0; i < months; i++){
			if(sql.length() > 0)
				sql += " union ";
			sql += String.format("select * from t_bill_%02d where (srcUserId = :user or dstUserId = :user) and createTime >= :start and createTime <= :end"
					, monthIdx);
			monthIdx = (monthIdx + 1) % 12;
		}
		ArrayList<TBill> data = null;
		try {
			SQLQuery query = session.createSQLQuery(sql).addEntity(TBill.class);
			query.setLong("user", bill.getSrcUserId());
			query.setTimestamp("start", startTime);
			query.setTimestamp("end", endTime);
			data = (ArrayList<TBill>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				super.restSession();
			}
			logger.error(e.getMessage());
			return null;
		}
		session.flush();
		return data;
	}
	
}
