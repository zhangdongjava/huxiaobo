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

import com.findu.dto.FUPayment;
import com.findu.dto.FUResult;
import com.findu.model.TBill;
import com.findu.model.TWealth;
import com.findu.service.BillService;
import com.findu.utils.DataUtils;
import com.findu.utils.DateUtils;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.NetUtils;
import com.findu.utils.SysParamUtils;

/**
 * 支付模块接口，通过AuthInterceptor做消息鉴权。
 */
@Controller
@RequestMapping(AuthInterceptor.BASE_PATH)
public class BillController {

	private static Logger logger = Logger.getLogger(BillController.class);
	
	@Autowired
	private BillService billService;
	
	/**
	 * 查询资产
	 * @param id 资产id
	 * @param userId 用户id
	 * @return 查询成功code = 200，实体是TWealth。
	 * 访问地址：get   /findu/v2_1/wealth
	 */
	@RequestMapping(value = "/wealth", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetWealth(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get wealth of %d, user id %d", id, userId));
		FUResult result = new FUResult();
		TWealth wealth = billService.getWealth(id,userId);
		if(wealth != null){
			ArrayList<TWealth> data = new ArrayList<TWealth>();
			data.add(wealth);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}
	
	/**
	 * 修改资产
	 * @param request 建议将com.findu.model.TWealth转为json进行传输
	 * @return 修改成功code = 200，不返回对象。
	 * 访问地址：put   /findu/v2_1/wealth
	 */
	@RequestMapping(value = "/wealth", method = RequestMethod.PUT)
	@ResponseBody
	public FUResult UpdateWealth(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TWealth wealth = DataUtils.Json2Object(json, TWealth.class);
				if(wealth == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("update wealth of %d, user id %d", wealth.getId(), wealth.getUserId()));
				int code = billService.updateWealth(wealth);
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
	 * 添加资产
	 * @param request 建议将com.findu.model.TWealth转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TWealth对象。
	 * 访问地址：post   /findu/v2_1/wealth
	 */
	@RequestMapping(value = "/wealth", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertWealth(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TWealth wealth = DataUtils.Json2Object(json, TWealth.class);
				if(wealth == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert wealth of %d, user id %d", wealth.getId(), wealth.getUserId()));
				int res = billService.insertWealth(wealth);
				if(ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert wealth error");
					break;
				}
				ArrayList<TWealth> data = new ArrayList<TWealth>();
				data.add(wealth);
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
	 * 删除资产
	 * @param id 资产id
	 * @param userId 用户id
	 * @return 删除成功code = 200，不返回对象。
	 * 访问地址：delete   /findu/v2_1/wealth
	 */
	@RequestMapping(value = "/wealth", method = RequestMethod.DELETE)
	@ResponseBody
	public FUResult DeleteWealth(@RequestParam(value="id", required=true) long id, @RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("delete wealth of %d, user id %d", id, userId));
		FUResult result = new FUResult();
		try {
			int code = billService.deleteWealth(id, userId);
			result.setCode(code);
		} catch (Exception e) {
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询账单
	 * @param id 账单id
	 * @param createTime 创建时间
	 * @return 查询成功code = 200，实体是TBill。
	 * 访问地址：get   /findu/v2_1/bill
	 */
	@RequestMapping(value = "/bill", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetBill(@RequestParam(value="id", required=true) long id, @RequestParam(value="createTime", required=true) Date createTime){
		logger.info(String.format("get bill of %d, createTime %tc", id, createTime));
		FUResult result = new FUResult();
		TBill bill =	billService.getBill(id,createTime);
		if(bill != null){
			ArrayList<TBill> data = new ArrayList<TBill>();
			data.add(bill);
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		}else{
			result.setCode(ErrorCodeUtil.ERROR_NOT_EXIST);
		}
		return result;
	}
	
	/**
	 * 添加账单
	 * @param request 建议将com.findu.model.TBill转为json进行传输，但不传递id和createTime。
	 * @return 添加成功code = 200，返回TBill对象。
	 * 访问地址：post   /findu/v2_1/bill
	 */
	@RequestMapping(value = "/bill", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertBill(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				TBill bill = DataUtils.Json2Object(json, TBill.class);
				if(bill == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert bill of %d, createTime %tc", bill.getBillId(), bill.getCreateTime()));
				int res = billService.insertBill(bill);
				if(!ErrorCodeUtil.isSucc(res)){
					result.setCode(res);
					logger.warn("insert bill error");
					break;
				}
				ArrayList<TBill> data = new ArrayList<TBill>();
				data.add(bill);
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
	 * 查询用户所有的资产
	 * @param userId 用户id
	 * @return 查询成功code = 200，返回TWealth列表。
	 * 访问地址：get   /findu/v2_1/wealth/list
	 */
	@RequestMapping(value = "/wealth/list", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetWealthList(@RequestParam(value="userId", required=true) long userId){
		logger.info(String.format("get wealths of user id %d", userId));
		FUResult result = new FUResult();
		ArrayList<TWealth> data = billService.listWealth(userId, 0);
		result.setData(data);
		result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		return result;
	}
	
	/**
	 * 查询所有的账单
	 * @param userId 用户id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 查询成功code = 200，返回TBill列表
	 * 访问地址：get   /findu/v2_1/bill/list
	 */
	@RequestMapping(value = "/bill/list", method = RequestMethod.GET)
	@ResponseBody
	public FUResult GetBillList(@RequestParam(value="userId", required=true) long userId, @RequestParam(value="startTime", required=true) String startTime, @RequestParam(value="endTime", required=true) String endTime){
		logger.info(String.format("get wealths of user id %d", userId));
		FUResult result = null;
		try {
			result = new FUResult();
			Date start = DateUtils.parseStrDate(null, startTime);
			Date end = DateUtils.parseStrDate(null, endTime);
			if(start.compareTo(end) > 0 
					|| DateUtils.getIntervalSeconds(end, start) / DateUtils.SecondsOfDay > Integer.parseInt(SysParamUtils.QUERY_DAYS_MAX)){
				logger.warn(String.format("query from start time %s to end time %s is error"
						, startTime, endTime));
				result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
				return result;
			}
			ArrayList<TBill> data;
			try {
				data = billService.listBill(userId, start, end);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.getMessage());
				result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
				return result;
			}
			result.setData(data);
			result.setCode(ErrorCodeUtil.ERROR_SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
			return result;
		}
		return result;
	}
	
	/**
	 * 支付，包括冲值、转帐、提现等
	 * @param request 建议将com.findu.dto.FUPayment转为json进行传输。
	 * @return 添加成功code = 200，返回TBill对象。
	 * 访问地址：post   /findu/v2_1/payment
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	@ResponseBody
	public FUResult InsertPayment(HttpServletRequest request){
		FUResult result = new FUResult();
		do {
			try {
				String json = NetUtils.getHttpRequestBody(request).trim();
				if(json == null || json.length() <= 0){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("can not get request body");
					break;
				}
				FUPayment payment = DataUtils.Json2Object(json, FUPayment.class);
				if(payment == null){
					result.setCode(ErrorCodeUtil.ERROR_REQUEST_PARAMETER);
					logger.warn("json object is error");
					break;
				}
				logger.info(String.format("insert payment from (%d, %d) to [%d, %s, %s]"
						, payment.getUserId(), payment.getWealthId(), payment.getDstUserId(), payment.getDstAccount(), payment.getDstPhone()));
				TBill bill = billService.insertPayment(payment);
				if(bill == null){
					result.setCode(ErrorCodeUtil.ERROR_SYSTEM);
					logger.warn("pay error");
					break;
				}
				ArrayList<TBill> data = new ArrayList<TBill>();
				data.add(bill);
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
}
