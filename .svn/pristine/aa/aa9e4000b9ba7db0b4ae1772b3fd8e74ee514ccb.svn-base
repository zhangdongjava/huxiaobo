package com.findu.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.findu.dto.FUFriend;
import com.findu.dto.FUResult;
import com.findu.dto.FUSearchPage;
import com.findu.dto.FUUser;
import com.findu.model.TFriend;
import com.findu.model.TFriendApply;
import com.findu.model.TPost;
import com.findu.model.TSubject;
import com.findu.model.TTopic;
import com.findu.model.TUser;
import com.findu.service.SocializingService;
import com.findu.service.UserService;
import com.findu.utils.DataUtils;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.FUPushUtils;
import com.findu.utils.NetUtils;

/**
 * 社交模块接口，通过AuthInterceptor做消息鉴权。
 */
@Controller
@RequestMapping(AuthInterceptor.BASE_PATH)
public class SocializingController {

	private static Logger logger = Logger.getLogger(SocializingController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private SocializingService socializingService;


	/**
	 * 查询好友
	 * @param id 好友id
	 * @param userId 用户id
	 * @return 查询成功code = 200，实体是TFriend。
	 * 访问地址：get	/findu/v2_1/friend
	 */
	@RequestMapping(value = "/friend", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetFriend(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get friend of %d,user id %d", id, userId));
		FUResult result = new FUResult();
		TFriend friend = socializingService.getFriend(id,userId);
		if(friend != null){
			ArrayList<TFriend> list = new ArrayList<TFriend>();
			list.add(friend);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			result.setData(list);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}

		return result;
	}

	/**
	 * 修改好友
	 * @param request 建议将com.findu.model.TFriend转为json进行传输 
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put	/findu/v2_1/friend
	 */
	@RequestMapping(value = "/friend", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateFriend(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TFriend friend = DataUtils.Json2Object(json, TFriend.class);
				if(friend == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update friend of %d, user id %d", friend.getFuId(), friend.getUserId()));
				int code = socializingService.updateFriend(friend);
				result.setCode(code);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);

		return result;
	}

	/**
	 * 添加好友
	 * @param request 建议将com.findu.model.TFriend转为json进行传输 ，但不传递id和createTime。
	 * @return 添加成功code= 200，返回TFriend对象。
	 * 访问地址：post	/findu/v2_1/friend
	 */
	@RequestMapping(value = "/friend", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertFriend(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TFriend friend = DataUtils.Json2Object(json, TFriend.class);
				if(friend == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert friend of %d, user id %d", friend.getFuId(), friend.getUserId()));
				int res = socializingService.insertFriend(friend);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert friend error");
					break;
				}
				ArrayList<TFriend> data = new ArrayList<TFriend>();
				data.add(friend);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 删除好友
	 * @param id 好友id
	 * @param userId 用户id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete	/findu/v2_1/friend
	 */
	@RequestMapping(value = "/friend", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteFriend(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("delete friend of %d,user id %d", id, userId));
		FUResult result = new FUResult();
		try {
			int code = socializingService.deleteFriend(id, userId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}

		return result;
	}

	/**
	 * 获得所有的好友
	 * @param userId 用户id
	 * @return 查询成功code = 200，返回TFriend列表
	 * 访问地址：get	/findu/v2_1/friends
	 */
	@RequestMapping(value = "/friends", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetFriends(@RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get friends of %d", userId));
		FUResult result = new FUResult();
		try {
			ArrayList<TFriend> data = socializingService.listFriend(userId);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}
	/**
	 * 获得所有的好友
	 * @param userId 用户id
	 * @return 查询成功code = 200，返回FUFriend列表
	 * 访问地址：get	/findu/v2_1/friend/list
	 */
	@RequestMapping(value = "/friend/list", method = RequestMethod.GET)
	@ResponseBody
	public FUResult ListFriend(@RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get friends of %d", userId));
		FUResult result = new FUResult();
		try {
			ArrayList<FUFriend> list = new ArrayList<FUFriend>();
			ArrayList<TFriend> data = socializingService.listFriend(userId);
			if (data!=null) {
				for (TFriend tFriend : data) {
					FUUser user =userService.getUser(tFriend.getFriendId());
					if (user!=null) {
						FUFriend ff = new FUFriend(tFriend, user);
						list.add(ff);
					}
				}
			}
			result.setData(list);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}
	/**
	 * 获取好友申请列表 
	 * @param userId 用户Id
	 * @param state： -1 获取全部，0 获取未处理的，1 获取已同意的
	 * @return 查询成功code = 200，返回FUFriendApply列表
	 */
	@RequestMapping(value = "/friend/apply", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetFriendApply(@RequestParam(value="userId", required=true) long userId,
			@RequestParam(value="state", required=true) int state,
			@RequestParam(value="pageCount", required=true) int pageCount, 
			@RequestParam(value="pageNumber", required=true) int pageNumber, 
			@RequestParam(value="pageSize", required=true) int pageSize){
		logger.info(String.format("friend apply 好友申请"));
		FUResult result = new FUResult();
		FUSearchPage page = new FUSearchPage();
		page.setPageCount(pageCount);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		if(pageCount < 0 || pageNumber < 0  || pageNumber > pageCount || pageSize <= 0){
			logger.warn("page parameter is error");
			result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
			return result;
		}
		try {
			int code = socializingService.listFriendApply(userId, page, result, state);
			if(!ErrorCodeUtil.isSucc(code)){
				logger.warn("list user error");
			}
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}
	/**
	 * 发起好友申请
	 * @param request 建议将com.findu.model.TFriendApply 转为json进行传输 只需要(userId,friendId,content)
	 * @return 修改成功code = 200，不返回对象。
	 * @return
	 */
	@RequestMapping(value = "/friend/apply", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertFriendApply(HttpServletRequest request){
		logger.info(String.format("friend apply 好友申请"));
		FUResult result = new FUResult();

		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TFriendApply friendApply = DataUtils.Json2Object(json, TFriendApply.class);
				if(friendApply == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("friendApply json object is error");
					break;
				}
				if (friendApply.getUserId() == friendApply.getFriendId()) {
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("error");
					break;
				}
				TUser user = userService.getTUser(friendApply.getUserId());
				if (user==null) {
					result.setCode(ErrorCodeUtil.ERROR_USER_NULL);
					logger.warn("user is null");
					break;
				}
				TUser f_user = userService.getTUser(friendApply.getFriendId());
				if (f_user==null) {
					result.setCode(ErrorCodeUtil.ERROR_USER_NULL);
					logger.warn("friend user is null");
					break;
				}
				

				TFriend f = socializingService.findFriend(friendApply.getUserId(), friendApply.getFriendId());
				if (f!=null) {
					result.setCode(ErrorCodeUtil.ERROR_FRIEND_DUPLICATE);
					break;
				}

				TFriendApply apply = socializingService.getFriendApply(friendApply.getUserId(), friendApply.getFriendId());

				int code = 0;
				if (apply==null) {
					apply = new TFriendApply();
					apply.setContent(friendApply.getContent());
					apply.setUserId(friendApply.getUserId());
					apply.setFriendId(friendApply.getFriendId());
					apply.setType(friendApply.getType());
					apply.setState(0);
					apply.setCreateTime(new Date());
					code = socializingService.insertFriendApply(apply);
				}else{
					if (apply.getState()==0) {
						result.setCode(ErrorCodeUtil.ERROR_FRIEND_APPLY_DUPLICATE);
						break;
					}else{
						
						socializingService.deleteFriendApply(apply);
						
						apply = new TFriendApply();
						apply.setContent(friendApply.getContent());
						apply.setUserId(friendApply.getUserId());
						apply.setFriendId(friendApply.getFriendId());
						apply.setType(friendApply.getType());
						apply.setState(0);
						apply.setCreateTime(new Date());
						code = socializingService.insertFriendApply(apply);
					}
				}
				result.setCode(code);

			} catch (Exception e) {
				// TODO: handle exception
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			}
		} while (false);

		return result;
	}
	/**
	 * 好友申请回复
	 * @param request 建议将com.findu.model.TFriendApply 转为json进行传输 只需要(userId,friendId,content)
	 * @return 修改成功code = 200，不返回对象。
	 */
	@RequestMapping(value = "/friend/apply", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateFriendApply(HttpServletRequest request){
		logger.info(String.format("friend apply 好友申请 回复"));
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TFriendApply friendApply = DataUtils.Json2Object(json, TFriendApply.class);
				if(friendApply == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("friendApply json object is error");
					break;
				}
				int state  = friendApply.getState();
				String reply = friendApply.getReply();
				friendApply.setState(null);
				friendApply.setReply(null);
				friendApply = socializingService.findFriendApply(friendApply, true);
				if (friendApply==null) {
					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
					logger.warn("friendApply is null");
					break;
				}
				int code = 0;
				if (state>0&&state<5) {
					Date date = new Date();
					if (state==1) {
						TFriend f = new TFriend();
						f.setUserId(friendApply.getUserId());
						f.setFriendId(friendApply.getFriendId());
						f.setType(friendApply.getType());
						f.setCreateTime(date);
						code = socializingService.insertFriend(f);
						if (code==200) {
							friendApply.setState(state);
							friendApply.setReply(reply);
							friendApply.setReplyTime(date);
							code = socializingService.updateFriendApply(friendApply);
						}
						if (code!=200) {
							result.setCode(code);
							break;
						}
					}else if (state==2) {
						TFriend f = new TFriend();
						f.setUserId(friendApply.getUserId());
						f.setFriendId(friendApply.getFriendId());
						f.setType(friendApply.getType());
						f.setCreateTime(date);
						code = socializingService.insertFriend(f);
						if (code!=200) {
							result.setCode(code);
							break;
						}
						TFriend f2 = new TFriend();
						f2.setUserId(friendApply.getFriendId());
						f2.setFriendId(friendApply.getUserId());
						f2.setType(friendApply.getType());
						f2.setCreateTime(date);
						code = socializingService.insertFriend(f2);
						if (code==200) {
							friendApply.setState(state);
							friendApply.setReply(reply);
							friendApply.setReplyTime(date);
							code = socializingService.updateFriendApply(friendApply);
						}
						if (code!=200) {
							result.setCode(code);
							break;
						}
					}else{

						friendApply.setState(state);
						friendApply.setReply(reply);
						friendApply.setReplyTime(new Date());
						code = socializingService.updateFriendApply(friendApply);

					}

				}else{

					friendApply.setState(state);
					friendApply.setReply(reply);
					friendApply.setReplyTime(new Date());
					code = socializingService.updateFriendApply(friendApply);

				}

				result.setCode(code);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} while (false);
		return result;
	}
	
	@RequestMapping(value = "/friend/apply", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteFriendApply(@RequestParam(value="userId", required=true) long userId,
			@RequestParam(value="friendId", required=true) long friendId){
		logger.info(String.format("friend apply 好友申请 回复"));
		FUResult result = new FUResult();
		TFriendApply ap = socializingService.getFriendApply(userId, friendId);
		if (ap==null) {
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
			logger.warn("friendApply is null");
			return result;
		}
		int code = socializingService.deleteFriendApply(ap);
		result.setCode(code);
		return result;
	}
	/**
	 * 查询家族讨论话题
	 * @param id 家族讨论话题id
	 * @param refId 创建家族讨论话题人id
	 * @param type 家族讨论话题类型
	 * @return 查询成功code = 200，实体是TTopic。
	 * 访问地址：get    /findu/v2_1/topic
	 */
	@RequestMapping(value = "/topic", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetTopic(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId, @RequestParam(value="type", required=true) int type){
		logger.info(String.format("get topic of %d, reference id %d, type %d", id, refId, type));
		FUResult result = new FUResult();
		TTopic topic = socializingService.getTopic(id,refId,type);
		if(topic != null){
			ArrayList<TTopic> data = new ArrayList<TTopic>();
			data.add(topic);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}
	
	/**
	 * 修改家庭讨论话题
	 * @param request 建议将com.findu.model.TTopic转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put    /findu/v2_1/topic
	 */
	@RequestMapping(value = "/topic", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateTopic(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TTopic topic = DataUtils.Json2Object(json, TTopic.class);
				if(topic == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update topic of %d, reference id %d, type % ",topic.getTopicId(), topic.getRefId(), topic.getType()));
				int code = socializingService.updateTopic(topic);
				result.setCode(code);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 添加家族讨论话题
	 * @param request 建议将com.findu.model.TTopic转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TTopic对象。
	 * 访问地址：post    /findu/v2_1/topic
	 */
	@RequestMapping(value = "/topic", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertTopic(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TTopic topic = DataUtils.Json2Object(json, TTopic.class);
				if(topic == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert topic of %d, reference id %d, type %d",topic.getTopicId(),topic.getRefId(),topic.getType()));
				int res = socializingService.insertTopic(topic);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert topic error");
					break;
				}
				ArrayList<TTopic> data = new ArrayList<TTopic>();
				data.add(topic);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 删除家族讨论话题
	 * @param id 家族讨论话题id
	 * @param refId 创建家族讨论话题人id
	 * @param type 家族讨论话题类型
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete    /findu/v2_1/topic
	 */
	@RequestMapping(value = "/topic", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteTopic(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId, @RequestParam(value="type", required=true) int type){
		logger.info(String.format("get topic of %d, reference id %d, type %d", id, refId, type));
		FUResult result = new FUResult();
		try {
			int code = socializingService.deleteTopic(id, refId, type);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家族讨论话题主题
	 * @param id 主题id
	 * @param refId 创建家族讨论话题人id
	 * @return 查询成功code = 200，实体是TSubject。
	 * 访问地址：get    /findu/v2_1/subject
	 */
	@RequestMapping(value = "/subject", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetSubject(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId){
		logger.info(String.format("get subject of %d,reference id %d", id, refId));
		FUResult result = new FUResult();
		TSubject subject = socializingService.getSubject(id,refId);
		if(subject != null){
			ArrayList<TSubject> data = new ArrayList<TSubject>();
			data.add(subject);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 修改家族讨论话题主题
	 * @param request 建议将com.findu.model.TSubject转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put    /findu/v2_1/subject
	 */
	@RequestMapping(value = "/subject", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateSubject(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TSubject subject = DataUtils.Json2Object(json, TSubject.class);
				if(subject == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update subject of %d, reference id %d", subject.getSubjectId(), subject.getRefId()));
				int code = socializingService.updateSubject(subject);
				result.setCode(code);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 添加家族讨论话题主题
	 * @param request 建议将com.findu.model.TSubject转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TSubject对象。
	 * 访问地址：post    /findu/v2_1/subject
	 */
	@RequestMapping(value = "/subject", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertSubject(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TSubject subject = DataUtils.Json2Object(json, TSubject.class);
				if(subject == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert subject of %d, reference id %d",subject.getSubjectId(), subject.getRefId()));
				int res = socializingService.insertSubject(subject);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert subject error");
					break;
				}
				ArrayList<TSubject> data = new ArrayList<TSubject>();
				data.add(subject);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 删除家族讨论话题主题
	 * @param id 主题id
	 * @param refId 创建家族讨论话题人id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete    /findu/v2_1/subject
	 */
	@RequestMapping(value = "/subject", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteSubject(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId){
		logger.info(String.format("delete subject of %d,reference id %d", id, refId));
		FUResult result = new FUResult();
		try {
			int code = socializingService.deleteSubject(id, refId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家族跟帖
	 * @param id 跟帖id
	 * @param refId 创建家族讨论话题人id
	 * @return 查询成功code = 200，实体是TPost。
	 * 访问地址：get    /findu/v2_1/post
	 */
	@RequestMapping(value = "/post", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetPost(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId){
		logger.info(String.format("get post of %d, reference id %d", id, refId));
		FUResult result = new FUResult();
		TPost post = socializingService.getPost(id,refId);
		if(post != null){
			ArrayList<TPost> data = new ArrayList<TPost>();
			data.add(post);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 修改家族跟帖
	 * @param request 建议将com.findu.model.TPost转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put    /findu/v2_1/post
	 */
	@RequestMapping(value = "/post", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdatePost(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TPost post = DataUtils.Json2Object(json, TPost.class);
				if(post == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update post of %d,reference id %d", post.getPostId(), post.getRefId()));
				int code = socializingService.updatePost(post);
				result.setCode(code);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 添加家族跟帖
	 * @param request 建议将com.findu.model.TPost转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TPost对象。
	 * 访问地址：post    /findu/v2_1/post
	 */
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertPost(HttpServletRequest request){
		FUResult result = new FUResult();
		do {	
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TPost post = DataUtils.Json2Object(json, TPost.class);
				if(post == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert post of %d, reference id %d", post.getPostId(), post.getRefId()));
				int res = socializingService.insertPost(post);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert post error");
					break;
				}
				ArrayList<TPost> data = new ArrayList<TPost>();
				data.add(post);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}

	/**
	 * 删除家族跟帖
	 * @param id 跟帖id
	 * @param refId 创建家族讨论话题人id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete    /findu/v2_1/post
	 */
	@RequestMapping(value = "/post", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeletePost(@RequestParam(value="id", required=true) long id, @RequestParam(value="refId", required=true) long refId){
		logger.info(String.format("delete post of %d, reference id %d", id, refId));
		FUResult result = new FUResult();
		try {
			socializingService.deletePost(id, refId);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询主题列表
	 * @param refId 创建家族讨论话题人id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 查询成功code = 200，返回TSubject列表;
	 * 访问地址：get    /findu/v2_1/subjects
	 */
	@RequestMapping(value = "/subjects", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetSubjects(@RequestParam(value="refId", required=true) long refId, @RequestParam(value="startTime", required=true) Date startTime, @RequestParam(value="endTime", required=true) Date endTime){
		logger.info(String.format("get subjects of reference id  %d, start time %tc, end time %tc", refId, startTime, endTime));
		FUResult result = new FUResult();
		ArrayList<TSubject> data = socializingService.listSubject(refId, startTime, endTime);
		result.setData(data);
		result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		return result;
	}
	@RequestMapping(value = "/subject/list", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetSubjectList(@RequestParam(value="refId", required=true) long refId, 
			@RequestParam(value="pageCount", required=true) int pageCount, 
			@RequestParam(value="pageNumber", required=true) int pageNumber, 
			@RequestParam(value="pageSize", required=true) int pageSize){
		logger.info(String.format("get subjects of reference id  %d", refId));
		FUResult result = new FUResult();

		if(pageCount < 0 || pageNumber < 0  || pageNumber > pageCount || pageSize <= 0){
			logger.warn("page parameter is error");
			result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
			return result;
		}
		FUSearchPage page = new FUSearchPage();
		page.setPageCount(pageCount);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);	

		try {
			int code = socializingService.listSubject(page, result, refId);
			if(!ErrorCodeUtil.isSucc(code)){
				logger.warn("list user error");
			}
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}
}
