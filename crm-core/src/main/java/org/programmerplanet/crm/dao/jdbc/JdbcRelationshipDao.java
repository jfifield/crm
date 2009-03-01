package org.programmerplanet.crm.dao.jdbc;

import java.util.List;
import java.util.UUID;

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
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationship(java.util.UUID)
	 */
	public Relationship getRelationship(UUID id) {
		String sql = "SELECT * FROM crm_relationship WHERE id = ?::uuid";
		RowMapper rowMapper = new RelationshipRowMapper();
		Relationship relationship = (Relationship)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, rowMapper);
		return relationship;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationship(java.util.UUID, java.util.UUID)
	 */
	public Relationship getRelationship(UUID parentObjectId, UUID childObjectId) {
		String sql = "SELECT * FROM crm_relationship WHERE parent_object_id = ?::uuid AND child_object_id = ?::uuid";
		RowMapper rowMapper = new RelationshipRowMapper();
		Object[] params = new Object[] { parentObjectId.toString(), childObjectId.toString() };
		Relationship relationship = (Relationship)this.getJdbcTemplate().queryForObject(sql, params, rowMapper);
		return relationship;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#getRelationshipsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getRelationshipsForObject(ObjectDefinition objectDefinition) {
		String sql = "SELECT * FROM crm_relationship WHERE parent_object_id = ?::uuid AND view_index IS NOT NULL ORDER BY view_index";
		Object[] params = new Object[] { objectDefinition.getId().toString() };
		RowMapper rowMapper = new RelationshipRowMapper();
		List relationships = this.getJdbcTemplate().query(sql, params, rowMapper);
		return relationships;
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#insertRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void insertRelationship(Relationship relationship) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_relationship (id, parent_object_id, child_object_id, view_index, table_name) VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?)";
		Object[] params = new Object[] { id.toString(), relationship.getParentObjectId().toString(), relationship.getChildObjectId().toString(), relationship.getViewIndex(), relationship.getTableName() };
		this.getJdbcTemplate().update(sql, params);
		relationship.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#updateRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void updateRelationship(Relationship relationship) {
		String sql = "UPDATE crm_relationship SET parent_object_id = ?::uuid, child_object_id = ?::uuid, view_index = ?, table_name = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { relationship.getParentObjectId().toString(), relationship.getChildObjectId().toString(), relationship.getViewIndex(), relationship.getTableName(), relationship.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.RelationshipDao#deleteRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void deleteRelationship(Relationship relationship) {
		String sql = "DELETE FROM crm_relationship WHERE id = ?::uuid";
		Object[] params = new Object[] { relationship.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

}
