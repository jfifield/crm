package org.programmerplanet.crm.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.programmerplanet.crm.metadata.dao.ApplicationDao;
import org.programmerplanet.crm.metadata.dao.ApplicationObjectDao;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.metadata.dao.OptionListDao;
import org.programmerplanet.crm.metadata.dao.OptionListItemDao;
import org.programmerplanet.crm.metadata.dao.RelationshipDao;
import org.programmerplanet.crm.schema.SchemaManager;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class MetadataManagerImplTest extends TestCase {

	public void testSaveApplication_NoExisting() {
		Application application = new Application();

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getApplications();
		applicationDaoControl.setReturnValue(Collections.EMPTY_LIST);

		applicationDao.insertApplication(application);

		applicationDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);

		metadataManager.saveApplication(application);
		assertEquals("application.viewIndex", new Integer(0), application.getViewIndex());
	}

	public void testSaveApplication_WithExisting() {
		Application application = new Application();

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getApplications();

		List list = new ArrayList();
		Application application1 = new Application();
		application1.setViewIndex(new Integer(0));
		list.add(application1);
		Application application2 = new Application();
		application2.setViewIndex(new Integer(3));
		list.add(application2);
		Application application3 = new Application();
		application3.setViewIndex(new Integer(2));
		list.add(application3);
		applicationDaoControl.setReturnValue(list);

		applicationDao.insertApplication(application);

		applicationDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);

		metadataManager.saveApplication(application);
		assertEquals("application.viewIndex", new Integer(4), application.getViewIndex());
	}

	public void testMoveApplicationViewIndex_MoveUp() {
		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getApplications();

		List list = new ArrayList();
		Application application1 = new Application();
		application1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		application1.setViewIndex(new Integer(0));
		list.add(application1);
		Application application2 = new Application();
		application2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		application2.setViewIndex(new Integer(1));
		list.add(application2);
		Application application3 = new Application();
		application3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		application3.setViewIndex(new Integer(2));
		list.add(application3);
		applicationDaoControl.setReturnValue(list);

		applicationDao.updateApplication(application1);
		applicationDao.updateApplication(application2);

		applicationDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);

		metadataManager.moveApplicationViewIndex(application2, "up");
		assertEquals("application1.viewIndex", new Integer(1), application1.getViewIndex());
		assertEquals("application2.viewIndex", new Integer(0), application2.getViewIndex());
		assertEquals("application3.viewIndex", new Integer(2), application3.getViewIndex());
	}

	public void testMoveApplicationViewIndex_MoveDown() {
		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getApplications();

		List list = new ArrayList();
		Application application1 = new Application();
		application1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		application1.setViewIndex(new Integer(0));
		list.add(application1);
		Application application2 = new Application();
		application2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		application2.setViewIndex(new Integer(1));
		list.add(application2);
		Application application3 = new Application();
		application3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		application3.setViewIndex(new Integer(2));
		list.add(application3);
		applicationDaoControl.setReturnValue(list);

		applicationDao.updateApplication(application2);
		applicationDao.updateApplication(application3);

		applicationDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);

		metadataManager.moveApplicationViewIndex(application2, "down");
		assertEquals("application1.viewIndex", new Integer(0), application1.getViewIndex());
		assertEquals("application2.viewIndex", new Integer(2), application2.getViewIndex());
		assertEquals("application3.viewIndex", new Integer(1), application3.getViewIndex());
	}

	public void testSaveApplicationObject_NoExisting() {
		UUID applicationId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(applicationId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		Application application = new Application();
		application.setId(applicationId);
		applicationDao.getApplication(application.getId());
		applicationDaoControl.setReturnValue(application);

		applicationDaoControl.replay();

		MockControl applicationObjectDaoControl = MockControl.createControl(ApplicationObjectDao.class);
		ApplicationObjectDao applicationObjectDao = (ApplicationObjectDao)applicationObjectDaoControl.getMock();
		applicationObjectDao.getApplicationObjectsForApplication(application);
		applicationObjectDaoControl.setReturnValue(Collections.EMPTY_LIST);

		applicationObjectDao.insertApplicationObject(applicationObject);

		applicationObjectDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);
		metadataManager.setApplicationObjectDao(applicationObjectDao);

		metadataManager.saveApplicationObject(applicationObject);
		assertEquals("applicationObject.viewIndex", new Integer(0), applicationObject.getViewIndex());
	}

	public void testSaveApplicationObject_WithExisting() {
		UUID applicationId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(applicationId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		Application application = new Application();
		application.setId(applicationId);
		applicationDao.getApplication(application.getId());
		applicationDaoControl.setReturnValue(application);

		applicationDaoControl.replay();

		MockControl applicationObjectDaoControl = MockControl.createControl(ApplicationObjectDao.class);
		ApplicationObjectDao applicationObjectDao = (ApplicationObjectDao)applicationObjectDaoControl.getMock();
		applicationObjectDao.getApplicationObjectsForApplication(application);

		List list = new ArrayList();
		ApplicationObject applicationObject1 = new ApplicationObject();
		applicationObject1.setViewIndex(new Integer(0));
		list.add(applicationObject1);
		ApplicationObject applicationObject2 = new ApplicationObject();
		applicationObject2.setViewIndex(new Integer(3));
		list.add(applicationObject2);
		ApplicationObject applicationObject3 = new ApplicationObject();
		applicationObject3.setViewIndex(new Integer(2));
		list.add(applicationObject3);
		applicationObjectDaoControl.setReturnValue(list);

		applicationObjectDao.insertApplicationObject(applicationObject);

		applicationObjectDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);
		metadataManager.setApplicationObjectDao(applicationObjectDao);

		metadataManager.saveApplicationObject(applicationObject);
		assertEquals("applicationObject.viewIndex", new Integer(4), applicationObject.getViewIndex());
	}

	public void testMoveApplicationObjectViewIndex_MoveUp() {
		UUID applicationId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		Application application = new Application();
		application.setId(applicationId);
		applicationDao.getApplication(application.getId());
		applicationDaoControl.setReturnValue(application);

		applicationDaoControl.replay();

		MockControl applicationObjectDaoControl = MockControl.createControl(ApplicationObjectDao.class);
		ApplicationObjectDao applicationObjectDao = (ApplicationObjectDao)applicationObjectDaoControl.getMock();
		applicationObjectDao.getApplicationObjectsForApplication(application);

		List list = new ArrayList();
		ApplicationObject applicationObject1 = new ApplicationObject();
		applicationObject1.setApplicationId(applicationId);
		applicationObject1.setObjectId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		applicationObject1.setViewIndex(new Integer(0));
		list.add(applicationObject1);
		ApplicationObject applicationObject2 = new ApplicationObject();
		applicationObject2.setApplicationId(applicationId);
		applicationObject2.setObjectId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		applicationObject2.setViewIndex(new Integer(3));
		list.add(applicationObject2);
		ApplicationObject applicationObject3 = new ApplicationObject();
		applicationObject3.setApplicationId(applicationId);
		applicationObject3.setObjectId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		applicationObject3.setViewIndex(new Integer(2));
		list.add(applicationObject3);
		applicationObjectDaoControl.setReturnValue(list);

		applicationObjectDao.updateApplicationObject(applicationObject1);
		applicationObjectDao.updateApplicationObject(applicationObject2);

		applicationObjectDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);
		metadataManager.setApplicationObjectDao(applicationObjectDao);

		metadataManager.moveApplicationObjectViewIndex(applicationObject2, "up");
		assertEquals("applicationObject1.viewIndex", new Integer(1), applicationObject1.getViewIndex());
		assertEquals("applicationObject2.viewIndex", new Integer(0), applicationObject2.getViewIndex());
		assertEquals("applicationObject3.viewIndex", new Integer(2), applicationObject3.getViewIndex());
	}

	public void testMoveApplicationObjectViewIndex_MoveDown() {
		UUID applicationId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		Application application = new Application();
		application.setId(applicationId);
		applicationDao.getApplication(application.getId());
		applicationDaoControl.setReturnValue(application);

		applicationDaoControl.replay();

		MockControl applicationObjectDaoControl = MockControl.createControl(ApplicationObjectDao.class);
		ApplicationObjectDao applicationObjectDao = (ApplicationObjectDao)applicationObjectDaoControl.getMock();
		applicationObjectDao.getApplicationObjectsForApplication(application);

		List list = new ArrayList();
		ApplicationObject applicationObject1 = new ApplicationObject();
		applicationObject1.setApplicationId(applicationId);
		applicationObject1.setObjectId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		applicationObject1.setViewIndex(new Integer(0));
		list.add(applicationObject1);
		ApplicationObject applicationObject2 = new ApplicationObject();
		applicationObject2.setApplicationId(applicationId);
		applicationObject2.setObjectId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		applicationObject2.setViewIndex(new Integer(3));
		list.add(applicationObject2);
		ApplicationObject applicationObject3 = new ApplicationObject();
		applicationObject3.setApplicationId(applicationId);
		applicationObject3.setObjectId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		applicationObject3.setViewIndex(new Integer(2));
		list.add(applicationObject3);
		applicationObjectDaoControl.setReturnValue(list);

		applicationObjectDao.updateApplicationObject(applicationObject2);
		applicationObjectDao.updateApplicationObject(applicationObject3);

		applicationObjectDaoControl.replay();

		metadataManager.setApplicationDao(applicationDao);
		metadataManager.setApplicationObjectDao(applicationObjectDao);

		metadataManager.moveApplicationObjectViewIndex(applicationObject2, "down");
		assertEquals("applicationObject1.viewIndex", new Integer(0), applicationObject1.getViewIndex());
		assertEquals("applicationObject2.viewIndex", new Integer(2), applicationObject2.getViewIndex());
		assertEquals("applicationObject3.viewIndex", new Integer(1), applicationObject3.getViewIndex());
	}

	public void testSaveFieldDefinition_NoExisting() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setObjectId(objectId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		fieldDefinitionDaoControl.setReturnValue(Collections.EMPTY_LIST);

		fieldDefinitionDao.insertFieldDefinition(fieldDefinition);

		fieldDefinitionDaoControl.replay();

		MockControl schemaManagerControl = MockControl.createControl(SchemaManager.class);
		SchemaManager schemaManager = (SchemaManager)schemaManagerControl.getMock();
		schemaManager.createColumn(objectDefinition, fieldDefinition);
		schemaManagerControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);
		metadataManager.setSchemaManager(schemaManager);

		metadataManager.saveFieldDefinition(fieldDefinition);
		assertEquals("fieldDefinition.viewIndex", new Integer(0), fieldDefinition.getViewIndex());
	}

	public void testSaveFieldDefinition_WithExisting() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setObjectId(objectId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setViewIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setViewIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setViewIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.insertFieldDefinition(fieldDefinition);

		fieldDefinitionDaoControl.replay();

		MockControl schemaManagerControl = MockControl.createControl(SchemaManager.class);
		SchemaManager schemaManager = (SchemaManager)schemaManagerControl.getMock();
		schemaManager.createColumn(objectDefinition, fieldDefinition);
		schemaManagerControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);
		metadataManager.setSchemaManager(schemaManager);

		metadataManager.saveFieldDefinition(fieldDefinition);
		assertEquals("fieldDefinition.viewIndex", new Integer(4), fieldDefinition.getViewIndex());
	}

	public void testMoveFieldDefinitionViewIndex_MoveUp() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		fieldDefinition1.setViewIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		fieldDefinition2.setViewIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		fieldDefinition3.setViewIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition1);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.moveFieldDefinitionViewIndex(fieldDefinition2, "up");
		assertEquals("fieldDefinition1.viewIndex", new Integer(1), fieldDefinition1.getViewIndex());
		assertEquals("fieldDefinition2.viewIndex", new Integer(0), fieldDefinition2.getViewIndex());
		assertEquals("fieldDefinition3.viewIndex", new Integer(2), fieldDefinition3.getViewIndex());
	}

	public void testMoveFieldDefinitionViewIndex_MoveDown() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		fieldDefinition1.setViewIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		fieldDefinition2.setViewIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		fieldDefinition3.setViewIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition3);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.moveFieldDefinitionViewIndex(fieldDefinition2, "down");
		assertEquals("fieldDefinition1.viewIndex", new Integer(0), fieldDefinition1.getViewIndex());
		assertEquals("fieldDefinition2.viewIndex", new Integer(2), fieldDefinition2.getViewIndex());
		assertEquals("fieldDefinition3.viewIndex", new Integer(1), fieldDefinition3.getViewIndex());
	}

	public void testMoveFieldDefinitionListIndex_MoveUp() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		fieldDefinition2.setListIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		fieldDefinition3.setListIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition1);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.moveFieldDefinitionListIndex(fieldDefinition2, "up");
		assertEquals("fieldDefinition1.listIndex", new Integer(1), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(0), fieldDefinition2.getListIndex());
		assertEquals("fieldDefinition3.listIndex", new Integer(2), fieldDefinition3.getListIndex());
	}

	public void testMoveFieldDefinitionListIndex_MoveDown() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		fieldDefinition2.setListIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		fieldDefinition3.setListIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition3);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.moveFieldDefinitionListIndex(fieldDefinition2, "down");
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(2), fieldDefinition2.getListIndex());
		assertEquals("fieldDefinition3.listIndex", new Integer(1), fieldDefinition3.getListIndex());
	}

	public void testAddFieldDefinitionListIndex_NoExisting() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.addFieldDefinitionListIndex(fieldDefinition2);
		assertNull("fieldDefinition1.listIndex", fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(0), fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testAddFieldDefinitionListIndex_WithExisting() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("f77d8832-62e8-467f-88ea-526398dce0f9"));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.addFieldDefinitionListIndex(fieldDefinition2);
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(1), fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testRemoveFieldDefinitionListIndex() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl fieldDefinitionDaoControl = MockControl.createControl(FieldDefinitionDao.class);
		FieldDefinitionDao fieldDefinitionDao = (FieldDefinitionDao)fieldDefinitionDaoControl.getMock();
		fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);

		List list = new ArrayList();
		FieldDefinition fieldDefinition1 = new FieldDefinition();
		fieldDefinition1.setObjectId(objectId);
		fieldDefinition1.setId(UUID.fromString("f77d8832-62e8-467f-88ea-526398dce0f9"));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		fieldDefinition2.setListIndex(new Integer(1));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setFieldDefinitionDao(fieldDefinitionDao);

		metadataManager.removeFieldDefinitionListIndex(fieldDefinition2);
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertNull("fieldDefinition2.listIndex", fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testSaveOptionListItem_NoExisting() {
		UUID optionListId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setOptionListId(optionListId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl optionListDaoControl = MockControl.createControl(OptionListDao.class);
		OptionListDao optionListDao = (OptionListDao)optionListDaoControl.getMock();
		OptionList optionList = new OptionList();
		optionList.setId(optionListId);
		optionListDao.getOptionList(optionList.getId());
		optionListDaoControl.setReturnValue(optionList);

		optionListDaoControl.replay();

		MockControl optionListItemDaoControl = MockControl.createControl(OptionListItemDao.class);
		OptionListItemDao optionListItemDao = (OptionListItemDao)optionListItemDaoControl.getMock();
		optionListItemDao.getOptionListItems(optionList);
		optionListItemDaoControl.setReturnValue(Collections.EMPTY_LIST);

		optionListItemDao.insertOptionListItem(optionListItem);

		optionListItemDaoControl.replay();

		metadataManager.setOptionListDao(optionListDao);
		metadataManager.setOptionListItemDao(optionListItemDao);

		metadataManager.saveOptionListItem(optionListItem);
		assertEquals("optionListItem.viewIndex", new Integer(0), optionListItem.getViewIndex());
	}

	public void testSaveOptionListItem_WithExisting() {
		UUID optionListId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setOptionListId(optionListId);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl optionListDaoControl = MockControl.createControl(OptionListDao.class);
		OptionListDao optionListDao = (OptionListDao)optionListDaoControl.getMock();
		OptionList optionList = new OptionList();
		optionList.setId(optionListId);
		optionListDao.getOptionList(optionList.getId());
		optionListDaoControl.setReturnValue(optionList);

		optionListDaoControl.replay();

		MockControl optionListItemDaoControl = MockControl.createControl(OptionListItemDao.class);
		OptionListItemDao optionListItemDao = (OptionListItemDao)optionListItemDaoControl.getMock();
		optionListItemDao.getOptionListItems(optionList);

		List list = new ArrayList();
		OptionListItem optionListItem1 = new OptionListItem();
		optionListItem1.setViewIndex(new Integer(0));
		list.add(optionListItem1);
		OptionListItem optionListItem2 = new OptionListItem();
		optionListItem2.setViewIndex(new Integer(3));
		list.add(optionListItem2);
		OptionListItem optionListItem3 = new OptionListItem();
		optionListItem3.setViewIndex(new Integer(2));
		list.add(optionListItem3);
		optionListItemDaoControl.setReturnValue(list);

		optionListItemDao.insertOptionListItem(optionListItem);

		optionListItemDaoControl.replay();

		metadataManager.setOptionListDao(optionListDao);
		metadataManager.setOptionListItemDao(optionListItemDao);

		metadataManager.saveOptionListItem(optionListItem);
		assertEquals("optionListItem.viewIndex", new Integer(4), optionListItem.getViewIndex());
	}

	public void testMoveOptionListItemViewIndex_MoveUp() {
		UUID optionListId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl optionListDaoControl = MockControl.createControl(OptionListDao.class);
		OptionListDao optionListDao = (OptionListDao)optionListDaoControl.getMock();
		OptionList optionList = new OptionList();
		optionList.setId(optionListId);
		optionListDao.getOptionList(optionList.getId());
		optionListDaoControl.setReturnValue(optionList);

		optionListDaoControl.replay();

		MockControl optionListItemDaoControl = MockControl.createControl(OptionListItemDao.class);
		OptionListItemDao optionListItemDao = (OptionListItemDao)optionListItemDaoControl.getMock();
		optionListItemDao.getOptionListItems(optionList);

		List list = new ArrayList();
		OptionListItem optionListItem1 = new OptionListItem();
		optionListItem1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		optionListItem1.setOptionListId(optionListId);
		optionListItem1.setViewIndex(new Integer(0));
		list.add(optionListItem1);
		OptionListItem optionListItem2 = new OptionListItem();
		optionListItem2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		optionListItem2.setOptionListId(optionListId);
		optionListItem2.setViewIndex(new Integer(1));
		list.add(optionListItem2);
		OptionListItem optionListItem3 = new OptionListItem();
		optionListItem3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		optionListItem3.setOptionListId(optionListId);
		optionListItem3.setViewIndex(new Integer(2));
		list.add(optionListItem3);
		optionListItemDaoControl.setReturnValue(list);

		optionListItemDao.updateOptionListItem(optionListItem1);
		optionListItemDao.updateOptionListItem(optionListItem2);

		optionListItemDaoControl.replay();

		metadataManager.setOptionListDao(optionListDao);
		metadataManager.setOptionListItemDao(optionListItemDao);

		metadataManager.moveOptionListItemViewIndex(optionListItem2, "up");
		assertEquals("optionListItem1.viewIndex", new Integer(1), optionListItem1.getViewIndex());
		assertEquals("optionListItem2.viewIndex", new Integer(0), optionListItem2.getViewIndex());
		assertEquals("optionListItem3.viewIndex", new Integer(2), optionListItem3.getViewIndex());
	}

	public void testMoveOptionListItemViewIndex_MoveDown() {
		UUID optionListId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl optionListDaoControl = MockControl.createControl(OptionListDao.class);
		OptionListDao optionListDao = (OptionListDao)optionListDaoControl.getMock();
		OptionList optionList = new OptionList();
		optionList.setId(optionListId);
		optionListDao.getOptionList(optionList.getId());
		optionListDaoControl.setReturnValue(optionList);

		optionListDaoControl.replay();

		MockControl optionListItemDaoControl = MockControl.createControl(OptionListItemDao.class);
		OptionListItemDao optionListItemDao = (OptionListItemDao)optionListItemDaoControl.getMock();
		optionListItemDao.getOptionListItems(optionList);

		List list = new ArrayList();
		OptionListItem optionListItem1 = new OptionListItem();
		optionListItem1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		optionListItem1.setOptionListId(optionListId);
		optionListItem1.setViewIndex(new Integer(0));
		list.add(optionListItem1);
		OptionListItem optionListItem2 = new OptionListItem();
		optionListItem2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		optionListItem2.setOptionListId(optionListId);
		optionListItem2.setViewIndex(new Integer(1));
		list.add(optionListItem2);
		OptionListItem optionListItem3 = new OptionListItem();
		optionListItem3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		optionListItem3.setOptionListId(optionListId);
		optionListItem3.setViewIndex(new Integer(2));
		list.add(optionListItem3);
		optionListItemDaoControl.setReturnValue(list);

		optionListItemDao.updateOptionListItem(optionListItem2);
		optionListItemDao.updateOptionListItem(optionListItem3);

		optionListItemDaoControl.replay();

		metadataManager.setOptionListDao(optionListDao);
		metadataManager.setOptionListItemDao(optionListItemDao);

		metadataManager.moveOptionListItemViewIndex(optionListItem2, "down");
		assertEquals("optionListItem1.viewIndex", new Integer(0), optionListItem1.getViewIndex());
		assertEquals("optionListItem2.viewIndex", new Integer(2), optionListItem2.getViewIndex());
		assertEquals("optionListItem3.viewIndex", new Integer(1), optionListItem3.getViewIndex());
	}

	public void testSaveRelationship_NoExisting() {
		UUID objectId1 = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		UUID objectId2 = UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402");
		Relationship relationship1 = new Relationship();
		relationship1.setParentObjectId(objectId1);
		relationship1.setChildObjectId(objectId2);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition1 = new ObjectDefinition();
		objectDefinition1.setId(objectId1);
		objectDefinitionDao.getObjectDefinition(objectDefinition1.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition1);
		ObjectDefinition objectDefinition2 = new ObjectDefinition();
		objectDefinition2.setId(objectId2);
		objectDefinitionDao.getObjectDefinition(objectDefinition2.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition2);

		objectDefinitionDaoControl.replay();

		MockControl relationshipDaoControl = MockControl.createControl(RelationshipDao.class);
		RelationshipDao relationshipDao = (RelationshipDao)relationshipDaoControl.getMock();
		relationshipDao.getRelationshipsForObject(objectDefinition1);
		relationshipDaoControl.setReturnValue(Collections.EMPTY_LIST);
		relationshipDao.getRelationshipsForObject(objectDefinition2);
		relationshipDaoControl.setReturnValue(Collections.EMPTY_LIST);

		relationshipDao.insertRelationship(relationship1);

		SaveArgumentsMatcher matcher = new SaveArgumentsMatcher();
		relationshipDaoControl.setMatcher(matcher);
		relationshipDao.insertRelationship(null);

		relationshipDaoControl.replay();

		MockControl schemaManagerControl = MockControl.createControl(SchemaManager.class);
		SchemaManager schemaManager = (SchemaManager)schemaManagerControl.getMock();
		schemaManager.createTable(relationship1, objectDefinition1, objectDefinition2);
		schemaManagerControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setRelationshipDao(relationshipDao);
		metadataManager.setSchemaManager(schemaManager);

		metadataManager.saveRelationship(relationship1);
		assertEquals("relationship1.viewIndex", new Integer(0), relationship1.getViewIndex());
		Relationship relationship2 = (Relationship)matcher.actual[0];
		assertEquals("relationship2.viewIndex", new Integer(0), relationship2.getViewIndex());
	}

	public void testSaveRelationship_WithExisting() {
		UUID objectId1 = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");
		UUID objectId2 = UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402");
		Relationship relationship1 = new Relationship();
		relationship1.setParentObjectId(objectId1);
		relationship1.setChildObjectId(objectId2);

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition1 = new ObjectDefinition();
		objectDefinition1.setId(objectId1);
		objectDefinitionDao.getObjectDefinition(objectDefinition1.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition1);
		ObjectDefinition objectDefinition2 = new ObjectDefinition();
		objectDefinition2.setId(objectId2);
		objectDefinitionDao.getObjectDefinition(objectDefinition2.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition2);

		objectDefinitionDaoControl.replay();

		MockControl relationshipDaoControl = MockControl.createControl(RelationshipDao.class);
		RelationshipDao relationshipDao = (RelationshipDao)relationshipDaoControl.getMock();
		relationshipDao.getRelationshipsForObject(objectDefinition1);
		List list1 = new ArrayList();
		Relationship relationship1a = new Relationship();
		relationship1a.setViewIndex(new Integer(0));
		list1.add(relationship1a);
		Relationship relationship1b = new Relationship();
		relationship1b.setViewIndex(new Integer(3));
		list1.add(relationship1b);
		Relationship relationship1c = new Relationship();
		relationship1c.setViewIndex(new Integer(2));
		list1.add(relationship1c);
		relationshipDaoControl.setReturnValue(list1);

		relationshipDao.getRelationshipsForObject(objectDefinition2);
		List list2 = new ArrayList();
		Relationship relationship2a = new Relationship();
		relationship2a.setViewIndex(new Integer(0));
		list2.add(relationship2a);
		Relationship relationship2b = new Relationship();
		relationship2b.setViewIndex(new Integer(3));
		list2.add(relationship2b);
		Relationship relationship2c = new Relationship();
		relationship2c.setViewIndex(new Integer(2));
		list2.add(relationship2c);
		relationshipDaoControl.setReturnValue(list2);

		relationshipDao.insertRelationship(relationship1);

		SaveArgumentsMatcher matcher = new SaveArgumentsMatcher();
		relationshipDaoControl.setMatcher(matcher);
		relationshipDao.insertRelationship(null);

		relationshipDaoControl.replay();

		MockControl schemaManagerControl = MockControl.createControl(SchemaManager.class);
		SchemaManager schemaManager = (SchemaManager)schemaManagerControl.getMock();
		schemaManager.createTable(relationship1, objectDefinition1, objectDefinition2);
		schemaManagerControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setRelationshipDao(relationshipDao);
		metadataManager.setSchemaManager(schemaManager);

		metadataManager.saveRelationship(relationship1);
		assertEquals("relationship1.viewIndex", new Integer(4), relationship1.getViewIndex());
		Relationship relationship2 = (Relationship)matcher.actual[0];
		assertEquals("relationship2.viewIndex", new Integer(4), relationship2.getViewIndex());
	}

	public void testMoveRelationshipViewIndex_MoveUp() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl relationshipDaoControl = MockControl.createControl(RelationshipDao.class);
		RelationshipDao relationshipDao = (RelationshipDao)relationshipDaoControl.getMock();
		relationshipDao.getRelationshipsForObject(objectDefinition);

		List list = new ArrayList();
		Relationship relationship1 = new Relationship();
		relationship1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		relationship1.setParentObjectId(objectId);
		relationship1.setViewIndex(new Integer(0));
		list.add(relationship1);
		Relationship relationship2 = new Relationship();
		relationship2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		relationship2.setParentObjectId(objectId);
		relationship2.setViewIndex(new Integer(3));
		list.add(relationship2);
		Relationship relationship3 = new Relationship();
		relationship3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		relationship3.setParentObjectId(objectId);
		relationship3.setViewIndex(new Integer(2));
		list.add(relationship3);
		relationshipDaoControl.setReturnValue(list);

		relationshipDao.updateRelationship(relationship1);
		relationshipDao.updateRelationship(relationship2);

		relationshipDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setRelationshipDao(relationshipDao);

		metadataManager.moveRelationshipViewIndex(relationship2, "up");
		assertEquals("relationship1.viewIndex", new Integer(1), relationship1.getViewIndex());
		assertEquals("relationship2.viewIndex", new Integer(0), relationship2.getViewIndex());
		assertEquals("relationship3.viewIndex", new Integer(2), relationship3.getViewIndex());
	}

	public void testMoveRelationshipViewIndex_MoveDown() {
		UUID objectId = UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc");

		MetadataManagerImpl metadataManager = new MetadataManagerImpl();

		MockControl objectDefinitionDaoControl = MockControl.createControl(ObjectDefinitionDao.class);
		ObjectDefinitionDao objectDefinitionDao = (ObjectDefinitionDao)objectDefinitionDaoControl.getMock();
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(objectId);
		objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDaoControl.setReturnValue(objectDefinition);

		objectDefinitionDaoControl.replay();

		MockControl relationshipDaoControl = MockControl.createControl(RelationshipDao.class);
		RelationshipDao relationshipDao = (RelationshipDao)relationshipDaoControl.getMock();
		relationshipDao.getRelationshipsForObject(objectDefinition);

		List list = new ArrayList();
		Relationship relationship1 = new Relationship();
		relationship1.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		relationship1.setParentObjectId(objectId);
		relationship1.setViewIndex(new Integer(0));
		list.add(relationship1);
		Relationship relationship2 = new Relationship();
		relationship2.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		relationship2.setParentObjectId(objectId);
		relationship2.setViewIndex(new Integer(3));
		list.add(relationship2);
		Relationship relationship3 = new Relationship();
		relationship3.setId(UUID.fromString("f3db28bb-76e7-49ee-88fd-10a8a9a204db"));
		relationship3.setParentObjectId(objectId);
		relationship3.setViewIndex(new Integer(2));
		list.add(relationship3);
		relationshipDaoControl.setReturnValue(list);

		relationshipDao.updateRelationship(relationship2);
		relationshipDao.updateRelationship(relationship3);

		relationshipDaoControl.replay();

		metadataManager.setObjectDefinitionDao(objectDefinitionDao);
		metadataManager.setRelationshipDao(relationshipDao);

		metadataManager.moveRelationshipViewIndex(relationship2, "down");
		assertEquals("relationship1.viewIndex", new Integer(0), relationship1.getViewIndex());
		assertEquals("relationship2.viewIndex", new Integer(2), relationship2.getViewIndex());
		assertEquals("relationship3.viewIndex", new Integer(1), relationship3.getViewIndex());
	}

	private static class SaveArgumentsMatcher extends AbstractMatcher {

		public Object[] expected;
		public Object[] actual;

		public boolean matches(Object[] expected, Object[] actual) {
			this.expected = expected;
			this.actual = actual;
			return true;
		}

	}

}
