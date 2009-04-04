package org.programmerplanet.crm.metadata.dao.jdbc;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.metadata.Application;
import org.programmerplanet.crm.metadata.dao.ApplicationDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcApplicationDao extends JdbcDaoSupport implements ApplicationDao {

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#getApplications()
	 */
	public List<Application> getApplications() {
		String sql = "SELECT * FROM crm_application ORDER BY view_index, application_name";
		RowMapper applicationRowMapper = new ApplicationRowMapper();
		List application = this.getJdbcTemplate().query(sql, applicationRowMapper);
		return application;
	}

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#getApplication(java.util.UUID)
	 */
	public Application getApplication(UUID id) {
		String sql = "SELECT * FROM crm_application WHERE id = ?::uuid";
		RowMapper applicationRowMapper = new ApplicationRowMapper();
		Application application = (Application)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, applicationRowMapper);
		return application;
	}

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#insertApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public void insertApplication(Application application) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_application (id, application_name, view_index) VALUES (?::uuid, ?, ?)";
		Object[] params = new Object[] { id.toString(), application.getApplicationName(), application.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		application.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#updateApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public void updateApplication(Application application) {
		String sql = "UPDATE crm_application SET application_name = ?, view_index = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { application.getApplicationName(), application.getViewIndex(), application.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#deleteApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public void deleteApplication(Application application) {
		String sql = "DELETE FROM crm_application WHERE id = ?::uuid";
		Object[] params = new Object[] { application.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.dao.ApplicationDao#isApplicationNameUnique(java.util.UUID, java.lang.String)
	 */
	public boolean isApplicationNameUnique(UUID id, String applicationName) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_application WHERE application_name = ? AND id <> ?::uuid";
			params = new Object[] { applicationName, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_application WHERE application_name = ?";
			params = new Object[] { applicationName };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
