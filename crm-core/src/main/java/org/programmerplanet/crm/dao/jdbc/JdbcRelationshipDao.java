package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.RelationshipDao;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcRelationshipDao extends JdbcDaoSupport implements RelationshipDao {

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationship(java.lang.Long)
	 */
	public Relationship getRelationship(Long id) {
		String sql = "SELECT * FROM crm_relationship WHERE id = ?";
		RowMapper rowMapper = new RelationshipRowMapper();
		Relationship relationship = (Relationship)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, rowMapper);
		return relationship;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationship(java.lang.Long, java.lang.Long)
	 */
	public Relationship getRelationship(Long parentObjectId, Long childObjectId) {
		String sql = "SELECT * FROM crm_relationship WHERE parent_object_id = ? AND child_object_id = ?";
		RowMapper rowMapper = new RelationshipRowMapper();
		Object[] params = new Object[] { parentObjectId, childObjectId };
		Relationship relationship = (Relationship)this.getJdbcTemplate().queryForObject(sql, params, rowMapper);
		return relationship;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationshipsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getRelationshipsForObject(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_relationship WHERE parent_object_id = ? AND view_index IS NOT NULL ORDER BY view_index";
		Object[] params = new Object[] { objectDefinition.getId() };
		RowMapper rowMapper = new RelationshipRowMapper();
		List relationships = this.getJdbcTemplate().query(sql, params, rowMapper);
		return relationships;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#insertRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void insertRelationship(Relationship relationship) {
		String sql = "INSERT INTO crm_relationship (parent_object_id, child_object_id, view_index, table_name) VALUES (?, ?, ?, ?)";
		Object[] params = new Object[] { relationship.getParentObjectId(), relationship.getChildObjectId(), relationship.getViewIndex(), relationship.getTableName() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_relationship_id_seq')");
		relationship.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#updateRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void updateRelationship(Relationship relationship) {
		String sql = "UPDATE crm_relationship SET parent_object_id = ?, child_object_id = ?, view_index = ?, table_name = ? WHERE id = ?";
		Object[] params = new Object[] { relationship.getParentObjectId(), relationship.getChildObjectId(), relationship.getViewIndex(), relationship.getTableName(), relationship.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#deleteRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void deleteRelationship(Relationship relationship) {
		String sql = "DELETE FROM crm_relationship WHERE id = ?";
		Object[] params = new Object[] { relationship.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

}
