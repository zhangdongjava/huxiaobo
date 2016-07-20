package com.findu.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.findu.dto.FUResult;
import com.findu.dto.FUSearchPage;
import com.findu.dto.FUUser;
import com.findu.model.TPartner;
import com.findu.model.TUser;
import com.findu.service.UserService;
import com.findu.utils.DataUtils;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.NetUtils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 用户模块接口，通过AuthInterceptor做消息鉴权。
 */
@Controller
@RequestMapping(AuthInterceptor.BASE_PATH)
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 查询用户信息
	 * @param id 用户id
	 * @return 查询成功code = 200，实体是FUUser。
	 * 访问地址：get	/findu/v2_1/user
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetUser(@RequestParam(value="id", required=true) long id){
		logger.info(String.format("get user of %d", id));
		FUResult result = new FUResult();
		try {
			FUUser user = userService.getUser(id);
			if (user == null) {
				result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
			} else {
				ArrayList<FUUser> list = new ArrayList<FUUser>();
				list.add(user);
				result.setData(list);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);		
			} 
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			
		}
		return result;
	}
	
	/**
	 * 修改用户信息
	 * @param request 建议将com.findu.model.TUser转为json进行传输，如果payword和payPassword为空则该两字段的值无效。 
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put	/findu/v2_1/user
	 */
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateUser(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TUser user = DataUtils.Json2Object(json, TUser.class);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update user of %d", user.getUserId()));
				int code = userService.updateUser(user);
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
	 * 添加用户信息
	 * @param request 建议将com.findu.model.TUser转为json进行传输 ，但不传递id和createTime。
	 * @return 添加成功code = 200，返回FUUser对象，但不包含第三方帐号信息。
	 * 访问地址：post	/findu/v2_1/user
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertUser(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TUser user = DataUtils.Json2Object(json, TUser.class);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert user of %d", user.getUserId()));
				int res = userService.insertUser(user);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert user error");
					break;
				}
				FUUser uo = new FUUser();				
				if(!DataUtils.CopyBean(user, uo)){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("copy user bean error");
					break;
				}
				ArrayList<FUUser> data = new ArrayList<FUUser>();
				data.add(uo);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
				
			}catch (Exception e) {
				if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
					result.setCode(ErrorCodeUtil.ERROR_USER_DUPLICATE);
				}else{
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				}
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		
		return result;
	}
	
	/**
	 * 删除用户信息
	 * @param id 用户id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete		/findu/v2_1/user
	 */
	@RequestMapping(value = "/user", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteUser(@RequestParam(value="id", required=true) long id){
		logger.info(String.format("delete user of %d", id));
		FUResult result = new FUResult();
		try {
			int code = userService.deleteUser(id);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 查询第三方信息
	 * @param id 第三方id
	 * @param userId 用户id     
	 * @return 查询成功code = 200，实体是TPartner。
	 * 访问地址：get	/findu/v2_1/user/partner
	 */
	@RequestMapping(value = "/user/partner", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetPartner(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get parter of %d, %d", userId, id));
		FUResult result = new FUResult();
		TPartner partner = userService.getPartner(id, userId);
		if(partner == null){
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}else{
			ArrayList<TPartner> list = new ArrayList<TPartner>();
			list.add(partner);
			result.setData(list);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}
		return result;
	}
	
	/**
	 * 修改第三方信息
	 * @param request 建议将com.findu.model.TPartner转为json进行传输 
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put	/findu/v2_1/user/partner
	 */
	@RequestMapping(value = "/user/partner", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdatePartner(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TPartner partner = DataUtils.Json2Object(json, TPartner.class);
				if(partner == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update parter of %d, %d", partner.getUserId(), partner.getFuId()));
				int code = userService.updatePartner(partner);
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
	 * 添加第三方信息
	 * @param request 建议将com.findu.model.TPartner转为json进行传输 ，但不传递id和createTime
	 * ，可以不传partnerId，系统自己生成。
	 * @return 添加成功code = 200，返回TPartner对象。
	 * 访问地址：post	/findu/v2_1/user/partner
	 */
	@RequestMapping(value = "/user/partner", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertPartner(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TPartner partner = DataUtils.Json2Object(json, TPartner.class);
				if(partner == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert partner of %d", partner.getFuId()));
				int res = userService.insertPartner(partner);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert partner error");
					break;
				}
				ArrayList<TPartner> data = new ArrayList<TPartner>();
				data.add(partner);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
				
			}catch (Exception e) {
				if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
					result.setCode(ErrorCodeUtil.ERROR_USER_DUPLICATE);
				}else{
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				}
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		
		return result;
	}
	
	/**
	 * 删除用户信息
	 * @param id 第三方id
	 * @param userId 用户id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete		/findu/v2_1/user/partner
	 */
	@RequestMapping(value = "/user/partner", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeletePartner(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("delete partner of %d,%d", userId, id));
		FUResult result = new FUResult();
		try {
			int code = userService.deletePartner(id, userId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 用户登录
	 * @param request 建议将com.findu.model.TUser转为json进行传输 ，需要account或phone，以及password。
	 * @return 登录成功code = 200，返回FUUser对象。
	 * 访问地址：POST	/findu/v2_1/user/login
	 */
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public FUResult LoginUser(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TUser user = DataUtils.Json2Object(json, TUser.class);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("login user of %s", user.getAccount() == null ? user.getPhone() : user.getAccount()));
				FUUser uo = new FUUser();
				int code = userService.loginUser(user, uo);
				if(!ErrorCodeUtil.isSucc(code)){
					result.setCode(code);
					break;
				}
				ArrayList<FUUser> data = new ArrayList<FUUser>();				
				data.add(uo);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
				
			} catch (Exception e) {
				e.printStackTrace();
				result.setCode(ErrorCodeUtil.ERROR_ACCOUNT);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}
	
	/**
	 * 退出登录，未处理
	 * @param id 用户id
	 * @return 成功code = 200，不返回对象。
	 * 访问地址：DELETE	/findu/v2_1/user/login
	 */
	@RequestMapping(value = "/user/login", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult LogoutUser(@RequestParam(value="id", required=true) long id){
		FUResult result = new FUResult();
		result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		return result;
	}
	/**
	 * 更新用户的推送帐号ID
	 * @param request request 建议将com.findu.dto.FUUser转为json进行传输 只需用户 Id 和 pushId
	 * @return
	 */
	@RequestMapping(value = "/user/push", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateUserPush(HttpServletRequest request){
		FUResult result = new FUResult();
//		do {
//			try {
//				String json = NetUtils.getHttpRequestBody(request).trim();
//				if(json == null || json.length() <= 0){
//					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
//					logger.warn("can not get request body");
//					break;
//				}
//				FUUser fuuser = DataUtils.Json2Object(json, FUUser.class);
//				if (fuuser==null) {
//					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
//					logger.warn("json object is error");
//					break;
//				}
//				if (fuuser.getUserId()==0 || fuuser.getPushId()==null) {
//					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
//					logger.warn("参数错误");
//					break;
//				}
//				TUser user = userService.getTUser(fuuser.getUserId());
//				if (user==null) {
//					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
//					logger.warn("用户不存在");
//					break;
//				}
//				TPartner push = userService.getPartner(fuuser.getUserId(), TPartner.TYPE_PUSH);
//				int code = 500;
//				if (push==null) {
//					push = new TPartner();
//					push.setUserId(fuuser.getUserId());
//					push.setType(TPartner.TYPE_PUSH);
//					push.setPartnerId(fuuser.getPushId());
//					code = userService.insertPartner(push);
//				}else{
//					push.setPartnerId(fuuser.getPushId());
//					code = userService.updatePartner(push);
//				}
//				result.setCode(code);
//			} catch (Exception e) {
//				// TODO: handle exception
//				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
//				logger.error(e.getMessage());
//			}
//		} while (false);
				
		return result;
	}
	/**
	 * 修改用户密码
	 * @param request 建议将com.findu.model.TUser转为json进行传输 ，需要account或phone，password和payPassword可选，其它字段不用。
	 * @return 成功code = 200，返回FUUser对象，不包含第三方帐号信息。
	 * 访问地址：PUT	/findu/v2_1/user/password
	 */
	@RequestMapping(value = "/user/password", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult ResetUserPassword(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TUser user = DataUtils.Json2Object(json, TUser.class);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update user of %d", user.getUserId()));
				int code = userService.resetUserPassword(user);
				if(!ErrorCodeUtil.isSucc(code)){
					result.setCode(code);
					logger.warn("reset password error");
					break;
				}
				FUUser uo = new FUUser();				
				if(!DataUtils.CopyBean(user, uo)){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("copy user bean error");
					break;
				}
				ArrayList<FUUser> data = new ArrayList<FUUser>();
				data.add(uo);
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
	 * 获取用户第三方帐号列表
	 * @param userId 用户id
	 * @return 查询成功code = 200，返回TPartner列表
	 * 访问地址：delete		/findu/v2_1/user/partners
	 */
	@RequestMapping(value = "/user/partners", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetPartners(@RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get partner list of %d", userId));;
		FUResult result = new FUResult();
		try {
			ArrayList<TPartner> data = userService.listPartner(userId);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 用户注册，将自动添加一个缺省的第三方帐号，并添加四条资产：零钱、现金蒲币、赠送蒲币、蒲籽。
	 * @param request 建议将com.findu.model.TUser转为json进行传输 ，但不传递id和createTime。
	 * @return 添加成功code = 200，返回FUUser对象。
	 * 访问地址：post	/findu/v2_1/user/register
	 */
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public FUResult RegisterUser(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TUser user = DataUtils.Json2Object(json, TUser.class);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				FUUser uo = new FUUser();
				int code = userService.registerUser(user, uo);
				if(!ErrorCodeUtil.isSucc(code)){
					result.setCode(code);
					logger.warn(String.format("register user %s,%s error", user.getAccount(), user.getPhone()));
					break;
				}
				logger.info(String.format("register user of %d", user.getUserId()));
				ArrayList<FUUser> data = new ArrayList<FUUser>();				
				data.add(uo);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			}catch (Exception e) {
				if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
					result.setCode(ErrorCodeUtil.ERROR_USER_DUPLICATE);
				}else{
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				}
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		
		return result;
	}
	@RequestMapping(value = "/user/find", method = RequestMethod.GET)
	@ResponseBody
	public FUResult FindUser(@RequestParam(value="phone", required=true) String phone){
		logger.info(String.format("用户查找--->"+phone));
		
		FUResult result = new FUResult();
		do {
			try {
				if (phone==null) {
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					break;
				}
				TUser user = new TUser();
				user.setPhone(phone);
				
				user = userService.findTUser(user, true);
				
				if (user==null) {
					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
					break;
				}
				
				FUUser theuser = userService.getUser(user.getUserId());
				ArrayList<FUUser> userlist = new ArrayList<FUUser>();
				userlist.add(theuser);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
				result.setData(userlist);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		} while (false);
		return result;
	}
	/**
	 * 查询注册用户列表，并按用户上次登陆时间降序排序。
	 * @param pageCount 分页查询总页数，总页数为0则会先计算总页数。
	 * @param pageNumber 分页查询页码，从0开始。
	 * @param pageSize 分页查询一页数据量。
	 * @return 查询成功code = 200，返回FUUser列表
	 * 访问地址：delete		/findu/v2_1/users
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetUsers(@RequestParam(value="pageCount", required=true) int pageCount
			, @RequestParam(value="pageNumber", required=true) int pageNumber
			, @RequestParam(value="pageSize", required=true) int pageSize){
		logger.info(String.format("get users of page %d, page count %d, page size %d"
				, pageNumber, pageCount, pageSize));
		
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
			int code = userService.listUser(page, result);
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
