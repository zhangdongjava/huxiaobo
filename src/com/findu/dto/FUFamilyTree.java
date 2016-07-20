package com.findu.dto;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * 家庭
 * @author ll
 *
 */
public class FUFamilyTree {
	/**家谱id*/
	private long id = 0;
	/**创建人*/
	private long creatorId = 0;
	/**家庭名称*/
	private String title = null;
	/**姓*/
	private String surname = null;	
	/**家庭节点*/
	private long centerId = 0;
	/**按辈份存放*/
	private ArrayList<ArrayList<Long>> tiers = null;
	/**成员列表*/
	private HashMap<Long, FUMember> members = null;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public long getCenterId() {
		return centerId;
	}
	public void setCenterId(long centerId) {
		this.centerId = centerId;
	}
	public ArrayList<ArrayList<Long>> getTiers() {
		return tiers;
	}
	public void setTiers(ArrayList<ArrayList<Long>> tiers) {
		this.tiers = tiers;
	}
	public HashMap<Long, FUMember> getMembers() {
		return members;
	}
	public void setMembers(HashMap<Long, FUMember> members) {
		this.members = members;
	}
}
