package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

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
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getObjectDefinitions()
	 */
	public List<ObjectDefinition> getObjectDefinitions() {
		String sql = "SELECT * FROM crm_object ORDER BY object_name";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		List objectDefinition = this.getJdbcTemplate().query(sql, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#getObjectDefinition(java.util.UUID)
	 */
	public ObjectDefinition getObjectDefinition(UUID id) {
		String sql = "SELECT * FROM crm_object WHERE id = ?::uuid";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		ObjectDefinition objectDefinition = (ObjectDefinition)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, objectDefinitionRowMapper);
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
	public List<ObjectDefinition> getObjectDefinitionsForApplication(Application application) {
		String sql = "SELECT o.* FROM crm_application_object AS ao INNER JOIN crm_object AS o ON (ao.object_id = o.id) WHERE ao.application_id = ?::uuid ORDER BY ao.view_index";
		RowMapper objectDefinitionRowMapper = new ObjectDefinitionRowMapper();
		List objectDefinition = this.getJdbcTemplate().query(sql, new Object[] { application.getId().toString() }, objectDefinitionRowMapper);
		return objectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#insertObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void insertObjectDefinition(ObjectDefinition objectDefinition) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_object (id, object_name, table_name) VALUES (?::uuid, ?, ?)";
		Object[] params = new Object[] { id.toString(), objectDefinition.getObjectName(), objectDefinition.getTableName() };
		this.getJdbcTemplate().update(sql, params);
		objectDefinition.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#updateObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void updateObjectDefinition(ObjectDefinition objectDefinition) {
		String sql = "UPDATE crm_object SET object_name = ?, table_name = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { objectDefinition.getObjectName(), objectDefinition.getTableName(), objectDefinition.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#deleteObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void deleteObjectDefinition(ObjectDefinition objectDefinition) {
		String sql = "DELETE FROM crm_object WHERE id = ?::uuid";
		Object[] params = new Object[] { objectDefinition.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ObjectDefinitionDao#isObjectNameUnique(java.util.UUID, java.lang.String)
	 */
	public boolean isObjectNameUnique(UUID id, String objectName) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_object WHERE object_name = ? AND id <> ?::uuid";
			params = new Object[] { objectName, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_object WHERE object_name = ?";
			params = new Object[] { objectName };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
