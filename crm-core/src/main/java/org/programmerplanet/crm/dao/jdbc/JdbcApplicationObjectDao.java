package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.dao.ApplicationObjectDao;
import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ApplicationObject;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcApplicationObjectDao extends JdbcDaoSupport implements ApplicationObjectDao {

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#getApplicationObjectsForApplication(org.programmerplanet.crm.model.Application)
	 */
	public List getApplicationObjectsForApplication(Application application) {
		String sql = "SELECT * FROM crm_application_object WHERE application_id = ?::uuid ORDER BY view_index";
		RowMapper applicationObjectRowMapper = new ApplicationObjectRowMapper();
		List applicationObjects = this.getJdbcTemplate().query(sql, new Object[] { application.getId().toString() }, applicationObjectRowMapper);
		return applicationObjects;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#getApplicationObject(java.util.UUID, java.util.UUID)
	 */
	public ApplicationObject getApplicationObject(UUID applicationId, UUID objectId) {
		String sql = "SELECT * FROM crm_application_object WHERE application_id = ?::uuid AND object_id = ?::uuid";
		RowMapper applicationObjectRowMapper = new ApplicationObjectRowMapper();
		Object[] params = new Object[] { applicationId.toString(), objectId.toString() };
		ApplicationObject applicationObject = (ApplicationObject)this.getJdbcTemplate().queryForObject(sql, params, applicationObjectRowMapper);
		return applicationObject;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#insertApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void insertApplicationObject(ApplicationObject applicationObject) {
		String sql = "INSERT INTO crm_application_object (application_id, object_id, view_index) VALUES (?::uuid, ?::uuid, ?)";
		Object[] params = new Object[] { applicationObject.getApplicationId().toString(), applicationObject.getObjectId().toString(), applicationObject.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#updateApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void updateApplicationObject(ApplicationObject applicationObject) {
		String sql = "UPDATE crm_application_object SET view_index = ? WHERE application_id = ?::uuid AND object_id = ?::uuid";
		Object[] params = new Object[] { applicationObject.getViewIndex(), applicationObject.getApplicationId().toString(), applicationObject.getObjectId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#deleteApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void deleteApplicationObject(ApplicationObject applicationObject) {
		String sql = "DELETE FROM crm_application_object WHERE application_id = ?::uuid AND object_id = ?::uuid";
		Object[] params = new Object[] { applicationObject.getApplicationId().toString(), applicationObject.getObjectId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#deleteApplicationObjects(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void deleteApplicationObjects(ObjectDefinition objectDefinition) {
		String sql = "DELETE FROM crm_application_object WHERE object_id = ?::uuid";
		Object[] params = new Object[] { objectDefinition.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

}
