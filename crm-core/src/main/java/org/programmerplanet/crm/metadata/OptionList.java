package org.programmerplanet.crm.metadata;

import org.programmerplanet.crm.BaseReferenceable;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionList extends BaseReferenceable {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
