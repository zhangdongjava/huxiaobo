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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户资产表
 * 分表关键字是userId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_wealth", uniqueConstraints = @UniqueConstraint(columnNames = { "userId",
		"type", "identity" }) )
public class TWealth implements java.io.Serializable {

	public static final int TYPE_MONEY = 1;
	public static final int TYPE_CVM = 2;
	public static final int TYPE_PVM = 3;
	public static final int TYPE_POINT = 4;
	public static final int TYPE_REDBAG = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3331718539594069228L;
	
	private long id = 0;
	private long userId = 0;
	/**资产类型1 零钱，2 现金蒲币，3 赠送蒲币，4 蒲籽，5 红包*/
	private int type = 0;
	/**资产标志*/
	private String identity = null;
	/**资产值*/
	private long value = 0;
	/**有效时间*/
	private Date validateTime = null;
	private String description = null;
	private Date createTime = null;

	public TWealth() {
	}

	public TWealth(long id, long userId, int type, long value, Date createTime) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.value = value;
		this.createTime = createTime;
	}

	public TWealth(long id, long userId, int type, long value, Date validateTime, String description,
			Date createTime) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.value = value;
		this.validateTime = validateTime;
		this.description = description;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_wealth",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "userId", nullable = false)
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 返回资产类型。
	 * @return  1 零钱，2 现金蒲币，3 赠送蒲币，4 蒲籽，5 红包
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Column(name = "identity", length = 30)
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Column(name = "value")
	public long getValue() {
		return this.value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validateTime", length = 19)
	public Date getValidateTime() {
		return this.validateTime;
	}

	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return this.description;
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
