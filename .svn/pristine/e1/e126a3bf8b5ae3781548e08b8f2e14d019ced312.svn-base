package com.findu.model;

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
 * 好友申请 记录
 * @author FindU
 *
 */
@Entity
@Table(name = "t_friend_apply", uniqueConstraints = @UniqueConstraint(columnNames = { "userId",
		"friendId" }) )
public class TFriendApply implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2541396028168263185L;
	
	private long applyId = 0;
	private long userId = 0;
	private long friendId = 0;
	
	/**申请类型，0 未处理，1 同意 2 同意并加为好友 3 拒绝 4 忽略*/
	private Integer state = null;
	/**朋友类型，1好友，2宗亲*/
	private Integer type = null;
	/**昵称*/
	private String content = null;
	private String reply = null;
	private Date replyTime = null;
	private Date createTime = null;
	
	public TFriendApply() {
		// TODO Auto-generated constructor stub
	}
	public TFriendApply(long id, long userId, long friendId, Date createTime) {
		this.applyId = id;
		this.userId = userId;
		this.friendId = friendId;
		this.createTime = createTime;
	}

	public TFriendApply(long id, long userId, long friendId, Integer type, String content, String reply, Date createTime) {
		this.applyId = id;
		this.userId = userId;
		this.friendId = friendId;
		this.type = type;
		this.content = content;
		this.reply = reply;
		this.createTime = createTime;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_friend_apply",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getApplyId() {
		return applyId;
	}
    public void setApplyId(long applyId) {
		this.applyId = applyId;
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
    
	/**申请类型，0 未处理，1 已同意 2 已拒绝 3 已忽略*/
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
    public void setType(Integer type) {
		this.type = type;
	}
    
    
    @Column(name = "content", length = 50)
    public String getContent() {
		return content;
	}
    public void setContent(String content) {
		this.content = content;
	}
    
    @Column(name = "reply", length = 50)
    public String getReply() {
		return reply;
	}
    public void setReply(String reply) {
		this.reply = reply;
	}
    @Column(name = "state")
    public Integer getState() {
		return state;
	}
    public void setState(Integer state) {
		this.state = state;
	}
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "replyTime", nullable = false, length = 19)
    public Date getReplyTime() {
		return replyTime;
	}
    public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
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
