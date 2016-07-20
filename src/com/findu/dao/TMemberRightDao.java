package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TMemberRight;

@Repository
public class TMemberRightDao extends TBaseDao<TMemberRight>{

	public TMemberRightDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TMemberRight))
			throw new Exception(String.format("TMemberRightDao entity %s is invalid", entity.getClass().getName()));
		TMemberRight memberRight = (TMemberRight)entity;
		return String.format("%s_%02d", getEntityTable(entity), memberRight.getMemberId() % 10);
	}

}
