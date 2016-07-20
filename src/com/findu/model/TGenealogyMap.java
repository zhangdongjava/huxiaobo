package com.findu.model;
// Generated 2016-3-12 23:27:55 by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户与家谱映射关系表
 * 分表关键字是userId
 */
@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_genealogy_map")
public class TGenealogyMap implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3829309300801017512L;
	private TGenealogyMapId id = null;
	/**家谱创建*/
	private long creatorId = 0;
	/**缺省，以当前节点查询家谱*/
	private long centerId = 0;
	/** 参与方式，1 创建, 2 加入*/
	private int type = 0;

	public TGenealogyMap() {
	}

	public TGenealogyMap(TGenealogyMapId id, long creatorId, long centerId, int type) {
		this.id = id;
		this.creatorId = creatorId;
		this.centerId = centerId;
		this.type = type;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false) ),
			@AttributeOverride(name = "genealogyId", column = @Column(name = "genealogyId", nullable = false) ) })
	public TGenealogyMapId getId() {
		return this.id;
	}

	public void setId(TGenealogyMapId id) {
		this.id = id;
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

	@Column(name = "type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
