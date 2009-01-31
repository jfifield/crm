package org.programmerplanet.crm.schema;

import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PostgresqlSchemaManager extends JdbcDaoSupport implements SchemaManager {

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#createTable(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void createTable(ObjectDefinition objectDefinition) {
		String sql = "CREATE TABLE " + objectDefinition.getTableName() + " (id SERIAL, CONSTRAINT pk_" + objectDefinition.getTableName() + " PRIMARY KEY (id))";
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#renameTable(java.lang.String, org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void renameTable(String oldTableName, ObjectDefinition objectDefinition) {
		String sql = "ALTER TABLE " + oldTableName + " RENAME TO " + objectDefinition.getTableName();
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#dropTable(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void dropTable(ObjectDefinition objectDefinition) {
		String sql = "DROP TABLE " + objectDefinition.getTableName();
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#createColumn(org.programmerplanet.crm.model.ObjectDefinition, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void createColumn(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition) {
		String sql = "ALTER TABLE " + objectDefinition.getTableName() + " ADD COLUMN ";
		sql += getColumnDefinition(fieldDefinition);
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#renameColumn(org.programmerplanet.crm.model.ObjectDefinition, java.lang.String, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void renameColumn(ObjectDefinition objectDefinition, String oldColumnName, FieldDefinition fieldDefinition) {
		String sql = "ALTER TABLE " + objectDefinition.getTableName() + " RENAME COLUMN " + oldColumnName + " TO " + fieldDefinition.getColumnName();
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#dropColumn(org.programmerplanet.crm.model.ObjectDefinition, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void dropColumn(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition) {
		String sql = "ALTER TABLE " + objectDefinition.getTableName() + " DROP COLUMN " + fieldDefinition.getColumnName();
		this.getJdbcTemplate().execute(sql);
	}

	private String getColumnDefinition(FieldDefinition fieldDefinition) {
		String result = fieldDefinition.getColumnName() + " ";

		if (DataType.SHORT_TEXT.equals(fieldDefinition.getDataType())) {
			result += "varchar(" + fieldDefinition.getDataTypeExt() + ")";
		}
		else if (DataType.LONG_TEXT.equals(fieldDefinition.getDataType())) {
			result += "text";
		}
		else if (DataType.AUTO_NUMBER.equals(fieldDefinition.getDataType())) {
			// TODO: should we create a unique index on this column?
			result += "integer";
		}
		else if (DataType.NUMBER.equals(fieldDefinition.getDataType())) {
			if (fieldDefinition.getDataTypeExt().intValue() > 0) {
				result += "numeric(12," + fieldDefinition.getDataTypeExt() + ")";
			}
			else {
				result += "integer";
			}
		}
		else if (DataType.MONEY.equals(fieldDefinition.getDataType())) {
			if (fieldDefinition.getDataTypeExt().intValue() > 0) {
				result += "numeric(12," + fieldDefinition.getDataTypeExt() + ")";
			}
			else {
				result += "integer";
			}
		}
		else if (DataType.PERCENT.equals(fieldDefinition.getDataType())) {
			int precision = fieldDefinition.getDataTypeExt().intValue();
			precision += 2;
			result += "numeric(12," + precision + ")";
		}
		else if (DataType.DATE.equals(fieldDefinition.getDataType())) {
			result += "date";
		}
		else if (DataType.DATE_TIME.equals(fieldDefinition.getDataType())) {
			result += "timestamp";
		}
		else if (DataType.BOOLEAN.equals(fieldDefinition.getDataType())) {
			result += "boolean";
		}
		else if (DataType.USER.equals(fieldDefinition.getDataType())) {
			result += "varchar(255)";
		}
		else if (DataType.EMAIL.equals(fieldDefinition.getDataType())) {
			result += "varchar(255)";
		}
		else if (DataType.URL.equals(fieldDefinition.getDataType())) {
			result += "varchar(255)";
		}
		else if (DataType.OPTION_LIST.equals(fieldDefinition.getDataType())) {
			result += "varchar(255)";
		}
		else if (DataType.FILE.equals(fieldDefinition.getDataType())) {
			result += "integer";
		}
		else if (DataType.OBJECT.equals(fieldDefinition.getDataType())) {
			result += "integer";
		}
		else {
			throw new IllegalArgumentException("Invalid data type: " + fieldDefinition.getDataType());
		}
		
		result += " NULL";
		
		return result;
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#createTable(org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void createTable(Relationship relationship, ObjectDefinition objectDefinition1, ObjectDefinition objectDefinition2) {
		String sql = "CREATE TABLE " + relationship.getTableName() + " (";
		sql += "" + objectDefinition1.getTableName() + "_id integer NOT NULL, ";
		sql += "" + objectDefinition2.getTableName() + "_id integer NOT NULL)";
		this.getJdbcTemplate().execute(sql);
	}

	/**
	 * @see org.programmerplanet.crm.schema.SchemaManager#dropTable(org.programmerplanet.crm.model.Relationship)
	 */
	public void dropTable(Relationship relationship) {
		String sql = "DROP TABLE " + relationship.getTableName();
		this.getJdbcTemplate().execute(sql);
	}

}
