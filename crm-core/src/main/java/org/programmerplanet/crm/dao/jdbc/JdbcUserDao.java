package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.UserDao;
import org.programmerplanet.crm.model.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcUserDao extends JdbcDaoSupport implements UserDao {

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#getAllUsers()
	 */
	public List getAllUsers() {
		String sql = "SELECT * FROM crm_user ORDER BY username";
		RowMapper userRowMapper = new UserRowMapper();
		List users = this.getJdbcTemplate().query(sql, userRowMapper);
		return users;
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#getUser(java.lang.Long)
	 */
	public User getUser(Long id) {
		String sql = "SELECT * FROM crm_user WHERE id = ?";
		RowMapper userRowMapper = new UserRowMapper();
		User user = (User)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, userRowMapper);
		return user;
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#getUser(java.lang.String, java.lang.String)
	 */
	public User getUser(String username, String password) {
		String sql = "SELECT * FROM crm_user WHERE username = ? AND password = ?";
		Object[] params = new Object[] { username, password };
		RowMapper userRowMapper = new UserRowMapper();
		List users = this.getJdbcTemplate().query(sql, params, userRowMapper);
		User user = null;
		if (!users.isEmpty()) {
			if (users.size() == 1) {
				user = (User)users.get(0);
			}
			else {
				throw new IncorrectResultSizeDataAccessException(1, users.size());
			}
		}
		return user;
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#insertUser(org.programmerplanet.crm.model.User)
	 */
	public void insertUser(User user) {
		String sql = "INSERT INTO crm_user (username, password, first_name, last_name, email_address, administrator) VALUES (?, ?, ?, ?, ?, ?)";
		Object[] params = new Object[] { user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmailAddress(), new Boolean(user.isAdministrator()) };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_user_id_seq')");
		user.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#updateUser(org.programmerplanet.crm.model.User)
	 */
	public void updateUser(User user) {
		String sql = "UPDATE crm_user SET username = ?, password = ?, first_name = ?, last_name = ?, email_address = ?, administrator = ? WHERE id = ?";
		Object[] params = new Object[] { user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmailAddress(), new Boolean(user.isAdministrator()), user.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#deleteUser(org.programmerplanet.crm.model.User)
	 */
	public void deleteUser(User user) {
		String sql = "DELETE FROM crm_user WHERE id = ?";
		Object[] params = new Object[] { user.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.UserDao#isUsernameUnique(java.lang.Long, java.lang.String)
	 */
	public boolean isUsernameUnique(Long id, String username) {
		String sql = "SELECT COUNT(*) FROM crm_user WHERE username = ? AND id <> ?";
		Object[] params = new Object[] { username, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
