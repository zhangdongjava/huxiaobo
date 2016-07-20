package com.findu.model;
// Generated 2016-3-12 23:27:55 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;

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
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;

import com.findu.utils.SysParamUtils;

/**
 * 家庭成员表
 * 分表关键字是creatorId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_member")
public class TMember implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7069160255506253294L;
	private long memberId = 0;
	/**成员添加人*/
	private long creatorId = 0;
	/**用户id,可以不存在*/
	private Long userId = null;
	/**姓*/
	private String surname = null;
	/**名字*/
	private String name = null;
	/**昵称*/
	private String nickname = null;
	/**头像*/
	private String thumbnail = null;
	/**第几代人*/
	private String generation = null;
	/**字辈*/
	private String generationName = null;
	/**手机号*/
	private String phone = null;
	/**成员兄弟姐妹排行*/
	private int seniority = 0;
	/**性别1男，2女*/
	private Integer sexy = null;
	/**生日*/
	private String birthday = null;
	/**纪念日*/
	private String memorialDay = null;
	
	private Date createTime = null;
	
//	private List<TMemberExtend> extend = null;

	public TMember() {
	}

	public TMember(long id, long creatorId, Date createTime) {
		this.memberId = id;
		this.creatorId = creatorId;
		this.createTime = createTime;
	}

	public TMember(long id, long creatorId, Long userId, String surname, String name, String nickname,
			String thumbnail, String generation, Integer sexy, String birthday, String memorialDay, Date createTime) {
		this.memberId = id;
		this.creatorId = creatorId;
		this.userId = userId;
		this.surname = surname;
		this.name = name;
		this.nickname = nickname;
		this.thumbnail = thumbnail;
		this.generation = generation;
		this.sexy = sexy;
		this.birthday = birthday;
		this.memorialDay = memorialDay;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_member",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "creatorId", nullable = false)
	public long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "userId")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "surname", length = 10)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "name", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 头像url，可以是相对地址，则必须以 / 作为第一个字符。
	 */
	@Column(name = "thumbnail", length = 100)
	@Pattern(regexp = "(^/.+)|(^http://.+)", message = "头像url地址，必须以 http:// 或 / 开始")
	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		if(thumbnail == null)
			this.thumbnail = null;
		else if(thumbnail.indexOf(SysParamUtils.THUMNAIL_BASE_URL) == 0)
			this.thumbnail = thumbnail.substring(SysParamUtils.THUMNAIL_BASE_URL.length());
		else
			this.thumbnail = thumbnail;
	}

	@Column(name = "generation", length = 20)
	public String getGeneration() {
		return this.generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}
	@Column(name = "generationName", length = 20)
	public String getGenerationName() {
		return generationName;
	}
	public void setGenerationName(String generationName) {
		this.generationName = generationName;
	}
	/**
	 * 性别，1 男，2 女,0 未知。
	 */
	@Column(name = "sexy")
	@Range(min = 1, max = 2, message = "性别 1 男性，2 女性")
	public Integer getSexy() {
		return this.sexy;
	}

	public void setSexy(Integer sexy) {
		this.sexy = sexy;
	}
	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name = "birthday", length = 30)
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name = "memorialDay", length = 30)
	public String getMemorialDay() {
		return this.memorialDay;
	}

	public void setMemorialDay(String memorialDay) {
		this.memorialDay = memorialDay;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 19, updatable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "seniority")
	public int getSeniority() {
		return seniority;
	}
	public void setSeniority(int seniority) {
		this.seniority = seniority;
	}

//	public List<TMemberExtend> getExtend() {
//		return extend;
//	}
//	public void setExtend(List<TMemberExtend> extend) {
//		this.extend = extend;
//	}
}
