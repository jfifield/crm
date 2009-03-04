package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.dao.FieldDefinitionDao;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.OptionList;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcFieldDefinitionDao extends JdbcDaoSupport implements FieldDefinitionDao {

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinition(java.util.UUID)
	 */
	public FieldDefinition getFieldDefinition(UUID id) {
		String sql = "SELECT * FROM crm_field WHERE id = ?::uuid";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		FieldDefinition fieldDefinition = (FieldDefinition)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, fieldDefinitionRowMapper);
		return fieldDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObject(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ?::uuid ORDER BY view_index, field_name";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId().toString() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObjectList(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ?::uuid AND list_index IS NOT NULL ORDER BY list_index";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId().toString() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObjectView(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ?::uuid AND view_index IS NOT NULL ORDER BY view_index";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId().toString() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#insertFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void insertFieldDefinition(FieldDefinition fieldDefinition) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_field (id, object_id, field_name, column_name, data_type, data_type_ext, data_type_ext_id, required, list_index, view_index) VALUES (?::uuid, ?::uuid, ?, ?, ?, ?, ?::uuid, ?, ?, ?)";
		Object[] params = new Object[] { id.toString(), fieldDefinition.getObjectId().toString(), fieldDefinition.getFieldName(), fieldDefinition.getColumnName(), new Integer(fieldDefinition.getDataType().getValue()), fieldDefinition.getDataTypeExt(), fieldDefinition.getDataTypeExtId() != null ? fieldDefinition.getDataTypeExtId().toString() : null, new Boolean(fieldDefinition.isRequired()), fieldDefinition.getListIndex(), fieldDefinition.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		fieldDefinition.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#updateFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void updateFieldDefinition(FieldDefinition fieldDefinition) {
		String sql = "UPDATE crm_field SET field_name = ?, column_name = ?, data_type = ?, data_type_ext = ?, data_type_ext_id = ?::uuid, required = ?, list_index = ?, view_index = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { fieldDefinition.getFieldName(), fieldDefinition.getColumnName(), new Integer(fieldDefinition.getDataType().getValue()), fieldDefinition.getDataTypeExt(), fieldDefinition.getDataTypeExtId() != null ? fieldDefinition.getDataTypeExtId().toString() : null, new Boolean(fieldDefinition.isRequired()), fieldDefinition.getListIndex(), fieldDefinition.getViewIndex(), fieldDefinition.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#deleteFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void deleteFieldDefinition(FieldDefinition fieldDefinition) {
		String sql = "DELETE FROM crm_field WHERE id = ?::uuid";
		Object[] params = new Object[] { fieldDefinition.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#isFieldNameUnique(java.util.UUID, java.util.UUID, java.lang.String)
	 */
	public boolean isFieldNameUnique(UUID objectId, UUID id, String fieldName) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_field WHERE object_id = ?::uuid AND field_name = ? AND id <> ?::uuid";
			params = new Object[] { objectId.toString(), fieldName, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_field WHERE object_id = ?::uuid AND field_name = ?";
			params = new Object[] { objectId.toString(), fieldName };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsOfObjectType(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsOfObjectType(ObjectDefinition objectDefinition) {
		return getFieldDefinitionsOfType(DataType.OBJECT, objectDefinition.getId());
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsOfOptionListType(org.programmerplanet.crm.model.OptionList)
	 */
	public List<FieldDefinition> getFieldDefinitionsOfOptionListType(OptionList optionList) {
		return getFieldDefinitionsOfType(DataType.OPTION_LIST, optionList.getId());
	}

	private List<FieldDefinition> getFieldDefinitionsOfType(DataType dataType, UUID dataTypeExtId) {
		String sql = "SELECT * FROM crm_field WHERE data_type = ? AND data_type_ext_id = ?::uuid";
		Object[] params = new Object[] { new Integer(dataType.getValue()), dataTypeExtId.toString() };
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, params, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

}
