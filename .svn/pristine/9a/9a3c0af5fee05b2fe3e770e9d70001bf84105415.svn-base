package com.findu.service;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findu.dao.TBillDao;
import com.findu.dao.TUserDao;
import com.findu.dao.TWealthDao;
import com.findu.dto.FUPayment;
import com.findu.model.TBill;
import com.findu.model.TUser;
import com.findu.model.TWealth;
import com.findu.utils.DataUtils;
import com.findu.utils.DateUtils;
import com.findu.utils.ErrorCodeUtil;

@Service
public class BillService {
	
	private static Logger logger = Logger.getLogger(BillService.class);

	@Autowired
	private TWealthDao wealthDao;
	
	@Autowired
	private TUserDao userDao;
	
	@Autowired
	private TBillDao billDao;
	
	public TWealth getWealth(long id, long userId) {
		TWealth wealth = new TWealth();
		wealth.setId(id);
		wealth.setUserId(userId);
		return wealthDao.get(wealth, id);
	}
	
	public int updateWealth(TWealth wealth) {
		if(!wealthDao.update(wealth)){
			logger.warn("update wealth error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteWealth(long id,long userId) {
		TWealth wealth = new TWealth();
		wealth.setId(id);
		wealth.setUserId(userId);
		if(!wealthDao.delete(wealth)){
			logger.warn("delete wealth error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertWealth(TWealth wealth) {
		if(wealth.getCreateTime() == null)
			wealth.setCreateTime(new Date());
		if(wealthDao.insert(wealth) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TBill getBill(long billId, Date createTime) {
		TBill bill = new TBill();
		bill.setBillId(billId);
		bill.setCreateTime(createTime);
		return billDao.get(bill, billId);
	}

	public int insertBill(TBill bill) {
		if(bill.getCreateTime() == null)
			bill.setCreateTime(new Date());
		if(billDao.insert(bill) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public ArrayList<TWealth> listWealth(long userId, int type) {
		TWealth wealth = new TWealth();
		wealth.setUserId(userId);
		wealth.setType(type);
		return wealthDao.search(wealth, true, null, false, null);
	}

	public ArrayList<TBill> listBill(long userId, Date startTime, Date endTime) {
		TBill bill = new TBill();
		bill.setSrcUserId(userId);
		return billDao.list(bill, startTime, endTime);
	}
	
	public TBill insertPayment(FUPayment payment){
		Date now = new Date();
		TBill bill = null;
		TUser user = new TUser();
		user.setUserId(payment.getUserId());
		user = userDao.get(user, payment.getUserId());
		if(user == null){
			logger.warn(String.format("can not get user of %d", payment.getUserId()));
			return null;
		}
		TWealth wealth = getWealth(payment.getWealthId(), payment.getUserId());
		if(wealth == null){
			logger.warn(String.format("can not get wealth %d of user %d", payment.getWealthId(), payment.getUserId()));
			return null;
		}
		if(wealth.getValidateTime() != null && wealth.getValidateTime().compareTo(new Date()) < 0){
			logger.warn(String.format("source wealth %d of user %d is out of date"
					, payment.getWealthId(), payment.getUserId()));
			return null;
		}
		if(wealth.getValue() - payment.getValue() < 0){
			logger.warn(String.format("source wealth %d of user %d is less than value %d"
					, payment.getWealthId(), payment.getUserId(), payment.getValue()));
			return null;
		}
		TUser dstUser = null;
		if(user.getUserId() == payment.getDstUserId()){
			dstUser = user;
		}else{
			if(payment.getDstUserId() > 0){
				dstUser = new TUser();
				dstUser.setUserId(payment.getDstUserId());
				dstUser = userDao.get(dstUser, payment.getDstUserId());
			}
			if(dstUser == null){
				dstUser = new TUser();
				dstUser.setAccount(payment.getDstAccount());
				dstUser.setPhone(payment.getDstAccount());
				dstUser = userDao.find(dstUser, false);
				if(dstUser == null){
					logger.warn(String.format("can not find dest user [%d, %s, %s]"
							, payment.getDstUserId(), payment.getDstAccount(), payment.getDstPhone()));
					return null;
				}	
			}
		}
		
		
		if(user.getUserId() != dstUser.getUserId() && payment.getValue() < 0){
			logger.warn(String.format("do not support get monty from other user, value %d", payment.getValue()));
			return null;
		}
		
		Session session = wealthDao.getSession(wealth);
		Transaction tran = session.beginTransaction();
		try{
			wealth.setValue(wealth.getValue() - payment.getValue());
			if(!wealthDao.update(session, wealth)){
				logger.warn("update weath error");
				tran.rollback();
				return null;
			}
			
			TWealth dstWealth = null;
			if(user.getUserId() == dstUser.getUserId()){
				if(wealth.getId() == payment.getDstWealthId()){
					dstWealth = wealth;
				}else if(payment.getDstWealthId() > 0){
					dstWealth = new TWealth();
					dstWealth.setId(payment.getDstWealthId());
					dstWealth = wealthDao.get(session, dstWealth, payment.getDstWealthId());
					if(dstWealth == null){
						logger.warn(String.format("user %d has no wealth of %d", dstUser.getUserId(), payment.getDstWealthId()));
						tran.rollback();
						return null;
					}
				}
				else{
					logger.warn("need dest wealth id");
					tran.rollback();
					return null;
				}
			}else{
				dstWealth = null;
				if(payment.getValidateTime() != null){
					logger.info(String.format("create temprary wealth of type %d for user %d"
							, payment.getType(), dstUser.getUserId()));
					dstWealth = new TWealth();
					dstWealth.setUserId(dstUser.getUserId());
					dstWealth.setType(payment.getType());
					dstWealth.setIdentity(DataUtils.MD5(DateUtils.formatDate(null, payment.getValidateTime())));
					dstWealth.setValue(payment.getValue());
					dstWealth.setValidateTime(payment.getValidateTime());
					dstWealth.setCreateTime(now);
					if(wealthDao.insert(session, dstWealth) == null){
						logger.warn(String.format("insert temp wealth for user %d error", dstUser.getUserId()));
						tran.rollback();
						return null;
					}
				}else{
					dstWealth = new TWealth();
					dstWealth.setUserId(dstUser.getUserId());
					dstWealth.setType(wealth.getType());
					dstWealth = wealthDao.find(session, dstWealth, true);
					if(dstWealth == null){
						logger.warn(String.format("can not find one wealth of type %d of user %d", wealth.getType(), dstUser.getUserId()));
						tran.rollback();
						return null;
					}

					dstWealth.setValue(dstWealth.getValue() + payment.getValue());
					if(!wealthDao.update(session, dstWealth)){
						logger.warn(String.format("update wealth %d of user %d error", dstWealth.getId(), dstWealth.getUserId()));
						tran.rollback();
						return null;
					}
				}
			}			
			
			bill = new TBill();
			bill.setSrcUserId(user.getUserId());
			bill.setSrcWealthId(wealth.getId());
			bill.setDstUserId(dstUser.getUserId());
			bill.setDstWealthId(dstWealth.getId());
			bill.setType(payment.getType());
			bill.setValue(payment.getValue());
			bill.setCreateTime(now);
			if(billDao.insert(session, bill) == null){
				logger.warn("insert bill error");
				tran.rollback();
				return null;
			}
			
			tran.commit();
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			tran.rollback();
			return null;
		}
		
		return bill;
	}

}
