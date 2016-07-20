package com.findu.dto;

import java.util.Date;


/**
 * 家谱日志
 * @author MacBook_Pro
 *
 */
public class FUGenealogyLog {
	
	/**
	 * 日志ID
	 */
	private long logId = 0;
	
	/**
	 * 日志创建者Id
	 */
	private long creatorId = 0;
	/**
	 * 日志家谱ID
	 */
	private long genealogyId = 0;
	/**
	 * 日志类型
	 */
	private int type = 0;
	/**
	 * 日志内容
	 */
	private String content = null;
	/**
	 * 日志创建时间
	 */
	private Date createTime = null;
	
	public FUGenealogyLog() {
		// TODO Auto-generated constructor stub
	}
	
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public long getGenealogyId() {
		return genealogyId;
	}
	public void setGenealogyId(long genealogyId) {
		this.genealogyId = genealogyId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
