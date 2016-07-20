package com.findu.dto;

import java.util.ArrayList;

import com.findu.utils.ErrorCodeUtil;

public class FUResult {
	/**返回错误编码*/
	private int code = 0;
	/**搜索结果总页数*/
	private int pageCount = 0;
	/**结果集*/
	private ArrayList<? extends Object> data = null;
	/**错误原因*/
	private String reason;
	
	/**
	 * 错误码，成功是200。
	 * @return 错误码。
	 */
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
		this.reason = ErrorCodeUtil.getErrorCodeReason(code);
	}
	
	/**
	 * 结果集总页数，0无效。
	 * @return 结果集总页数。
	 */
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	/**
	 * 结果集数组。
	 * @return 结果集数组。
	 */
	public ArrayList<? extends Object> getData() {
		return data;
	}
	
	public void setData(ArrayList<? extends Object> data) {
		this.data = data;
	}

	/**
	 * 失败原因。
	 * @return 失败原因。
	 */
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
