package org.programmerplanet.crm.dao;

import java.util.List;

import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface RelationshipDao {

	Relationship getRelationship(Long id);

	Relationship getRelationship(Long parentObjectId, Long childObjectId);

	List getRelationshipsForObject(ObjectDefinition objectDefinition);

	void insertRelationship(Relationship relationship);

	void updateRelationship(Relationship relationship);

	void deleteRelationship(Relationship relationship);

}
