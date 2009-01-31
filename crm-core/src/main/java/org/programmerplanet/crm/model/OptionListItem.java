package org.programmerplanet.crm.model;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionListItem extends Entity {

	private Long optionListId;
	private String value;
	private Integer viewIndex;

	public void setOptionListId(Long optionListId) {
		this.optionListId = optionListId;
	}

	public Long getOptionListId() {
		return optionListId;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Integer getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}

}
