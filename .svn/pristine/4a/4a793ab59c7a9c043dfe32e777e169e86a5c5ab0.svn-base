package com.findu.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.findu.model.TFamily;
import com.findu.model.TGenealogy;

@Repository
public class TFamilyDao extends TBaseDao<TFamily>{
	
	private static Logger logger = Logger.getLogger(TFamilyDao.class);

	public TFamilyDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TFamily))
			throw new Exception(String.format("TFamilyDao entity %s is invalid", entity.getClass().getName()));
		TFamily family = (TFamily)entity;
		return String.format("%s_%02d", getEntityTable(entity),family.getGenealogyId() % 100);
	}

	public int getFamilyCount(TGenealogy genealogy){
		int res;
		try {
			TFamily family = new TFamily();
			family.setGenealogyId(genealogy.getGenealogyId());
			Session session = getSession(family);
			String hsql = String.format("select count(1) from %s t where t.genealogyId = %d"
					, getEntityTable(family), genealogy.getGenealogyId());
			Query query = session.createSQLQuery(hsql);
			ArrayList<Object> data = (ArrayList<Object>) query.list();
			res = Integer.parseInt(data.get(0).toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException){
				super.restSession();
			}
			logger.error(e.getMessage());
			return -1;
		}
		return res;
	}
	
}
