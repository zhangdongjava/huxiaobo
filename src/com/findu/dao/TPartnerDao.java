package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TPartner;

@Repository
public class TPartnerDao extends TBaseDao<TPartner>{

	public TPartnerDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TPartner))
			throw new Exception(String.format("TPartnerDao entity %s is invalid", entity.getClass().getName()));
		TPartner partner = (TPartner)entity;
		return String.format("%s_%02d", getEntityTable(entity), partner.getUserId()  % 10);
	}

}
