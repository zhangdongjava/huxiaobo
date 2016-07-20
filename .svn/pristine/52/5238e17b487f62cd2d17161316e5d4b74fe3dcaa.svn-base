package com.findu.dto;

import java.util.LinkedList;

import com.findu.utils.SysParamUtils;

/**
 * 成员
 * @author ll
 *
 */
public class FUMember {
	
	private long memberId = 0;
	/**成员添加人*/
	private long creatorId = 0;
	/**
	 *	用户id
	 */
	private Long userId = null;
	/**姓*/
	private String surname = null;
	/**名称*/
	private String name = null;
	/**第几代*/
	private String generation = null;
	/**性别*/
	private Integer sexy = null;
	/** 成员排行 */
	private Integer seniority = null;
	/**头像*/
	private String thumbnail = null;
	/**自动划分辈份*/
	private Integer tier = null;
	
	private LinkedList<FUFather> asFather = null;
	private LinkedList<FUMother> asMother = null;
	private LinkedList<FUSon> asSon = null;
	private LinkedList<FUDaughter> asDaughter = null;
	private LinkedList<Long> siblingIds = null;
	
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	/**成员添加人*/
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGeneration() {
		return generation;
	}
	public void setGeneration(String generation) {
		this.generation = generation;
	}
	public Integer getSexy() {
		return sexy;
	}
	public void setSexy(Integer sexy) {
		this.sexy = sexy;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	/** 成员排行 */
	public Integer getSeniority() {
		return seniority;
	}
	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}
	public void setThumbnail(String thumbnail) {
		if(thumbnail == null)
			this.thumbnail = null;
		else if(thumbnail.indexOf("http://") == 0)
			this.thumbnail = thumbnail;
		else
			this.thumbnail = SysParamUtils.THUMNAIL_BASE_URL + thumbnail;
	}
	public Integer getTier() {
		return tier;
	}
	public void setTier(Integer tier) {
		this.tier = tier;
	}
	public LinkedList<FUFather> getAsFather() {
		return asFather;
	}
	public void setAsFather(LinkedList<FUFather> asFather) {
		this.asFather = asFather;
	}
	public LinkedList<FUMother> getAsMother() {
		return asMother;
	}
	public void setAsMother(LinkedList<FUMother> asMother) {
		this.asMother = asMother;
	}
	public LinkedList<FUSon> getAsSon() {
		return asSon;
	}
	public void setAsSon(LinkedList<FUSon> asSon) {
		this.asSon = asSon;
	}
	public LinkedList<FUDaughter> getAsDaughter() {
		return asDaughter;
	}
	public void setAsDaughter(LinkedList<FUDaughter> asDaughter) {
		this.asDaughter = asDaughter;
	}
	public LinkedList<Long> getSiblingIds() {
		return siblingIds;
	}
	public void setSiblingIds(LinkedList<Long> siblingIds) {
		this.siblingIds = siblingIds;
	}

}
