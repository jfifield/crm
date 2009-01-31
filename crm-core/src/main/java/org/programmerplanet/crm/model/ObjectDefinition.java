package org.programmerplanet.crm.model;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ObjectDefinition extends Entity {

	private String objectName;
	private String tableName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String generateTableName() {
		String result = objectName; // start with object name
		result = result.toLowerCase(); // make lowercase
		result = result.replaceAll("\\W+", "_"); // replace all groups of non-word characters with an underscore
		result = result.replaceAll("_+", "_"); // replace multiple underscores with a single underscore
		result = result.replaceAll("^_|_$", ""); // remove underscore from beginning and end
		result = "o_" + result; // prefix with o_
		return result;
	}

	public String getPluralObjectName() {
		/* EXCEPTIONS
		 * ----------
		 * foot ==> feet
		 * goose ==> geese
		 * man ==> men
		 * woman ==> women
		 * criterion ==> criteria
		 * sheep ==> sheep
		 */
		// replace "[consonant]y" with "[consonant]ies"
		if (objectName.matches(".*[^aeiou]y$")) {
			return objectName.substring(0, objectName.length() - 1) + "ies";
		}
		// add "s" to end
		else {
			return objectName + "s";
		}
	}

}
