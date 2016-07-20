package com.findu.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.findu.dto.FUFamilyMember;
import com.findu.dto.FUFamilyTree;
import com.findu.dto.FUMemberAdd;
import com.findu.dto.FUMemberInfo;
import com.findu.dto.FUMemberTimeline;
import com.findu.dto.FUResult;
import com.findu.dto.FUSearchPage;
import com.findu.dto.FUUser;
import com.findu.model.TFamily;
import com.findu.model.TGenealogy;
import com.findu.model.TGenealogyMap;
import com.findu.model.TMember;
import com.findu.model.TMemberExtend;
import com.findu.model.TMemberRight;
import com.findu.model.TSubject;
import com.findu.model.TTopic;
import com.findu.model.TUser;
import com.findu.service.GenealogyService;
import com.findu.service.SocializingService;
import com.findu.service.UserService;
import com.findu.utils.DataUtils;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.NetUtils;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * 家谱模块接口，通过AuthInterceptor做消息鉴权。
 */
@Controller
//@RequestMapping(AuthInterceptor.BASE_PATH)
public class GenealogyController {

	private static Logger logger = Logger.getLogger(GenealogyController.class);

	@Autowired
	private GenealogyService genealogyService;
	@Autowired	
	private SocializingService socializingService;
	@Autowired
	private UserService userService;
	/**
	 * 查询家族成员
	 * @param id  家族成员id
	 * @param creatorId  创建家族成员的id  主要用于分表查询
	 * @return  查询结果存在 code = 200，实体是TMember。
	 * 访问地址：get     /findu/v2_1/member
	 */
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetMember(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("get member of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		TMember member = genealogyService.getMember(id, creatorId);
		if(member == null){
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			
			ArrayList<TMember> members = new ArrayList<TMember>();
			
			members.add(member);
			
			result.setData(members);
		}

		return result;
	}

	/**
	 * 修改家族成员
	 * @param request 建议将com.findu.model.TMember转为json进行传输
	 * @return 家族成员修改成功 code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/member
	 */
	@RequestMapping(value = "/member", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateMember(HttpServletRequest request){
		FUResult result = new FUResult();
		do{
			try{
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMember member = DataUtils.Json2Object(json, TMember.class);
				if(member == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update member of %d, creator id %d", member.getMemberId(), member.getCreatorId()));
				if (member.getPhone()!=null) {
					TUser u = new TUser();
					u.setPhone(member.getPhone());
					TUser user = userService.findTUser(u, true);
					if (user!=null) {
						member.setUserId(user.getUserId());
					}
				}
				int code = genealogyService.updateMember(member);
				
				result.setCode(code);
			}catch(Exception e){
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		}while(false);
		return result;
	}

	/**
	 * 添加家族成员
	 * @param request 建议将com.findu.model.TMember转为json进行传输，但不传递id和createTime。
	 * @return 家族成员添加成功 code = 200，返回TMember对象。
	 * 访问地址：post     /findu/v2_1/member
	 */
	@RequestMapping(value = "/member", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertMember(HttpServletRequest request){
		FUResult result = new FUResult();
		do{
			try{
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMember member = DataUtils.Json2Object(json, TMember.class);
				if(member == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert member of %d, creator id %d", member.getMemberId(), member.getCreatorId()));
				if (member.getPhone()!=null) {
					TUser u = new TUser();
					u.setPhone(member.getPhone());
					TUser user = userService.findTUser(u, true);
					if (user!=null) {
						member.setUserId(user.getUserId());
					}
				}
				int res = genealogyService.insertMember(member);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert member error");
					break;
				}
				ArrayList<TMember> data = new ArrayList<TMember>();
				data.add(member);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			}catch(Exception e){
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		}while(false);
		return result;
	}
	/**
	 * 添加家族成员
	 * @param request 建议将com.findu.dto.FUMemberAdd转为json进行传输，但不传递id和createTime。
	 * @return 家族成员添加成功 code = 200，返回TMember对象。
	 * 访问地址：post     /findu/v2_1/member/add
	 */
	@RequestMapping(value = "/member/add", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertMemberAdd(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				FUMemberAdd memberadd = DataUtils.Json2Object(json, FUMemberAdd.class);
				if(memberadd == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				int code = genealogyService.addMember(memberadd);
				result.setCode(code);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.getMessage());
			}
		} while (false);
		return result;
	}

	/**
	 * 删除家族成员
	 * @param id 家族成员id
	 * @param creatorId  创建家族成员的id  主要用于分表查询
	 * @return  删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/member
	 */
	@RequestMapping(value = "/member", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteMember(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("delete member of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteMember(id, creatorId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家族成员扩展信息
	 * @param id  家族成员id
	 * @param memberId  创建家族成员的id  主要用于分表查询
	 * @return  查询结果存在 code = 200，实体是TMemberExtend。
	 * 访问地址：get     /findu/v2_1/member/extend
	 */
	@RequestMapping(value = "/member/extend", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetMemberExt(@RequestParam(value="id", required=true) long id, @RequestParam(value="memberId", required=true) long memberId){
		logger.info(String.format("get member extend of %d, member id %d", id, memberId));
		FUResult result = new FUResult();
		TMemberExtend memberExt = genealogyService.getMemberExt(id, memberId);
		if(memberExt == null){
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			ArrayList<TMemberExtend> members = new ArrayList<TMemberExtend>();
			members.add(memberExt);
			result.setData(members);
		}

		return result;
	}

	/**
	 * 修改家族成员扩展信息
	 * @param request 建议将com.findu.model.TMemberExtend转为json进行传输
	 * @return 家族成员修改成功 code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/member/extend
	 */
	@RequestMapping(value = "/member/extend", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateMemberExt(HttpServletRequest request){
		FUResult result = new FUResult();
		do{
			try{
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMemberExtend memberExt = DataUtils.Json2Object(json, TMemberExtend.class);
				if(memberExt == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update member extend of %d, memberId id %d", memberExt.getId(), memberExt.getMemberId()));
				int code = genealogyService.updateMemberExt(memberExt);
				result.setCode(code);
			}catch(Exception e){
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		}while(false);
		return result;
	}
	
	/**
	 * 获取成员信息
	 * @param id 家庭成员编号
	 * @param creatorId 创建者编号
	 * @return 查询结果存在 code = 200，实体是TMember
	 */
	@RequestMapping(value = "/member/info", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetMemberInfo(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("get member of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		TMember member = genealogyService.getMember(id, creatorId);
		if(member == null){
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			List<TMemberExtend> es = genealogyService.listMemberExt(id);
			
			FUMemberInfo memberinfo = FUMemberInfo.fromObjects(member, es);
			
			ArrayList<FUMemberInfo> members = new ArrayList<FUMemberInfo>();
			
			members.add(memberinfo);
			
			result.setData(members);
		}

		return result;
	}
	/**
	 * 更新成员信息
	 * @param request 建议将com.findu.dto.FUMemberInfo 转为 json进行传输
	 * @return 家族成员修改成功 code = 200，不返回对象。
	 */
	@RequestMapping(value = "/member/info", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateMemberInfo(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try{
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				FUMemberInfo memberinfo = DataUtils.Json2Object(json, FUMemberInfo.class);
				if(memberinfo == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				
				JSONObject objc = JSONObject.fromObject(json);
				objc.put("id", objc.getString("memberId"));
				
				TMember member = DataUtils.Json2Object(objc.toString(), TMember.class);
				if(member == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				if (member.getPhone()!=null) {
					TUser u = new TUser();
					u.setPhone(member.getPhone());
					TUser user = userService.findTUser(u, true);
					if (user!=null) {
						member.setUserId(user.getUserId());
					}
				}
				//logger.info(String.format("update member extend of %d, memberId id %d", memberExt.getId(), memberExt.getMemberId()));
				int code = genealogyService.updateMember(member);
				if (code==200) {
					for (TMemberExtend ext : FUMemberInfo.memberExtends(memberinfo)) {
						TMemberExtend t_ext = genealogyService.getMemberExt(ext.getMemberId(), ext.getName());
						if (t_ext!=null) {
							t_ext.setValue(ext.getValue());
							code = genealogyService.updateMemberExt(t_ext);
							if (code!=200) {
								break;
							}
						}else{
							code = genealogyService.insertMemberExt(ext);
							if (code!=200) {
								break;
							}
						}
					}
				}
				result.setCode(code);
			}catch(Exception e){
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}
	/**
	 * 添加家族成员扩展信息
	 * @param request 建议将com.findu.model.TMemberExtend转为json进行传输，但不传递id和createTime。
	 * @return 家族成员添加成功 code = 200，返回TMemberExtend对象。
	 * 访问地址：post     /findu/v2_1/member/extend
	 */
	@RequestMapping(value = "/member/extend", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertMemberExt(HttpServletRequest request){
		FUResult result = new FUResult();
		do{
			try{
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMemberExtend memberExt = DataUtils.Json2Object(json, TMemberExtend.class);
				if(memberExt == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert member extend of %d, member id %d", memberExt.getId(), memberExt.getMemberId()));
				TMemberExtend mex = genealogyService.getMemberExt(memberExt.getMemberId(), memberExt.getName());
				int res = 0;
				if (mex!=null) {
					mex.setValue(memberExt.getValue());
					res = genealogyService.updateMemberExt(mex);
					memberExt = mex;
				}else{
					res = genealogyService.insertMemberExt(memberExt);
				}
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
					logger.warn("insert member extend error");
					break;
				}
				ArrayList<TMemberExtend> data = new ArrayList<TMemberExtend>();
				data.add(memberExt);
				result.setData(data);
				result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			}catch(Exception e){
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		}while(false);
		return result;
	}

	/**
	 * 删除家族成员扩展信息
	 * @param id 家族成员id
	 * @param memberId  创建家族成员的id  主要用于分表查询
	 * @return  删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/member/extend
	 */
	@RequestMapping(value = "/member/extend", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteMemberExt(@RequestParam(value="id", required=true) long id, @RequestParam(value="memberId", required=true) long memberId){
		logger.info(String.format("delete member extend of %d, member id %d", id, memberId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteMemberExt(id, memberId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家谱信息
	 * @param id 家谱id
	 * @param creatorId 家谱创建人id
	 * @return 查询结果存在 code = 200，实体是TGenealogy。
	 * 访问地址：get     /findu/v2_1/genealogy
	 */
	@RequestMapping(value = "/genealogy", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetGenealogy(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("get genealogy of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		TGenealogy genealogy = genealogyService.getGenealogy(id, creatorId);
		if(genealogy != null){
			ArrayList<TGenealogy> data = new ArrayList<TGenealogy>();
			data.add(genealogy);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 修改家谱信息
	 * @param request 建议将com.findu.model.TGenealogy转为json进行传输
	 * @return 修改成功 code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/genealogy
	 */
	@RequestMapping(value = "/genealogy", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateGenealogy(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TGenealogy genealogy = DataUtils.Json2Object(json, TGenealogy.class);
				if(genealogy == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update genealogy of %d, creator id %d", genealogy.getGenealogyId(), genealogy.getCreatorId()));
				int code = genealogyService.updateGenealogy(genealogy);
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
	 * 添加家谱信息
	 * @param request 建议将com.findu.model.TGenealogy转为json进行传输，但不传递id和createTime。
	 * @return 添加成功 code = 200，返回TGenealogy对象。
	 * 访问地址：post     /findu/v2_1/genealogy
	 */
	@RequestMapping(value = "/genealogy", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertGenealogy(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TGenealogy genealogy = DataUtils.Json2Object(json, TGenealogy.class);
				if(genealogy == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert genealogy of %d, creator id %d", genealogy.getGenealogyId(), genealogy.getCreatorId()));
				int res = genealogyService.insertGenealogy(genealogy);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert genealogy error");
					break;
				}
				ArrayList<TGenealogy> data = new ArrayList<TGenealogy>();
				data.add(genealogy);
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
	 * 删除家谱信息
	 * @param id 家谱id
	 * @param creatorId 家谱创建人id
	 * @return 删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/genealogy
	 */
	@RequestMapping(value = "/genealogy", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteGenealogy(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("delete genealogy of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteGenealogy(id, creatorId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家谱映射
	 * @param userId 用户id
	 * @param genealogyId 家谱id
	 * @return 查询成功 code = 200，实体是TGenealogyMap。
	 * 访问地址：get     /findu/v2_1/genealogyMap
	 */
	@RequestMapping(value = "/genealogyMap", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetGenealogyMap(@RequestParam(value="userId", required=true) long userId, @RequestParam(value="genealogyId", required=true) long genealogyId){
		logger.info(String.format("get genealogyMap of %d, genealogy id %d", userId, genealogyId));
		FUResult result = new FUResult();
		TGenealogyMap genealogyMap = genealogyService.getGenealogyMap(userId, genealogyId);
		if(genealogyMap != null){
			ArrayList<TGenealogyMap> data = new ArrayList<TGenealogyMap>();
			data.add(genealogyMap);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 修改家谱映射
	 * @param request 建议将com.findu.model.TGenealogyMap转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/genealogyMap
	 */
	@RequestMapping(value = "/genealogyMap", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateGenealogyMap(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TGenealogyMap genealogyMap = DataUtils.Json2Object(json, TGenealogyMap.class);
				if(genealogyMap == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update genealogyMap of %d, genealogy id %d", genealogyMap.getId().getUserId(), genealogyMap.getId().getGenealogyId()));
				int code = genealogyService.updateGenealogyMap(genealogyMap);
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
	 * 添加家谱映射
	 * @param request 建议将com.findu.model.TGenealogyMap转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TGenealogyMap对象。
	 * 访问地址：post     /findu/v2_1/genealogyMap
	 */
	@RequestMapping(value = "/genealogyMap", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertGenealogyMap(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TGenealogyMap genealogyMap = DataUtils.Json2Object(json, TGenealogyMap.class);
				if(genealogyMap == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert genealogyMap of %d, genealogy id %d", genealogyMap.getId().getUserId(), genealogyMap.getId().getGenealogyId()));
				int res = genealogyService.insertGenealogyMap(genealogyMap);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert genealogy map error");
					break;
				}
				ArrayList<TGenealogyMap> data = new ArrayList<TGenealogyMap>();
				data.add(genealogyMap);
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
	 * 删除家谱映射
	 * @param userId 用户id
	 * @param genealogyId 家谱id
	 * @return 删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/genealogyMap
	 */
	@RequestMapping(value = "/genealogyMap", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteGenealogyMap(@RequestParam(value="userId", required=true) long userId, @RequestParam(value="genealogyId", required=true) long genealogyId){
		logger.info(String.format("delete genealogyMap of %d, genealogy id %d", userId, genealogyId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteGenealogyMap(userId,genealogyId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 添加家庭信息
	 * @param request 建议将com.findu.model.TFamily转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TFamily对象。
	 * 访问地址：post     /findu/v2_1/family
	 */
	@RequestMapping(value = "/family", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertFamily(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TFamily family = DataUtils.Json2Object(json, TFamily.class);
				if(family == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				if(!checkHouseHolder(family)){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("check house holder error");
					break;
				}
				logger.info(String.format("insert family of %d, genealogy id %d", family.getFamilyId(), family.getGenealogyId()));
				int res = genealogyService.insertFamily(family);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert family error");
					break;
				}
				ArrayList<TFamily> data = new ArrayList<TFamily>();
				data.add(family);
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
	 * 检查是否是主家
	 * @param family 家庭实体
	 * @return true or false
	 */
	private boolean checkHouseHolder(TFamily family){
		if(family.getHouseholder() != null && family.getHouseholder() > 0){
			long id = family.getHouseholder();
			if(id != family.getFatherId() && id != family.getMotherId()){
				logger.warn(String.format("family householder %d is error", id));
				return false;
			}
		}
		return true;
	}

	/**
	 * 删除家庭信息
	 * @param id 家庭id
	 * @param genealogyId 家谱id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/family
	 */
	@RequestMapping(value = "/family", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteFamily(@RequestParam(value="id", required=true) long id, @RequestParam(value="genealogyId", required=true) long genealogyId){
		logger.info(String.format("delete family of %d, genealogy id %d", id, genealogyId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteFamily(id, genealogyId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家庭成员的其余信息
	 * @param id 家庭成员其余信息id
	 * @param memberId 家庭成员id
	 * @return 查询成功code = 200，实体是TMemberRight。
	 * 访问地址：get     /findu/v2_1/member/right
	 */
	@RequestMapping(value = "/member/right", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetMemberRight(@RequestParam(value="id", required=true) long id, @RequestParam(value="memberId", required=true) long memberId){
		logger.info(String.format("get memberRight of %d, genealogy id %d", id, memberId));
		FUResult result = new FUResult();
		TMemberRight memberRight = genealogyService.getMemberRight(id,memberId);
		if(memberRight != null){
			ArrayList<TMemberRight> data = new ArrayList<TMemberRight>();
			data.add(memberRight);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 修改家庭成员的其余信息
	 * @param request 建议将com.findu.model.TMemberRight转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/member/right
	 */
	@RequestMapping(value = "/member/right", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateMemberRight(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMemberRight memberRight = DataUtils.Json2Object(json, TMemberRight.class);
				if(memberRight == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update memberRight of %d, genealogy id %d", memberRight.getId(), memberRight.getMemberId()));
				int code = genealogyService.updateMemberRight(memberRight);
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
	 * 添加家庭成员的其余信息
	 * @param request 建议将com.findu.model.TMemberRight转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TMemberRight对象。
	 * 访问地址：post     /findu/v2_1/member/right
	 */
	@RequestMapping(value = "/member/right", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertMemberRight(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMemberRight memberRight = DataUtils.Json2Object(json, TMemberRight.class);
				if(memberRight == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert memberRight of %d, genealogy id %d", memberRight.getId(), memberRight.getMemberId()));
				int res = genealogyService.insertMemberRight(memberRight);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert member right error");
					break;
				}
				ArrayList<TMemberRight> data = new ArrayList<TMemberRight>();
				data.add(memberRight);
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
	 * 删除家庭成员的其余信息
	 * @param id 家庭成员的其余信息id
	 * @param memberId 家庭成员id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/member/right
	 */
	@RequestMapping(value = "/member/right", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteMemberRight(@RequestParam(value="id", required=true) long id, @RequestParam(value="memberId", required=true) long memberId){
		logger.info(String.format("delete memberRight of %d, genealogy id %d", id, memberId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.deleteMemberRight(id, memberId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询家谱列表
	 * @param userId 需要传入userId
	 * @return 查询成功code = 200，返回TGenealogy列表。
	 * 访问地址：get     /findu/v2_1/genealogies
	 */
	@RequestMapping(value = "/genealogies", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetGenealogies(@RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get genealogies of user id %d", userId));
		FUResult result = new FUResult();
		ArrayList<TGenealogyMap> genealogyMaps = genealogyService.listGenealogyMap(userId);
		if(genealogyMaps != null && genealogyMaps.size() > 0){
			ArrayList<TGenealogy> data = genealogyService.listGenealogy(genealogyMaps);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 查询家谱树
	 * @param genealogyId	家谱id
	 * @param userId 家谱创建这id
	 * @param centerId	缺省的中心成员id
	 * @param up	向上查找多少代，共有家谱有效
	 * @param down	向下查找多少代，共有家谱有效
	 * @return  返回家谱树结果集，实体是FUFamilyTree。
	 * 访问地址：get	/findu/v2_1/member/tree
	 */
	@RequestMapping(value = "/member/tree", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetMemberTree(@RequestParam(value="userId", required=true) long userId, @RequestParam(value="genealogyId", required=true) long genealogyId, @RequestParam(value="centerId", required=true) long centerId, @RequestParam(value="up", required=true) int up, @RequestParam(value="down", required=true) int down){
		logger.info(String.format("get GetMemberTree of genealogy id %d", genealogyId));
		FUResult result = new FUResult();
		FUFamilyTree fuFamily = genealogyService.getMemberTree(genealogyId,userId,centerId,up,down,result);
		if(fuFamily != null){
			ArrayList<FUFamilyTree> data = new ArrayList<FUFamilyTree>();
			data.add(fuFamily);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}
		return result;
	}

	/**
	 * 创建一个家谱图，自动设置映射关系和中心成员，如果没有中心成员则自动选择或创建一个成员。自动创建家庭，以中心成员作为孩子。
	 * @param request 建议将com.findu.model.TGenealogy转为json进行传输，但不传递id和createTime。
	 * @return 添加成功 code = 200，返回TGenealogy对象。
	 * 访问地址：post     /findu/v2_1/genealogy/create
	 */
	@RequestMapping(value = "/genealogy/create", method = RequestMethod.POST)
	@ResponseBody
	public FUResult CreateGenealogy(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TGenealogy genealogy = DataUtils.Json2Object(json, TGenealogy.class);
				if(genealogy == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("create genealogy of %d, creator id %d", genealogy.getGenealogyId(), genealogy.getCreatorId()));
				int res = genealogyService.createGenealogy(genealogy);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert genealogy error");
					break;
				}
				ArrayList<TGenealogy> data = new ArrayList<TGenealogy>();
				data.add(genealogy);
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
	 * 删除一个家谱，包括家谱映射和家庭关系。
	 * @param id 家谱id
	 * @param userId 家谱创建人id
	 * @return 删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/genealogy/all
	 */
	@RequestMapping(value = "/genealogy/all", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult RemoveGenealogy(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("delete genealogy of %d, creator id %d", id, userId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.removeGenealogy(id, userId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 关联家庭成员和注册用户
	 * @param request 建议将com.findu.model.TMember转为json进行传输。
	 * @return 添加成功code = 200，返回FUUser对象。
	 * 访问地址：put     /findu/v2_1/member/user
	 */
	@RequestMapping(value = "/member/user", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult AttachMemberUser(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TMember member = DataUtils.Json2Object(json, TMember.class);
				if(member == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("attach member of %d, %s, %s", member.getMemberId(), member.getSurname(), member.getName()));
				FUUser user = genealogyService.attachMemberUser(member);
				if(user == null){
					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
					logger.warn("attach member error");
					break;
				}
				ArrayList<FUUser> data = new ArrayList<FUUser>();
				data.add(user);
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
	 * 删除一个家庭成员的所有数据，包括基本信息、扩展信息，以及权限等。
	 * @param id 家族成员id
	 * @param creatorId  创建家族成员的id  主要用于分表查询
	 * @return 删除成功 code = 200，不返回对象。
	 * 访问地址：delete     /findu/v2_1/member/all
	 */
	@RequestMapping(value = "/member/all", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult RemoveMember(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("delete member of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		try {
			int code = genealogyService.removeMember(id, creatorId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/member/timeline/list", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetTimelineList(@RequestParam(value="memberId", required=true) long memberId, 
			@RequestParam(value="creatorId", required=true)long creatorId,
			@RequestParam(value="pageCount", required=true) int pageCount, 
			@RequestParam(value="pageNumber", required=true) int pageNumber, 
			@RequestParam(value="pageSize", required=true) int pageSize){
		logger.info(String.format("获取成员时光轴list member of %d, creator id %d", memberId, creatorId));
		FUResult result = new FUResult();
		do {
			if(pageCount < 0 || pageNumber < 0  || pageNumber > pageCount || pageSize <= 0){
				logger.warn("page parameter is error");
				result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
				break;
			}
			FUSearchPage page = new FUSearchPage();
			page.setPageCount(pageCount);
			page.setPageNumber(pageNumber);
			page.setPageSize(pageSize);	
			try {
				int code = socializingService.listSubject(page, result, memberId);
				if(!ErrorCodeUtil.isSucc(code)){
					logger.warn("list user error");
				}
				result.setCode(code);
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
			}
		} while (false);
		return result;
	}
	
	/**
	 * 添加时光轴
	 * @param request 建议将 com.findu.dto.FUMemberTimeline 转为json进行传输，但不传递timelineId和createTime。
	 * @return 添加成功code = 200，返回FUMemberTimeline对象。
	 */
	@RequestMapping(value = "/member/timeline", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertTimeline(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				FUMemberTimeline timeline = DataUtils.Json2Object(json, FUMemberTimeline.class);
				if(timeline == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				/**
				 * 拼接内容存入数据库<br/>
				 * 格式：{"type":0,"time":"","address":"","content":""}
				 */
				String content = "{\"type\":" + timeline.getType() + ",\"time\":\"" + timeline.getTime()
						+ "\",\"address\":\"" + timeline.getAddress() + "\",\"content\":\"" + timeline.getContent()
						+ "\"}";
				timeline.setContent(content);
				TMember member = genealogyService.getMember(timeline.getMemberId(), timeline.getCreatorId());
				if (member==null) {
					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
					logger.warn("成员不存在");
					break;
				}
				Date date = new Date();

				TTopic topic= socializingService.getTopic(timeline.getCreatorId(), TTopic.TYPE_USER_SPACE);

				if (topic==null) {
					topic = new TTopic();
					topic.setCreateTime(date);
					topic.setTitle("成员时光轴");
					topic.setType(TTopic.TYPE_USER_SPACE);
					topic.setRefId(member.getMemberId());
					topic.setRefTableSeq(member.getCreatorId());
					int code = socializingService.insertTopic(topic);
					if (code!=200) {
						result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
						logger.warn("添加topic失败");
						break;
					}
				}

				TSubject subject = new TSubject();
				subject.setTopicId(topic.getTopicId());
				subject.setTitle(timeline.getTitle());
				subject.setContent(timeline.getContent());
				subject.setCreateTime(date);
				subject.setContentType(1);
				subject.setRefId(topic.getRefId());
				subject.setUserId(topic.getRefTableSeq());
				int code = socializingService.insertSubject(subject);
				if (code!=200) {
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
					logger.warn("添加topic失败");
					break;
				}
				timeline = new FUMemberTimeline(subject);
				ArrayList<FUMemberTimeline> data = new ArrayList<FUMemberTimeline>();
				data.add(timeline);
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
	 * 删除时间轴
	 * @param id
	 * @param creatorId
	 * @return
	 */
	@RequestMapping(value = "/member/timeline", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteTimeline(@RequestParam(value="id", required=true) long id, @RequestParam(value="creatorId", required=true) long creatorId){
		logger.info(String.format("delete member of %d, creator id %d", id, creatorId));
		FUResult result = new FUResult();
		return result;
	}
	
	
	/**
	 * 查询家庭信息
	 * 
	 * @param request
	 *            建议将com.findu.model.TFamily转为json进行传输
	 *            ，需要motherId或fatherId或childId其中的两个条件。
	 * 
	 * @return 查询成功code = 200，实体是TFamily。 访问地址：post /findu/v2_1/getfamily
	 * 
	 * @author Hu Xiaobo
	 */
	@RequestMapping(value = "/getfamily", method = RequestMethod.POST)
	@ResponseBody
	public FUResult GetFamily(HttpServletRequest request) {
		String json = NetUtils.getHttpRequestBody(request).trim();
		FUResult result = new FUResult();
		if (json == null || json.length() <= 0) {
			result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
			logger.warn("can not get request body");
			return result;
		}
		TFamily family = DataUtils.Json2Object(json, TFamily.class);
		if (family == null) {
			result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
			logger.warn("json object is error");
			return result;
		}
		List<TFamily> dbFamilies = genealogyService.getFamilies(family);
		ArrayList<TFamily> data = new ArrayList<TFamily>();
		if (!dbFamilies.isEmpty()) {
			for (TFamily dbFamily : dbFamilies) {
				data.add(dbFamily);
			}
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} else {
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}

	/**
	 * 获取单个时光轴
	 * 
	 * @param id
	 *            t_subject表主键
	 * @return 查询成功code = 200，实体是TSubject。 访问地址：get /findu/v2_1/member/timeline
	 * 
	 * @author Hu Xiaobo
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/member/timeline", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetTimeline(@RequestParam(value = "subjectId", required = true) long subjectId,
			@RequestParam(value = "refId", required = true) long refId) {
		logger.info(String.format("获取成员时光轴 subject id of %d,member id of %d", subjectId, refId));
		FUResult result = new FUResult();
		TSubject subject = socializingService.getSubject(subjectId, refId);
		if (subject != null) {
			ArrayList<TSubject> data = new ArrayList<TSubject>();
			/**
			 * 处理内容回显，时间、类型、地点<br/>
			 * 格式：{"type":0,"time":"","address":"","content":""}
			 */
			String content = subject.getContent();
			if(content.indexOf("type")!=-1){
				Map<String, Object> map = JSONObject.fromObject(content);
				subject.setType((Integer) (map.get("type")));
				subject.setTime((String) (map.get("time")));
				subject.setAddress((String) (map.get("address")));
				subject.setContent((String) (map.get("content")));
			}else{
				subject.setType(0);
				subject.setTime("");
				subject.setAddress("");
			}
			data.add(subject);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} else {
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}
	
	/**
	 * 修改时间轴
	 * @param request 建议将com.findu.model.TSubject转为json进行传输
	 * @return 修改成功code = 200
	 * @author Hu Xiaobo
	 */
	@RequestMapping(value = "/member/timeline", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateTimeline(HttpServletRequest request) {
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				FUMemberTimeline timeline = DataUtils.Json2Object(json, FUMemberTimeline.class);
				if(timeline == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				/**
				 * 拼接内容存入数据库<br/>
				 * 格式：{"type":0,"time":"","address":"","content":""}
				 */
				String content = "{\"type\":" + timeline.getType() + ",\"time\":\"" + timeline.getTime()
						+ "\",\"address\":\"" + timeline.getAddress() + "\",\"content\":\"" + timeline.getContent()
						+ "\"}";
				timeline.setContent(content);

				TSubject subject = socializingService.getSubject(timeline.getTimelineId(), timeline.getMemberId());
				if(subject!=null){
					subject.setTitle(timeline.getTitle());
					subject.setContent(timeline.getContent());
					subject.setContentType(1);
					subject.setContent(content);
					int code = socializingService.updateSubject(subject);
					if (code!=200) {
						result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
						logger.warn("修改topic失败");
						break;
					}
					result.setCode(code);
				}else{
					result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
				}
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}
	
	/**
	 * 查询家庭成员
	 * @param genealogyId  家谱编号
	 * @param genealogyCreatorId  家谱创建编号
	 * @param memberId  家庭成员编号编号
	 * @param creatorId  创建家庭成员的id  主要用于分表查询
	 * @return  查询结果存在 code = 200，实体是TFamily。
	 * @author Hu Xiaobo
	 * 访问地址：get     /findu/v2_1/familymember
	 */
	@RequestMapping(value = "/familymember", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetFamilyMember(@RequestParam(value="genealogyId", required=true) long genealogyId,@RequestParam(value="genealogyCreatorId", required=true) long genealogyCreatorId,@RequestParam(value="memberId", required=true) long memberId){
		logger.info(String.format("get genealogy of %d, creator id %d,get member of %d", genealogyId, genealogyCreatorId, memberId));
		FUResult result = new FUResult();
		FUFamilyMember familyMember = genealogyService.getFamilyMember(genealogyId, genealogyCreatorId, memberId);
		if(familyMember == null){
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
			
			ArrayList<FUFamilyMember> familyMembers = new ArrayList<FUFamilyMember>();
			
			familyMembers.add(familyMember);
			
			result.setData(familyMembers);
		}

		return result;
	}
	
	/**
	 * 修改家庭信息
	 * @param request 建议将com.findu.model.TFamily数组转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put     /findu/v2_1/family
	 * @author Hu Xiaobo
	 */
	@RequestMapping(value = "/family", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateFamily(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				JSONArray jsonarray = JSONArray.fromObject(json); 
				 @SuppressWarnings("unchecked")
				List<TFamily> families = (List<TFamily>)JSONArray.toCollection(jsonarray, TFamily.class);
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				for (TFamily family : families) {
					if(!checkHouseHolder(family)){
						result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
						logger.warn("check house holder error");
						break;
					}
					logger.info(String.format("update family of %d, genealogy id %d", family.getFamilyId(), family.getGenealogyId()));
					int code = genealogyService.updateFamily(family);
					result.setCode(code);
				}
			} catch (Exception e) {
				result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
				logger.error(e.getMessage());
				break;
			}
		} while (false);
		return result;
	}
}
