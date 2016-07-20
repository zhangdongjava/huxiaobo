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
 * 好友表
 * 分表关键字是userId 
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_friend", uniqueConstraints = @UniqueConstraint(columnNames = { "userId",
		"friendId" }) )
public class TFriend implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4749530992294644042L;
	private long fuId = 0;
	private long userId = 0;
	private long friendId = 0;
	
	/**朋友类型，1好友，2宗亲*/
	private Integer type = null;
	/**昵称*/
	private String nickname = null;
	private Date createTime = null;

	public TFriend() {
	}

	public TFriend(long id, long userId, long friendId, Date createTime) {
		this.fuId = id;
		this.userId = userId;
		this.friendId = friendId;
		this.createTime = createTime;
	}

	public TFriend(long id, long userId, long friendId, Integer type, String nickname, Date createTime) {
		this.fuId = id;
		this.userId = userId;
		this.friendId = friendId;
		this.type = type;
		this.nickname = nickname;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_friend",
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

	@Column(name = "friendId", nullable = false)
	public long getFriendId() {
		return this.friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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
