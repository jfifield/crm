package org.programmerplanet.crm.metadata;

import java.util.UUID;

import org.programmerplanet.crm.BaseReferenceable;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class FieldDefinition extends BaseReferenceable {

	private UUID objectId;
	private String fieldName;
	private String columnName;
	private DataType dataType;
	private Long dataTypeExt;
	private UUID dataTypeExtId;
	private boolean required;
	private Integer listIndex;
	private Integer viewIndex;

	public UUID getObjectId() {
		return objectId;
	}

	public void setObjectId(UUID objectId) {
		this.objectId = objectId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Long getDataTypeExt() {
		return dataTypeExt;
	}

	public void setDataTypeExt(Long dataTypeExt) {
		this.dataTypeExt = dataTypeExt;
	}

	public void setDataTypeExtId(UUID dataTypeExtId) {
		this.dataTypeExtId = dataTypeExtId;
	}

	public UUID getDataTypeExtId() {
		return dataTypeExtId;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Integer getListIndex() {
		return listIndex;
	}

	public void setListIndex(Integer listIndex) {
		this.listIndex = listIndex;
	}

	public Integer getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(Integer viewIndex) {
		this.viewIndex = viewIndex;
	}

	public String generateColumnName() {
		String result = fieldName; // start with field name
		result = result.toLowerCase(); // make lowercase
		result = result.replaceAll("\\W+", "_"); // replace all groups of non-word characters with an underscore
		result = result.replaceAll("_+", "_"); // replace multiple underscores with a single underscore
		result = result.replaceAll("^_|_$", ""); // remove underscore from beginning and end
		result = "f_" + result; // prefix with f_
		return result;
	}

}
