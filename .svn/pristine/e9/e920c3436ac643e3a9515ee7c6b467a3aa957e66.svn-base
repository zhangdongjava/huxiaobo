package com.findu.dto;

import com.findu.model.TFamily;

/**
 * 丈夫
 * @author ll
 *
 */
public class FUFather extends FUParent {
	
	/**
	 * 妻子
	 */
	private Long wifeId = null;
	
	public FUFather(TFamily family){
		setWifeId(family.getMotherId());
		if(family.getFatherId().equals(family.getHouseholder()))
			setHouseholder(true);
	}

	public Long getWifeId() {
		return wifeId;
	}

	public void setWifeId(Long wifeId) {
		this.wifeId = wifeId;
	}
}
