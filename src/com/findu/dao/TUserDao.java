package com.findu.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.findu.model.TUser;

@Repository
public class TUserDao extends TBaseDao<TUser>{
	private static Logger logger = Logger.getLogger(TUserDao.class);

	public TUserDao() {
		super();
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		return null;
	}
	
	@Override
	public boolean update(Session session, TUser entity) {
		// TODO Auto-generated method stub
		TUser user = new TUser();
		user.setUserId(entity.getUserId());
		user = get(session, user, user.getUserId());
		if(entity.getPassword() == null)
			entity.setPassword(user.getPassword());
		if(entity.getPayPassword() == null)
			entity.setPayPassword(user.getPayPassword());
		entity.setLastLoginTime(user.getLastLoginTime());
		return super.update(session, entity);
	}
}
