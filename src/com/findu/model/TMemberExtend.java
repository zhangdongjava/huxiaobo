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
 * 家庭成员扩展信息表
 * 分表关键字是memberId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_member_extend")
public class TMemberExtend implements java.io.Serializable {

	public static final String NAME_PHONE = "phone";
	public static final String NAME_ADDRESS = "address";
	public static final String NAME_WEIXIN = "weixin";
	public static final String NAME_QQ = "qq";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -796594032899430779L;
	private long id = 0;
	/**成员id*/
	private long memberId = 0;
	/**用于分段，防止信息过长*/
	private Long groupId = null;
	/**分段位置*/
	private Integer groupPosition = null;
	/**信息名称*/
	private String name = null;
	/**信息值*/
	private String value = null;
	private Date createTime = null;

	public TMemberExtend() {
	}

	public TMemberExtend(long id, long memberId, Date createTime) {
		this.id = id;
		this.memberId = memberId;
		this.createTime = createTime;
	}

	public TMemberExtend(long id, long memberId, Long groupId, Integer groupPosition, String name, String value,
			Date createTime) {
		this.id = id;
		this.memberId = memberId;
		this.groupId = groupId;
		this.groupPosition = groupPosition;
		this.name = name;
		this.value = value;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_member_extend",
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

	@Column(name = "groupId")
	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "groupPosition")
	public Integer getGroupPosition() {
		return this.groupPosition;
	}

	public void setGroupPosition(Integer groupPosition) {
		this.groupPosition = groupPosition;
	}

	@Column(name = "name", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", length = 100)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 19, updatable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
