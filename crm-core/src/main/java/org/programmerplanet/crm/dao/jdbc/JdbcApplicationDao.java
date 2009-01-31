package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.ApplicationDao;
import org.programmerplanet.crm.model.Application;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcApplicationDao extends JdbcDaoSupport implements ApplicationDao {

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#getAllApplications()
	 */
	public List getAllApplications() {
		String sql = "SELECT * FROM crm_application ORDER BY view_index, application_name";
		RowMapper applicationRowMapper = new ApplicationRowMapper();
		List application = this.getJdbcTemplate().query(sql, applicationRowMapper);
		return application;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#getApplication(java.lang.Long)
	 */
	public Application getApplication(Long id) {
		String sql = "SELECT * FROM crm_application WHERE id = ?";
		RowMapper applicationRowMapper = new ApplicationRowMapper();
		Application application = (Application)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, applicationRowMapper);
		return application;
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#insertApplication(org.programmerplanet.crm.model.Application)
	 */
	public void insertApplication(Application application) {
		String sql = "INSERT INTO crm_application (application_name, view_index) VALUES (?, ?)";
		Object[] params = new Object[] { application.getApplicationName(), application.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_application_id_seq')");
		application.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#updateApplication(org.programmerplanet.crm.model.Application)
	 */
	public void updateApplication(Application application) {
		String sql = "UPDATE crm_application SET application_name = ?, view_index = ? WHERE id = ?";
		Object[] params = new Object[] { application.getApplicationName(), application.getViewIndex(), application.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#deleteApplication(org.programmerplanet.crm.model.Application)
	 */
	public void deleteApplication(Application application) {
		String sql = "DELETE FROM crm_application WHERE id = ?";
		Object[] params = new Object[] { application.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.ApplicationDao#isApplicationNameUnique(java.lang.Long, java.lang.String)
	 */
	public boolean isApplicationNameUnique(Long id, String applicationName) {
		String sql = "SELECT COUNT(*) FROM crm_application WHERE application_name = ? AND id <> ?";
		Object[] params = new Object[] { applicationName, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
