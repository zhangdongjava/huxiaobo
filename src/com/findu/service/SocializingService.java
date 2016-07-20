package com.findu.service;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findu.dao.TFriendApplyDao;
import com.findu.dao.TFriendDao;
import com.findu.dao.TPostDao;
import com.findu.dao.TSubjectDao;
import com.findu.dao.TTopicDao;
import com.findu.dao.TUserDao;
import com.findu.dto.FUFriendApply;
import com.findu.dto.FUMemberTimeline;
import com.findu.dto.FUResult;
import com.findu.dto.FUSearchPage;
import com.findu.dto.FUUser;
import com.findu.model.TFriend;
import com.findu.model.TFriendApply;
import com.findu.model.TPost;
import com.findu.model.TSubject;
import com.findu.model.TTopic;
import com.findu.model.TUser;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.FUPushUtils;

@Service
public class SocializingService {
	private static Logger logger = Logger.getLogger(SocializingService.class);
	
	@Autowired
	private TUserDao userDao;
	@Autowired
	private TFriendDao friendDao;
	
	@Autowired
	private TFriendApplyDao friendApplyDao;
	@Autowired
	private TTopicDao topicDao;
	
	@Autowired
	private TSubjectDao subjectDao;
	
	@Autowired
	private TPostDao postDao;
	
	public TFriend getFriend(long fuId, long userId) {
		TFriend friend = new TFriend();
		friend.setFuId(fuId);
		friend.setUserId(userId);
		return friendDao.get(friend, fuId);
	}
	public TFriend findFriend(long userId, long frendId) {
		
		TFriend friend = new TFriend();
		friend.setUserId(userId);
		friend.setFriendId(frendId);
		return friendDao.find(friend, true);
		
	}
	public TFriendApply getFriendApply(long userId, long friendId){
		
		return friendApplyDao.find(userId, friendId);
	}
	public TFriendApply findFriendApply(TFriendApply apply,boolean isAnd){
		
		return friendApplyDao.find(apply, true);
	}
	
	public int updateFriend(TFriend friend) {
		if(!friendDao.update(friend)){
			logger.warn("update friend error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	public int deleteFriendApply(TFriendApply apply) {
		boolean success = friendApplyDao.delete(apply);
		if (success==false) {
			return ErrorCodeUtil.ERROR_SYSTEM;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	public int deleteFriend(long fuId,long userId) {
		TFriend friend = new TFriend();
		friend.setFuId(fuId);
		friend.setUserId(userId);
		if(!friendDao.delete(friend)){
			logger.warn("delete friend error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public ArrayList<TFriend> listFriend(long userId){
		TFriend friend = new TFriend();
		friend.setUserId(userId);
		return friendDao.search(friend, true, null, false, null);
	}
	@SuppressWarnings("serial")
	public int listFriendApply(long userId,FUSearchPage page,FUResult result,int type){
		TFriendApply apply = new TFriendApply();
		apply.setUserId(userId);
		ArrayList<String> orderFields = new ArrayList<String>(){{add("createTime");}};
		ArrayList<TFriendApply> data = friendApplyDao.list(apply,type, orderFields, true, false, page);
		ArrayList<FUFriendApply> the_data = new ArrayList<FUFriendApply>();
		if (data!=null) {
			for (TFriendApply tFriendApply : data) {
				TUser user = new TUser();
				user = userDao.get(user, tFriendApply.getUserId());
				//TUser friend = new TUser();
				//friend = userDao.get(friend, tFriendApply.getFriendId());
				FUFriendApply a = new FUFriendApply(tFriendApply, user, null);
				the_data.add(a);
			}
		}
		result.setData(the_data);
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertFriend(TFriend friend) {
		if(friend.getCreateTime() == null)
			friend.setCreateTime(new Date());
		if(friendDao.insert(friend) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	public int insertFriendApply(TFriendApply friendapply) {
		if(friendapply.getCreateTime() == null)
			friendapply.setCreateTime(new Date());
		if(friendApplyDao.insert(friendapply) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		FUPushUtils.pushToOne(friendapply.getFriendId());
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	public int updateFriendApply(TFriendApply friendapply) {
		if(!friendApplyDao.update(friendapply)){
			logger.warn("update subject error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	public TTopic getTopic(long refId, int type){
		TTopic topic = new TTopic();
		topic.setRefId(refId);
		topic.setType(type);
		return topicDao.find(topic, true);
	}
	public TTopic getTopic(long topicId, long refId, int type) {
		TTopic topic = new TTopic();
		topic.setTopicId(topicId);
		topic.setRefId(refId);
		topic.setType(type);
		return topicDao.get(topic, topicId);
	}
	
	public int updateTopic(TTopic topic) {
		if(!topicDao.update(topic)){
			logger.warn("update topic error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteTopic(long topicId, long refId, int type) {
		TTopic topic = new TTopic();
		topic.setTopicId(topicId);
		topic.setRefId(refId);
		topic.setType(type);
		if(!topicDao.delete(topic)){
			logger.warn("delete topic error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertTopic(TTopic topic) {
		if(topic.getCreateTime() == null)
			topic.setCreateTime(new Date());
		if(topicDao.insert(topic) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TSubject getSubject(long subjectId, long refId) {
		TSubject subject = new TSubject();
		subject.setSubjectId(subjectId);;
		subject.setRefId(refId);
		return subjectDao.get(subject, subjectId);
	}
	
	public int updateSubject(TSubject subject) {
		if(!subjectDao.update(subject)){
			logger.warn("update subject error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteSubject(long subjectId, long refId) {
		TSubject subject = new TSubject();
		subject.setSubjectId(subjectId);
		subject.setRefId(refId);
		if(!subjectDao.delete(subject)){
			logger.warn("delete subject error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertSubject(TSubject subject) {
		if(subject.getCreateTime() == null)
			subject.setCreateTime(new Date());
		if(subjectDao.insert(subject) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TPost getPost(long postId, long refId) {
		TPost post = new TPost();
		post.setPostId(postId);
		post.setRefId(refId);
		return postDao.get(post, postId);
	}
	
	public int updatePost(TPost post) {
		if(!postDao.update(post)){
			logger.warn("update post error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deletePost(long postId, long refId) {
		TPost post = new TPost();
		post.setPostId(postId);
		post.setRefId(refId);
		if(!postDao.delete(post)){
			logger.warn("delete post error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertPost(TPost post) {
		if(post.getCreateTime() == null)
			post.setCreateTime(new Date());
		if(postDao.insert(post) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public ArrayList<TSubject> listSubject(long refId, Date startTime, Date endTime) {
		TSubject subject = new TSubject();
		subject.setRefId(refId);
		ArrayList<TSubject> data = subjectDao.list(subject,startTime,endTime);
		return data;
	}
	@SuppressWarnings("serial")
	public int listSubject(FUSearchPage page, FUResult result,long refId) {
		TSubject subject = new TSubject();
		subject.setRefId(refId);
		ArrayList<String> orderFields = new ArrayList<String>(){{add("createTime");}};
		ArrayList<TSubject> data = subjectDao.search(subject, true, orderFields, true, page);
		ArrayList<FUMemberTimeline> list = new ArrayList<>();
		for (TSubject tSubject : data) {
			FUMemberTimeline timeline = new FUMemberTimeline(tSubject);
			list.add(timeline);
		}
		result.setData(data);
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
}
