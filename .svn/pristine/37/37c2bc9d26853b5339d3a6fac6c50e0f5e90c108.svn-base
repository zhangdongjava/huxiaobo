package com.findu.dto;

import java.util.Date;

import com.findu.model.TFriend;
import com.findu.utils.DataUtils;

public class FUFriend {
	/**
	 * 序列号
	 */
	private long fuId = 0;
	private long userId = 0;
	private long friendId = 0;
	
	/**朋友类型，1好友，2宗亲*/
	private Integer type = null;
	/**昵称*/
	private String nickname = null;
	private Date createTime = null;
	
	private FUUser friendUser;
	
	public FUFriend() {
		// TODO Auto-generated constructor stub
	}
	public FUFriend(TFriend friend,FUUser friendUser) {
		// TODO Auto-generated constructor stub
		DataUtils.CopyBean(friend, this);
		this.friendUser = friendUser;
	}
	
	/**序列号*/
	public long getFuId() {
		return fuId;
	}
	public void setFuId(long fuId) {
		this.fuId = fuId;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	/**朋友类型，1好友，2宗亲*/
	public String getNickname() {
		return nickname;
	}
	/**昵称*/
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/** 好友（FUUser 对象） */
	public FUUser getFriendUser() {
		return friendUser;
	}
	public void setFriendUser(FUUser friendUser) {
		this.friendUser = friendUser;
	}
	
	
	
}
