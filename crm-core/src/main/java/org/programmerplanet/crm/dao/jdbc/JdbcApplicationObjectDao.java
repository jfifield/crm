package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

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
		String sql = "SELECT * FROM crm_application_object WHERE application_id = ? ORDER BY view_index";
		RowMapper applicationObjectRowMapper = new ApplicationObjectRowMapper();
		List applicationObjects = this.getJdbcTemplate().query(sql, new Object[] { application.getId() }, applicationObjectRowMapper);
		return applicationObjects;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#getApplicationObject(java.lang.Long, java.lang.Long)
	 */
	public ApplicationObject getApplicationObject(Long applicationId, Long objectId) {
		String sql = "SELECT * FROM crm_application_object WHERE application_id = ? AND object_id = ?";
		RowMapper applicationObjectRowMapper = new ApplicationObjectRowMapper();
		Object[] params = new Object[] { applicationId, objectId };
		ApplicationObject applicationObject = (ApplicationObject)this.getJdbcTemplate().queryForObject(sql, params, applicationObjectRowMapper);
		return applicationObject;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#insertApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void insertApplicationObject(ApplicationObject applicationObject) {
		String sql = "INSERT INTO crm_application_object (application_id, object_id, view_index) VALUES (?, ?, ?)";
		Object[] params = new Object[] { applicationObject.getApplicationId(), applicationObject.getObjectId(), applicationObject.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#updateApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void updateApplicationObject(ApplicationObject applicationObject) {
		String sql = "UPDATE crm_application_object SET view_index = ? WHERE application_id = ? AND object_id = ?";
		Object[] params = new Object[] { applicationObject.getViewIndex(), applicationObject.getApplicationId(), applicationObject.getObjectId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#deleteApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void deleteApplicationObject(ApplicationObject applicationObject) {
		String sql = "DELETE FROM crm_application_object WHERE application_id = ? AND object_id = ?";
		Object[] params = new Object[] { applicationObject.getApplicationId(), applicationObject.getObjectId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationObjectDao#deleteApplicationObjects(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void deleteApplicationObjects(ObjectDefinition objectDefinition) {
		String sql = "DELETE FROM crm_application_object WHERE object_id = ?";
		Object[] params = new Object[] { objectDefinition.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

}
