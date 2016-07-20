package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TWealth;

@Repository
public class TWealthDao extends TBaseDao<TWealth>{

	public TWealthDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TWealth))
			throw new Exception(String.format("TWealthDao entity %s is invalid", entity.getClass().getName()));
		TWealth  wealth = (TWealth)entity;
		return String.format("%s_%02d", getEntityTable(entity),wealth.getUserId() % 10);
	}

	
}
