package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TFriend;

@Repository
public class TFriendDao extends TBaseDao<TFriend>{

	public TFriendDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TFriend))
			throw new Exception(String.format("TFriendDao entity %s is invalid", entity.getClass().getName()));
		TFriend friend = (TFriend)entity;
		return String.format("%s_%02d", getEntityTable(entity),friend.getUserId()  % 100);
	}

}
