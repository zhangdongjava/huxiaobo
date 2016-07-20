package com.findu.model;
// Generated 2016-3-12 23:27:55 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 第三方登录帐号表
 * 分表关键字是userId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_partner")
public class TPartner implements java.io.Serializable {

	public static final int TYPE_DEFAULT = 1;
	
	public static final int TYPE_PUSH = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -444954497131970505L;
	private long fuId = 0;
	/**用户id*/
	private long userId = 0;
	/**第三方登陆帐号*/
	private String partnerId = null;
	/**第三方登陆口令*/
	private String partnerPass = null;
	/**第三方类型*/
	private int type = 0;
	/**描述信息*/
	private String description = null;
	private Date createTime = null;


	public TPartner() {
	}

	public TPartner(long id, long userId, String partnerId, int type, Date createTime) {
		this.fuId = id;
		this.userId = userId;
		this.partnerId = partnerId;
		this.type = type;
		this.createTime = createTime;
	}

	public TPartner(long id, long userId, String partnerId, String partnerPass, Integer type, String description, Date createTime) {
		this.fuId = id;
		this.userId = userId;
		this.partnerId = partnerId;
		this.partnerPass = partnerPass;
		this.type = type;
		this.description = description;
		this.createTime = createTime;
	}

	/**序列号*/
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_partner",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getFuId() {
		return fuId;
	}

	public void setFuId(long fuId) {
		this.fuId = fuId;
	}
	

	@Column(name = "userId", nullable = false)
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "partnerId", nullable = false, length = 50)
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@Column(name = "partnerPass", length = 50)
	public String getPartnerPass() {
		return partnerPass;
	}

	public void setPartnerPass(String partnerPass) {
		this.partnerPass = partnerPass;
	}
	
	@Column(name = "type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
