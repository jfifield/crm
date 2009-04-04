package org.programmerplanet.crm.metadata;

import org.programmerplanet.crm.BaseReferenceable;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class Application extends BaseReferenceable {

	private String applicationName;
	private Integer viewIndex;

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}

	public Integer getViewIndex() {
		return viewIndex;
	}

}
