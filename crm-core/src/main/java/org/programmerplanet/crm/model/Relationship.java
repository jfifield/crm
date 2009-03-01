package org.programmerplanet.crm.model;

import java.util.UUID;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class Relationship extends Entity {

	private UUID parentObjectId;
	private UUID childObjectId;
	private Integer viewIndex;
	private String tableName;

	public UUID getParentObjectId() {
		return parentObjectId;
	}

	public void setParentObjectId(UUID parentObjectId) {
		this.parentObjectId = parentObjectId;
	}

	public UUID getChildObjectId() {
		return childObjectId;
	}

	public void setChildObjectId(UUID childObjectId) {
		this.childObjectId = childObjectId;
	}

	public Integer getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String generateTableName(ObjectDefinition parentObjectDefinition, ObjectDefinition childObjectDefinition) {
		String result = parentObjectDefinition.getObjectName();
		result += "_";
		result += childObjectDefinition.getObjectName();
		result = result.toLowerCase(); // make lowercase
		result = result.replaceAll("\\W+", "_"); // replace all groups of non-word characters with an underscore
		result = result.replaceAll("_+", "_"); // replace multiple underscores with a single underscore
		result = result.replaceAll("^_|_$", ""); // remove underscore from beginning and end
		result = "r_" + result; // prefix with r_
		return result;
	}

}
