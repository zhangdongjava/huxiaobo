package com.findu.dto;

import java.util.Date;

import com.findu.model.TSubject;

/**
 * 成员时光轴 对象
 * @author MacBook_Pro
 *
 */
public class FUMemberTimeline {
	
	private long timelineId = 0;
	
	private long memberId = 0;
	
	private long creatorId = 0;
	
	private String title = null;
	
	private String content = null;
	
	private Date createTime = null;
	
	private Integer type;
	
	private String time;
	
	private String address;
	
	
	public long getTimelineId() {
		return timelineId;
	}
	public void setTimelineId(long timelineId) {
		this.timelineId = timelineId;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public FUMemberTimeline() {
		// TODO Auto-generated constructor stub
	}
	public FUMemberTimeline(TSubject subject) {
		// TODO Auto-generated constructor stub
		if (subject!=null) {
			timelineId = subject.getSubjectId();
			memberId = subject.getRefId();
			creatorId = subject.getUserId();
			title = subject.getTitle();
			content = subject.getContent();
			createTime = subject.getCreateTime();
		}
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
