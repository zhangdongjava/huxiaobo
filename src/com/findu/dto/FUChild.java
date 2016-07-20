package com.findu.dto;

/**
 * 孩子
 * @author ll
 *
 */
public class FUChild extends FUPerson {
	
	/**父亲*/
	private Long fatherId = null;
	/**母亲*/
	private Long motherId = null;
	
	public Long getFatherId() {
		return fatherId;
	}
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}
	public Long getMotherId() {
		return motherId;
	}
	public void setMotherId(Long motherId) {
		this.motherId = motherId;
	}
}
