package com.findu.dto;

import java.util.Date;

import com.findu.model.TFriendApply;
import com.findu.model.TUser;
import com.findu.utils.DataUtils;
import com.findu.utils.DateUtils;


public class FUFriendApply {
	
	private long applyId = 0;
	private long userId = 0;
	private long friendId = 0;
	
	private FUUser user = null;
	private FUUser friend = null;
	
	/**申请类型，0 未处理，1 同意 2 同意并加为好友 3 拒绝 4 忽略*/
	private Integer state = null;
	
	/**朋友类型，1好友，2宗亲*/
	private Integer type = null;
	/**昵称*/
	private String content = null;
	private String reply = null;
	private Date replyTime = null;
	private Date createTime = null;
	
	
	public FUFriendApply() {
		// TODO Auto-generated constructor stub
	}
	public FUFriendApply(TFriendApply friendApply,TUser user,TUser friend) {
		// TODO Auto-generated constructor stub
		DataUtils.CopyBean( friendApply,this);
		if (user!=null) {
			this.user = new FUUser();
			DataUtils.CopyBean(user, this.user);
		}
		if (friend!=null) {
			this.friend = new FUUser();
			DataUtils.CopyBean(friend, this.friend);
		}
	}
	
	public long getApplyId() {
		return applyId;
	}
    public void setApplyId(long applyId) {
		this.applyId = applyId;
	}
    
    
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFriendId() {
		return this.friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
    
	/**朋友类型，1好友，2宗亲*/
	public Integer getType() {
		return type;
	}
    public void setType(Integer type) {
		this.type = type;
	}
    
    

    public String getContent() {
		return content;
	}
    public void setContent(String content) {
		this.content = content;
	}
    
    
    public String getReply() {
		return reply;
	}
    public void setReply(String reply) {
		this.reply = reply;
	}

    /**申请类型，0 未处理，1 同意 2 同意并加为好友 3 拒绝 4 忽略*/
    public Integer getState() {
		return state;
	}
    public void setState(Integer state) {
		this.state = state;
	}
    
    public Date getReplyTime() {
		return replyTime;
	}
    public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
    
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public FUUser getUser() {
		return user;
	}
	public void setUser(FUUser user) {
		this.user = user;
	}
	public FUUser getFriend() {
		return friend;
	}
	public void setFriend(FUUser friend) {
		this.friend = friend;
	}
	
}
