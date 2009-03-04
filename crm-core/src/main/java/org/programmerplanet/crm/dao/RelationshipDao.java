package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface RelationshipDao {

	Relationship getRelationship(UUID id);

	Relationship getRelationship(UUID parentObjectId, UUID childObjectId);

	List<Relationship> getRelationshipsForObject(ObjectDefinition objectDefinition);

	void insertRelationship(Relationship relationship);

	void updateRelationship(Relationship relationship);

	void deleteRelationship(Relationship relationship);

}
