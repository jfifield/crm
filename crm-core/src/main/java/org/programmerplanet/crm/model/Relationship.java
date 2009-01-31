package org.programmerplanet.crm.model;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class Relationship extends Entity {

	private Long parentObjectId;
	private Long childObjectId;
	private Integer viewIndex;
	private String tableName;

	public Long getParentObjectId() {
		return parentObjectId;
	}

	public void setParentObjectId(Long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}

	public Long getChildObjectId() {
		return childObjectId;
	}

	public void setChildObjectId(Long childObjectId) {
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
