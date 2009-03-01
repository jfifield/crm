package org.programmerplanet.crm.model;

import java.util.UUID;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionListItem extends Entity {

	private UUID optionListId;
	private String value;
	private Integer viewIndex;

	public void setOptionListId(UUID optionListId) {
		this.optionListId = optionListId;
	}

	public UUID getOptionListId() {
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
