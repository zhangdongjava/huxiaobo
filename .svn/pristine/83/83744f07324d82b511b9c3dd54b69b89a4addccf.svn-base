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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 家谱表
 * 分表关键字是creatorId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_genealogy")
public class TGenealogy implements java.io.Serializable {

	public static final int TYPE_PRIVATE = 1;
	public static final int TYPE_PUBLIC = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 308173796642423144L;
	private long genealogyId = 0;
	/**创建人*/
	private long creatorId = 0;
	/**缺省，以当前节点查询家谱树*/
	private long centerId = 0;
	/**家谱名称*/
	private String name = null;
	/**姓*/
	private String surname = null;
	/**头像*/
	private String thumbnail = null;
	/** 介绍 */
	private String introduce = null;
	/**字辈*/
	private String generationName = null;
	/**家谱类型1私有家谱，2共有家谱*/
	private Integer type = null;
	private Date createTime = null;
	
	private int familyCount = 0;

	public TGenealogy() {
	}

	public TGenealogy(long id, long creatorId, long centerId, Date createTime) {
		this.genealogyId = id;
		this.creatorId = creatorId;
		this.centerId = centerId;
		this.createTime = createTime;
	}

	public TGenealogy(long id, long creatorId, long centerId, String name, String surname, Integer type, Date createTime) {
		this.genealogyId = id;
		this.creatorId = creatorId;
		this.centerId = centerId;
		this.name = name;
		this.surname = surname;
		this.type = type;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_genealogy",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getGenealogyId() {
		return genealogyId;
	}

	public void setGenealogyId(long genealogyId) {
		this.genealogyId = genealogyId;
	}

	@Column(name = "creatorId", nullable = false)
	public long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "centerId")
	public long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "surname", length = 10)
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	@Column(name = "thumbnail", length = 200)
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	@Column(name = "generationName", length = 20)
	public String getGenerationName() {
		return generationName;
	}
	/** 介绍 */
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public void setGenerationName(String generationName) {
		this.generationName = generationName;
	}
	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public int getFamilyCount() {
		return familyCount;
	}

	public void setFamilyCount(int familyes) {
		this.familyCount = familyes;
	}
}
