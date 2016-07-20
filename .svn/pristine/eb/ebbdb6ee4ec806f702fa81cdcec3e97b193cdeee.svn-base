package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TTopic;

@Repository
public class TTopicDao extends TBaseDao<TTopic>{

	public TTopicDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TTopic))
			throw new Exception(String.format("TTopicDao entity %s is invalid", entity.getClass().getName()));
		TTopic topic = (TTopic)entity;
		return String.format("%s_%02d", getEntityTable(entity),topic.getRefId() % 100);
	}

}
