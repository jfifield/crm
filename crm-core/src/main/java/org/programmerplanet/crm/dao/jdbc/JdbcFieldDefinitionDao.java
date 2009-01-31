package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

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
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinition(java.lang.Long)
	 */
	public FieldDefinition getFieldDefinition(Long id) {
		String sql = "SELECT * FROM crm_field WHERE id = ?";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		FieldDefinition fieldDefinition = (FieldDefinition)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, fieldDefinitionRowMapper);
		return fieldDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObject(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ? ORDER BY view_index, field_name";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObjectList(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ? AND list_index IS NOT NULL ORDER BY list_index";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsForObjectView(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_field WHERE object_id = ? AND view_index IS NOT NULL ORDER BY view_index";
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, new Object[] { objectDefinition.getId() }, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#insertFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void insertFieldDefinition(FieldDefinition fieldDefinition) {
		String sql = "INSERT INTO crm_field (object_id, field_name, column_name, data_type, data_type_ext, required, list_index, view_index) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = new Object[] { fieldDefinition.getObjectId(), fieldDefinition.getFieldName(), fieldDefinition.getColumnName(), new Integer(fieldDefinition.getDataType().getValue()), fieldDefinition.getDataTypeExt(), new Boolean(fieldDefinition.isRequired()), fieldDefinition.getListIndex(), fieldDefinition.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_field_id_seq')");
		fieldDefinition.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#updateFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void updateFieldDefinition(FieldDefinition fieldDefinition) {
		String sql = "UPDATE crm_field SET field_name = ?, column_name = ?, data_type = ?, data_type_ext = ?, required = ?, list_index = ?, view_index = ? WHERE id = ?";
		Object[] params = new Object[] { fieldDefinition.getFieldName(), fieldDefinition.getColumnName(), new Integer(fieldDefinition.getDataType().getValue()), fieldDefinition.getDataTypeExt(), new Boolean(fieldDefinition.isRequired()), fieldDefinition.getListIndex(), fieldDefinition.getViewIndex(), fieldDefinition.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#deleteFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void deleteFieldDefinition(FieldDefinition fieldDefinition) {
		String sql = "DELETE FROM crm_field WHERE id = ?";
		Object[] params = new Object[] { fieldDefinition.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#isFieldNameUnique(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	public boolean isFieldNameUnique(Long objectId, Long id, String fieldName) {
		String sql = "SELECT COUNT(*) FROM crm_field WHERE object_id = ? AND field_name = ? AND id <> ?";
		Object[] params = new Object[] { objectId, fieldName, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsOfObjectType(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsOfObjectType(ObjectDefinition objectDefinition) {
		return getFieldMedatataOfType(DataType.OBJECT, objectDefinition.getId());
	}

	/**
	 * @see org.programmerplanet.crm.dao.FieldDefinitionDao#getFieldDefinitionsOfOptionListType(org.programmerplanet.crm.model.OptionList)
	 */
	public List getFieldDefinitionsOfOptionListType(OptionList optionList) {
		return getFieldMedatataOfType(DataType.OPTION_LIST, optionList.getId());
	}

	private List getFieldMedatataOfType(DataType dataType, Long dataTypeExt) {
		String sql = "SELECT * FROM crm_field WHERE data_type = ? AND data_type_ext = ?";
		Object[] params = new Object[] { new Integer(dataType.getValue()), dataTypeExt };
		RowMapper fieldDefinitionRowMapper = new FieldDefinitionRowMapper();
		List fieldDefinitions = this.getJdbcTemplate().query(sql, params, fieldDefinitionRowMapper);
		return fieldDefinitions;
	}

}
