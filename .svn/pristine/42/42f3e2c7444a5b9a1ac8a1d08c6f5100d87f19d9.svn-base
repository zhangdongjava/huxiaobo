package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TPost;

@Repository
public class TPostDao extends TBaseDao<TPost>{
	
	public TPostDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TPost))
			throw new Exception(String.format("TPostDao entity %s is invalid", entity.getClass().getName()));
		TPost  post = (TPost)entity;
		return String.format("%s_%02d", getEntityTable(entity),post.getRefId() % 100);
	}
}
