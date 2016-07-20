package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TMemberExtend;

@Repository
public class TMemberExtendDao extends TBaseDao<TMemberExtend>{
	
	public TMemberExtendDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TMemberExtend))
			throw new Exception(String.format("TMemberExtend entity %s is invalid", entity.getClass().getName()));
		TMemberExtend memberExt = (TMemberExtend)entity;
		return String.format("%s_%02d", getEntityTable(entity), memberExt.getMemberId() % 10);
	}
}
