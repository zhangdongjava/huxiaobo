package com.findu.model;
// Generated 2016-3-15 17:21:54 by Hibernate Tools 4.0.0

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
 * 家庭成员权限表
 * 分表关键字是memberId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_member_right")
public class TMemberRight implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6030031622435199747L;
	private long id = 0;
	/**成员id*/
	private long memberId = 0;
	/**权限类型1普通成员，2管理员，3族长*/
	private int rightType = 0;
	private Date createTime = null;

	public TMemberRight() {
	}

	public TMemberRight(long id, long memberId, int rightType, Date createTime) {
		this.id = id;
		this.memberId = memberId;
		this.rightType = rightType;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_member_right",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "memberId", nullable = false)
	public long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "rightType", nullable = false)
	public int getRightType() {
		return this.rightType;
	}

	public void setRightType(int rightType) {
		this.rightType = rightType;
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
