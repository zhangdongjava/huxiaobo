package com.findu.dao;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.findu.model.TSubject;

@Repository
public class TSubjectDao extends TBaseDao<TSubject>{

	public TSubjectDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TSubject))
			throw new Exception(String.format("TSubjectDao entity %s is invalid", entity.getClass().getName()));
		TSubject  subject = (TSubject)entity;
		return String.format("%s_%02d", getEntityTable(entity),subject.getRefId() % 100);
	}

	/**
	 * 查询主贴
	 * @param subject
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public ArrayList<TSubject> list(TSubject subject, Date startTime, Date endTime) {
		Session session = getSession(subject);
		String hql = "from TSubject t where referenceId = ? and t.createTime >= ? and t.createTime <= ?";
		Query query = session.createQuery(hql);
		query.setLong(0,subject.getRefId());
		query.setDate(1, startTime);
		query.setDate(2, endTime);
		ArrayList<TSubject> data = (ArrayList<TSubject>) query.list();
		session.flush();
		return data;
	}
}
