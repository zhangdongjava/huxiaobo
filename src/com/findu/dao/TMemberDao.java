package com.findu.dao;

import org.springframework.stereotype.Repository;

import com.findu.model.TMember;

@Repository
public class TMemberDao extends TBaseDao<TMember>{
	
	public TMemberDao() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public TMember getMember(long memberId,long creatorId){
//		TMember member = new TMember();
//		member.setMemberId(memberId);
//		member.setCreatorId(creatorId);
//		member = find(member, true);
//		return member;
//	}
	
	@Override
	public String getTargetTableName(Object entity) throws Exception {
		// TODO Auto-generated method stub
		if(!(entity instanceof TMember))
			throw new Exception(String.format("TMemberDao entity %s is invalid", entity.getClass().getName()));
		TMember member = (TMember)entity;
		return String.format("%s_%02d", getEntityTable(entity), member.getCreatorId() % 100);
	}
}
