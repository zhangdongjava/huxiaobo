package com.findu.dto;
// Generated 2016-3-12 23:27:55 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.findu.utils.SysParamUtils;
/**
 * 用户
 */
public class FUUser {

	private long userId = 0;
	private long pid = 0;
	private String partnerId = null;
	
	/**帐号*/
	private String account = null;
	/**手机区号*/
	private String areacode = null;
	/**联系电话*/
	private String phone = null;
	/**姓*/
	private String surname = null;
	/**名字*/
	private String name = null;
	/**昵称*/
	private String nickname = null;
	/**头像url绝对地址*/
	private String thumbnail = null;
	/**第几代*/
	private String generation = null;
	/**性别 1男，2女*/
	private Integer sexy = null;
	/**用户等级 1普通用户，2vip用户*/
	private Integer rank = null;
	/**用户签名*/
	private String sign = null;
	/**用户地址*/
	private String address = null;
	private String qq = null;
	private String weixin = null;
	private Date lastLoginTime = null;
	private Date createTime = null;

	public FUUser() {
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	/** 缺省第三方帐号id*/
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}
	
	/**缺省第三方帐号*/
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**手机区号*/
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		if(thumbnail == null)
			this.thumbnail = null;
		else if(thumbnail.indexOf("http://") == 0)
			this.thumbnail = thumbnail;
		else
			this.thumbnail = SysParamUtils.THUMNAIL_BASE_URL + thumbnail;
	}

	public String getGeneration() {
		return this.generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public Integer getSexy() {
		return this.sexy;
	}

	public void setSexy(Integer sexy) {
		this.sexy = sexy;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeixin() {
		return this.weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
