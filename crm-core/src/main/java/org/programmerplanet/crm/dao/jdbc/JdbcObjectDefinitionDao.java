package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcObjectDefinitionDao extends JdbcDaoSupport implements ObjectDefinitionDao {

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getAllObjectDefinitions()
	 */
	public List getAllObjectDefinitions() {
		String sql = "SELECT * FROM crm_object ORDER BY object_name";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		List objectDefinition = this.getJdbcTemplate().query(sql, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getObjectDefinition(java.lang.Long)
	 */
	public ObjectDefinition getObjectDefinition(Long id) {
		String sql = "SELECT * FROM crm_object WHERE id = ?";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		ObjectDefinition objectDefinition = (ObjectDefinition)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getObjectDefinition(java.lang.String)
	 */
	public ObjectDefinition getObjectDefinition(String objectName) {
		String sql = "SELECT * FROM crm_object WHERE object_name = ?";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		ObjectDefinition objectDefinition = (ObjectDefinition)this.getJdbcTemplate().queryForObject(sql, new Object[] { objectName }, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getObjectDefinitionsForApplication(org.programmerplanet.crm.model.Application)
	 */
	public List getObjectDefinitionsForApplication(Application application) {
		String sql = "SELECT o.* FROM crm_application_object AS ao INNER JOIN crm_object AS o ON (ao.object_id = o.id) WHERE ao.application_id = ? ORDER BY ao.view_index";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		List objectDefinition = this.getJdbcTemplate().query(sql, new Object[] { application.getId() }, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#insertObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void insertObjectDefinition(ObjectDefinition objectDefinition) {
		String sql = "INSERT INTO crm_object (object_name, table_name) VALUES (?, ?)";
		Object[] params = new Object[] { objectDefinition.getObjectName(), objectDefinition.getTableName() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_object_id_seq')");
		objectDefinition.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#updateObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void updateObjectDefinition(ObjectDefinition objectDefinition) {
		String sql = "UPDATE crm_object SET object_name = ?, table_name = ? WHERE id = ?";
		Object[] params = new Object[] { objectDefinition.getObjectName(), objectDefinition.getTableName(), objectDefinition.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#deleteObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void deleteObjectDefinition(ObjectDefinition objectDefinition) {
		String sql = "DELETE FROM crm_object WHERE id = ?";
		Object[] params = new Object[] { objectDefinition.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#isObjectNameUnique(java.lang.Long, java.lang.String)
	 */
	public boolean isObjectNameUnique(Long id, String objectName) {
		String sql = "SELECT COUNT(*) FROM crm_object WHERE object_name = ? AND id <> ?";
		Object[] params = new Object[] { objectName, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
