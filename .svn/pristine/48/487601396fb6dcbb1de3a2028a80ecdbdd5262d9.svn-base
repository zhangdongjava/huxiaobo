package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TGenealogyMap;

@Repository
public class TGenealogyMapDao extends TBaseDao<TGenealogyMap>{

	public TGenealogyMapDao() {
		super();
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		if(!(entity instanceof TGenealogyMap))
			throw new Exception(String.format("TGenealogyMapDao entity %s is invalid", entity.getClass().getName()));
		TGenealogyMap genealogyMap = (TGenealogyMap)entity;
		return String.format("%s_%02d", getEntityTable(entity), genealogyMap.getId().getUserId() % 10);
	}

}
