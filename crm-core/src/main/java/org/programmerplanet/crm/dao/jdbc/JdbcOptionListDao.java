package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.dao.OptionListDao;
import org.programmerplanet.crm.model.OptionList;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcOptionListDao extends JdbcDaoSupport implements OptionListDao {

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#getAllOptionLists()
	 */
	public List getAllOptionLists() {
		String sql = "SELECT * FROM crm_option_list ORDER BY option_list_name";
		RowMapper rowMapper = new OptionListRowMapper();
		List optionLists = this.getJdbcTemplate().query(sql, rowMapper);
		return optionLists;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#getOptionList(java.util.UUID)
	 */
	public OptionList getOptionList(UUID id) {
		String sql = "SELECT * FROM crm_option_list WHERE id = ?::uuid";
		RowMapper rowMapper = new OptionListRowMapper();
		OptionList optionList = (OptionList)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, rowMapper);
		return optionList;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#insertOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void insertOptionList(OptionList optionList) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_option_list (id, option_list_name) VALUES (?::uuid, ?)";
		Object[] params = new Object[] { id.toString(), optionList.getName() };
		this.getJdbcTemplate().update(sql, params);
		optionList.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#updateOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void updateOptionList(OptionList optionList) {
		String sql = "UPDATE crm_option_list SET option_list_name = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { optionList.getName(), optionList.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#deleteOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void deleteOptionList(OptionList optionList) {
		String sql = "DELETE FROM crm_option_list WHERE id = ?::uuid";
		Object[] params = new Object[] { optionList.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#isNameUnique(java.util.UUID, java.lang.String)
	 */
	public boolean isNameUnique(UUID id, String name) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_option_list WHERE option_list_name = ? AND id <> ?::uuid";
			params = new Object[] { name, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_option_list WHERE option_list_name = ?";
			params = new Object[] { name };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
