package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.dao.OptionListItemDao;
import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcOptionListItemDao extends JdbcDaoSupport implements OptionListItemDao {

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#getOptionListItems(org.programmerplanet.crm.model.OptionList)
	 */
	public List<OptionListItem> getOptionListItems(OptionList optionList) {
		String sql = "SELECT * FROM crm_option_list_item WHERE option_list_id = ?::uuid ORDER BY view_index, item_value";
		RowMapper rowMapper = new OptionListItemRowMapper();
		List optionListItems = this.getJdbcTemplate().query(sql, new Object[] { optionList.getId().toString() }, rowMapper);
		return optionListItems;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#getOptionListItem(java.util.UUID)
	 */
	public OptionListItem getOptionListItem(UUID id) {
		String sql = "SELECT * FROM crm_option_list_item WHERE id = ?::uuid";
		RowMapper rowMapper = new OptionListItemRowMapper();
		OptionListItem optionListItem = (OptionListItem)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, rowMapper);
		return optionListItem;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#insertOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void insertOptionListItem(OptionListItem optionListItem) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_option_list_item (id, option_list_id, item_value, view_index) VALUES (?::uuid, ?::uuid, ?, ?)";
		Object[] params = new Object[] { id.toString(), optionListItem.getOptionListId().toString(), optionListItem.getValue(), optionListItem.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		optionListItem.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#updateOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void updateOptionListItem(OptionListItem optionListItem) {
		String sql = "UPDATE crm_option_list_item SET item_value = ?, view_index = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { optionListItem.getValue(), optionListItem.getViewIndex(), optionListItem.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#deleteOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void deleteOptionListItem(OptionListItem optionListItem) {
		String sql = "DELETE FROM crm_option_list_item WHERE id = ?::uuid";
		Object[] params = new Object[] { optionListItem.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#isValueUnique(java.util.UUID, java.util.UUID, java.lang.String)
	 */
	public boolean isValueUnique(UUID optionListId, UUID id, String value) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_option_list_item WHERE option_list_id = ?::uuid AND item_value = ? AND id <> ?::uuid";
			params = new Object[] { optionListId.toString(), value, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_option_list_item WHERE option_list_id = ?::uuid AND item_value = ?";
			params = new Object[] { optionListId.toString(), value };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}
