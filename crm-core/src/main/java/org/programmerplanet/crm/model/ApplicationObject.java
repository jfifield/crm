package org.programmerplanet.crm.model;

import java.util.UUID;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ApplicationObject {

	private UUID applicationId;
	private UUID objectId;
	private Integer viewIndex;

	public UUID getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(UUID applicationId) {
		this.applicationId = applicationId;
	}

	public UUID getObjectId() {
		return objectId;
	}

	public void setObjectId(UUID objectId) {
		this.objectId = objectId;
	}

	public Integer getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ApplicationObject) {
			ApplicationObject other = (ApplicationObject)obj;
			if (this.applicationId == null && other.applicationId == null || this.objectId == null && other.objectId == null) {
				return super.equals(obj);
			}
			else if (this.applicationId == null || other.applicationId == null || this.objectId == null || other.objectId == null) {
				return false;
			}
			else {
				return this.applicationId.equals(other.applicationId) && this.objectId.equals(other.objectId);
			}
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		if (this.applicationId != null && this.objectId != null) {
			return this.applicationId.hashCode() + this.objectId.hashCode();
		}
		else {
			return super.hashCode();
		}
	}

	public String toString() {
		return this.getClass().getName() + (this.applicationId != null && this.objectId != null ? ": " + this.applicationId.toString() + "/" + this.objectId.toString() : "");
	}

}
