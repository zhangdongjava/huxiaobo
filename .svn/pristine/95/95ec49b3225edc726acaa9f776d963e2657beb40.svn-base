package com.findu.dto;

import java.util.Date;

public class FUPayment {
	/** 发起交易的用户的资产id */
	private long wealthId = 0;
	/** 发起交易的用户 */
	private long userId = 0;
	/** 交易目的用户id，冲值、提现等交易需要填 */
	private long dstUserId = 0;
	/** 交易目的用户的资产id，为0则将选择同类型资产中的第一个 */
	private long dstWealthId = 0;
	/** 目的用户帐号，转帐类型交易填写 */
	private String dstAccount = null;
	/** 目的用户手机号码，转帐类型交易填写 */
	private String dstPhone = null;
	/** 交易类型，1 充值，2 充值赠送，3 发红包，4 提现*/
	private int type = 0;
	/** 交易金额 */
	private long value = 0;
	/** 目的资产有效期 */
	private Date validateTime = null;

	public long getWealthId() {
		return wealthId;
	}

	public void setWealthId(long wealthId) {
		this.wealthId = wealthId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getDstUserId() {
		return dstUserId;
	}

	public void setDstUserId(long dstUserId) {
		this.dstUserId = dstUserId;
	}
	
	public long getDstWealthId() {
		return dstWealthId;
	}

	public void setDstWealthId(long dstWealthId) {
		this.dstWealthId = dstWealthId;
	}

	public String getDstAccount() {
		return dstAccount;
	}

	public void setDstAccount(String dstAccount) {
		this.dstAccount = dstAccount;
	}

	public String getDstPhone() {
		return dstPhone;
	}

	public void setDstPhone(String dstPhone) {
		this.dstPhone = dstPhone;
	}

	/**
	 * 返回支付类型。
	 * @return  1 充值，2 赠送，3 发红包，4 提现
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	/**
	 * 返回目的用户支付有效期，如果不为空则将创建identity有效的临时资产。
	 * @return  资产有效期。
	 */
	public Date getValidateTime() {
		return validateTime;
	}

	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}
}
