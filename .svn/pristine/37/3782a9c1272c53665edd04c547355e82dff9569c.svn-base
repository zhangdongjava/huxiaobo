package com.findu.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.findu.dto.FUSearchPage;
import com.findu.model.TFriendApply;
import com.findu.utils.SysParamUtils;

@SuppressWarnings("unchecked")
@Repository
public class TFriendApplyDao extends TBaseDao<TFriendApply>{

	private static Logger logger = Logger.getLogger(TFriendApplyDao.class);

	public TFriendApplyDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TFriendApply))
			throw new Exception(String.format("TFriendApplyDao entity %s is invalid", entity.getClass().getName()));
		TFriendApply apply = (TFriendApply)entity;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(apply.getCreateTime());
		return String.format("%s_%02d", getEntityTable(entity),calendar.get(Calendar.MONTH) % 12);
	}
	public TFriendApply find(long userId,long friendId){
		TFriendApply fapply = new TFriendApply();
		Session session = getSession(fapply);
		ThreadContext ctx = ThreadContext.getThreadContext();
		ctx.dao = null;

		String sql = "";
		for(int i = 0; i < 12; i++){
			if(sql.length() > 0)
				sql += " union ";
			sql += String.format("select * from t_friend_apply_%02d where userId = :user1 and friendId = :user2"
					, i);
					
		}
		try {
			SQLQuery query = session.createSQLQuery(sql).addEntity(TFriendApply.class);
			query.setLong("user1", userId);
			query.setLong("user2", friendId);

			query.setCacheable(true);
						
			List<TFriendApply> list = (List<TFriendApply>) query.list();
			if (list!=null && list.size()>0) {
				fapply = list.get(0);
			}else{
				fapply = null;
			}
			


		} catch (Exception e) {
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				super.restSession();
			}
			logger.error(e.getMessage());
			return null;
		}
		session.flush();
		return fapply;
	}
	
	/**
	 * 
	 * @param fapply
	 * @param state
	 * @param orderFields
	 * @param desc
	 * @param isSelf
	 * @param page
	 * @return
	 */
	public ArrayList<TFriendApply> list(TFriendApply fapply,int state,ArrayList<String> orderFields,boolean desc,boolean isSelf, FUSearchPage page) {

		Session session = getSession(fapply);

		ThreadContext ctx = ThreadContext.getThreadContext();

		ctx.dao = null;

		String sql = "";
		for(int i = 0; i < 12; i++){
			if(sql.length() > 0)
				sql += " union ";
			if (isSelf) {

				if (state!=-1) {
					sql += String.format("select * from t_friend_apply_%02d where userId = :user and state = %d"
							, i,state);
				}else{
					sql += String.format("select * from t_friend_apply_%02d where userId = :user"
							,i);
				}

			}else{
				if (state!=-1) {
					sql += String.format("select * from t_friend_apply_%02d where friendId = :user and state = %d"
							, i,state);
				}else{
					sql += String.format("select * from t_friend_apply_%02d where friendId = :user"
							, i);
				}

			}
		}
		if(orderFields != null && orderFields.size() > 0){
			String orderBySql = "";
			for(String field : orderFields){
				if(orderBySql.trim().length() > 0)
					orderBySql += ",";
				orderBySql += field;
			}
			sql += String.format(" order by %s %s", orderBySql, desc ? " desc" : "asc");
		}
		logger.info(sql);

		ArrayList<TFriendApply> data = null;
		try {
			SQLQuery query = session.createSQLQuery(sql).addEntity(TFriendApply.class);
			query.setLong("user", fapply.getUserId());

			query.setCacheable(true);
			if(page != null && page.getPageCount() > 0 && page.getPageNumber() >= 0 && page.getPageSize() > 0){
				int pageNum = page.getPageNumber() >= page.getPageCount() ? page.getPageCount() - 1 : page.getPageNumber();
				int pageSize = Integer.parseInt(SysParamUtils.PAGE_MAX_SIZE);
				if(pageSize > page.getPageSize())
					pageSize = page.getPageSize(); 
				query.setFirstResult(pageNum * pageSize);
				query.setMaxResults(pageSize);
			}			
			data = (ArrayList<TFriendApply>) query.list();


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
