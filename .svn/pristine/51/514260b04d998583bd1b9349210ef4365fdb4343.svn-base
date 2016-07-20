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
 * 账单表
 * 分表关键字是createTime
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_bill")
public class TBill implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6909240818154508872L;
	private long billId = 0;
	/**发起的用户id*/
	private long srcUserId = 0;
	/**发起的用户的资产id*/
	private Long srcWealthId = null;
	/**目的用户id*/
	private long dstUserId = 0;
	/**目的用户的资产id*/
	private Long dstWealthId = null;
	/**资产类型 1 充值，2 赠送，3 发红包，4 提现*/
	private int type = 0;
	/**资产值*/
	private Long value = null;
	private String description = null;
	private Date createTime = null;

	public TBill() {
	}

	public TBill(long id, long srcUserId, long dstUserId, int type, Date createTime) {
		this.billId = id;
		this.srcUserId = srcUserId;
		this.dstUserId = dstUserId;
		this.type = type;
		this.createTime = createTime;
	}

	public TBill(long id, long srcUserId, Long srcWealthId, long dstUserId, Long dstWealthId, int type, Long value,
			String description, Date createTime) {
		this.billId = id;
		this.srcUserId = srcUserId;
		this.srcWealthId = srcWealthId;
		this.dstUserId = dstUserId;
		this.dstWealthId = dstWealthId;
		this.type = type;
		this.value = value;
		this.description = description;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_bill",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getBillId() {
		return billId;
	}

	public void setBillId(long billId) {
		this.billId = billId;
	}

	@Column(name = "srcUserId", nullable = false)
	public long getSrcUserId() {
		return this.srcUserId;
	}

	public void setSrcUserId(long srcUserId) {
		this.srcUserId = srcUserId;
	}

	@Column(name = "srcWealthId")
	public Long getSrcWealthId() {
		return this.srcWealthId;
	}

	public void setSrcWealthId(Long srcWealthId) {
		this.srcWealthId = srcWealthId;
	}

	@Column(name = "dstUserId", nullable = false)
	public long getDstUserId() {
		return this.dstUserId;
	}

	public void setDstUserId(long dstUserId) {
		this.dstUserId = dstUserId;
	}

	@Column(name = "dstWealthId")
	public Long getDstWealthId() {
		return this.dstWealthId;
	}

	public void setDstWealthId(Long dstWealthId) {
		this.dstWealthId = dstWealthId;
	}

	/**
	 * 返回支付类型。
	 * @return  1 充值，2 赠送，3 发红包，4 提现
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "value")
	public Long getValue() {
		return this.value;
	}

	public void setValue(Long value) {
		this.value = value;
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
