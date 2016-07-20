package com.findu.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.findu.model.TGenealogy;

@Repository
public class TGenealogyDao extends TBaseDao<TGenealogy>{
	private static Logger logger = Logger.getLogger(TGenealogyDao.class);

	public TGenealogyDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TGenealogy))
			throw new Exception(String.format("TGenealogyDao entity %s is invalid", entity.getClass().getName()));
		TGenealogy genealogy = (TGenealogy)entity;
		return String.format("%s_%02d", getEntityTable(entity),genealogy.getCreatorId()  % 10);
	}

}
