package com.findu.dto;

import com.findu.model.TFamily;

/**
 * 母亲
 * @author ll
 *
 */
public class FUMother extends FUParent {
	/**
	 * 丈夫
	 */
	private Long husbandId = null;
	
	public FUMother(TFamily family){
		setHusbandId(family.getFatherId());
		if(family.getMotherId().equals(family.getHouseholder()))
			setHouseholder(true);
	}

	public Long getHusbandId() {
		return husbandId;
	}

	public void setHusbandId(Long husbandId) {
		this.husbandId = husbandId;
	}
}
