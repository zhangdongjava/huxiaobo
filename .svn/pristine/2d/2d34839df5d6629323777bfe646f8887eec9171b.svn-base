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
 * 回帖表，属于一个主题贴。
 * 分表关键字是refId。
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_post")
public class TPost implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3771173779954088148L;
	private long postId = 0;
	/**主题模块*/
	private long topicId = 0;
	/**与主题模块对应*/
	private long refId = 0;
	/**用户id*/
	private long userId = 0;
	/**主题id*/
	private long subjectId = 0;
	/**内容类型1文本信息，2地址信息*/
	private int contentType = 0;
	/**内容*/
	private String content = null;
	private Date createTime = null;

	public TPost() {
	}

	public TPost(long postId, long topicId, long refId, long userId, long subjectId, int contentType,
			String content, Date createTime) {
		this.postId = postId;
		this.topicId = topicId;
		this.refId = refId;
		this.userId = userId;
		this.subjectId = subjectId;
		this.contentType = contentType;
		this.content = content;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_post",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	@Column(name = "topicId", nullable = false)
	public long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	/**
	 * 和所属的TTopic实体一致。
	 * @return
	 */
	@Column(name = "refId", nullable = false)
	public long getRefId() {
		return this.refId;
	}

	public void setRefId(long refId) {
		this.refId = refId;
	}

	@Column(name = "userId", nullable = false)
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "subjectId", nullable = false)
	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	/*
	 * 内容类型，1 文本，2 地址。
	 */
	@Column(name = "contentType", nullable = false)
	public int getContentType() {
		return this.contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	@Column(name = "content", nullable = false, length = 300)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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
