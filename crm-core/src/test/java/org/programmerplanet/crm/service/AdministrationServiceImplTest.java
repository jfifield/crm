package org.programmerplanet.crm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.programmerplanet.crm.dao.ApplicationDao;
import org.programmerplanet.crm.dao.ApplicationObjectDao;
import org.programmerplanet.crm.dao.FieldDefinitionDao;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.dao.OptionListDao;
import org.programmerplanet.crm.dao.OptionListItemDao;
import org.programmerplanet.crm.dao.RelationshipDao;
import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ApplicationObject;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.schema.SchemaManager;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class AdministrationServiceImplTest extends TestCase {

	public void testInsertApplication_NoExisting() {
		Application application = new Application();

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getAllApplications();
		applicationDaoControl.setReturnValue(Collections.EMPTY_LIST);

		applicationDao.insertApplication(application);

		applicationDaoControl.replay();

		administrationService.setApplicationDao(applicationDao);

		administrationService.insertApplication(application);
		assertEquals("application.viewIndex", new Integer(0), application.getViewIndex());
	}

	public void testInsertApplication_WithExisting() {
		Application application = new Application();

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getAllApplications();

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

		administrationService.setApplicationDao(applicationDao);

		administrationService.insertApplication(application);
		assertEquals("application.viewIndex", new Integer(4), application.getViewIndex());
	}

	public void testMoveApplicationViewIndex_MoveUp() {
		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getAllApplications();

		List list = new ArrayList();
		Application application1 = new Application();
		application1.setId(new Long(1));
		application1.setViewIndex(new Integer(0));
		list.add(application1);
		Application application2 = new Application();
		application2.setId(new Long(2));
		application2.setViewIndex(new Integer(1));
		list.add(application2);
		Application application3 = new Application();
		application3.setId(new Long(3));
		application3.setViewIndex(new Integer(2));
		list.add(application3);
		applicationDaoControl.setReturnValue(list);

		applicationDao.updateApplication(application1);
		applicationDao.updateApplication(application2);

		applicationDaoControl.replay();

		administrationService.setApplicationDao(applicationDao);

		administrationService.moveApplicationViewIndex(application2, "up");
		assertEquals("application1.viewIndex", new Integer(1), application1.getViewIndex());
		assertEquals("application2.viewIndex", new Integer(0), application2.getViewIndex());
		assertEquals("application3.viewIndex", new Integer(2), application3.getViewIndex());
	}

	public void testMoveApplicationViewIndex_MoveDown() {
		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

		MockControl applicationDaoControl = MockControl.createControl(ApplicationDao.class);
		ApplicationDao applicationDao = (ApplicationDao)applicationDaoControl.getMock();
		applicationDao.getAllApplications();

		List list = new ArrayList();
		Application application1 = new Application();
		application1.setId(new Long(1));
		application1.setViewIndex(new Integer(0));
		list.add(application1);
		Application application2 = new Application();
		application2.setId(new Long(2));
		application2.setViewIndex(new Integer(1));
		list.add(application2);
		Application application3 = new Application();
		application3.setId(new Long(3));
		application3.setViewIndex(new Integer(2));
		list.add(application3);
		applicationDaoControl.setReturnValue(list);

		applicationDao.updateApplication(application2);
		applicationDao.updateApplication(application3);

		applicationDaoControl.replay();

		administrationService.setApplicationDao(applicationDao);

		administrationService.moveApplicationViewIndex(application2, "down");
		assertEquals("application1.viewIndex", new Integer(0), application1.getViewIndex());
		assertEquals("application2.viewIndex", new Integer(2), application2.getViewIndex());
		assertEquals("application3.viewIndex", new Integer(1), application3.getViewIndex());
	}

	public void testInsertApplicationObject_NoExisting() {
		Long applicationId = new Long(1);
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(applicationId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setApplicationDao(applicationDao);
		administrationService.setApplicationObjectDao(applicationObjectDao);

		administrationService.insertApplicationObject(applicationObject);
		assertEquals("applicationObject.viewIndex", new Integer(0), applicationObject.getViewIndex());
	}

	public void testInsertApplicationObject_WithExisting() {
		Long applicationId = new Long(1);
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(applicationId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setApplicationDao(applicationDao);
		administrationService.setApplicationObjectDao(applicationObjectDao);

		administrationService.insertApplicationObject(applicationObject);
		assertEquals("applicationObject.viewIndex", new Integer(4), applicationObject.getViewIndex());
	}

	public void testMoveApplicationObjectViewIndex_MoveUp() {
		Long applicationId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		applicationObject1.setObjectId(new Long(1));
		applicationObject1.setViewIndex(new Integer(0));
		list.add(applicationObject1);
		ApplicationObject applicationObject2 = new ApplicationObject();
		applicationObject2.setApplicationId(applicationId);
		applicationObject2.setObjectId(new Long(2));
		applicationObject2.setViewIndex(new Integer(3));
		list.add(applicationObject2);
		ApplicationObject applicationObject3 = new ApplicationObject();
		applicationObject3.setApplicationId(applicationId);
		applicationObject3.setObjectId(new Long(3));
		applicationObject3.setViewIndex(new Integer(2));
		list.add(applicationObject3);
		applicationObjectDaoControl.setReturnValue(list);

		applicationObjectDao.updateApplicationObject(applicationObject1);
		applicationObjectDao.updateApplicationObject(applicationObject2);

		applicationObjectDaoControl.replay();

		administrationService.setApplicationDao(applicationDao);
		administrationService.setApplicationObjectDao(applicationObjectDao);

		administrationService.moveApplicationObjectViewIndex(applicationObject2, "up");
		assertEquals("applicationObject1.viewIndex", new Integer(1), applicationObject1.getViewIndex());
		assertEquals("applicationObject2.viewIndex", new Integer(0), applicationObject2.getViewIndex());
		assertEquals("applicationObject3.viewIndex", new Integer(2), applicationObject3.getViewIndex());
	}

	public void testMoveApplicationObjectViewIndex_MoveDown() {
		Long applicationId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		applicationObject1.setObjectId(new Long(1));
		applicationObject1.setViewIndex(new Integer(0));
		list.add(applicationObject1);
		ApplicationObject applicationObject2 = new ApplicationObject();
		applicationObject2.setApplicationId(applicationId);
		applicationObject2.setObjectId(new Long(2));
		applicationObject2.setViewIndex(new Integer(3));
		list.add(applicationObject2);
		ApplicationObject applicationObject3 = new ApplicationObject();
		applicationObject3.setApplicationId(applicationId);
		applicationObject3.setObjectId(new Long(3));
		applicationObject3.setViewIndex(new Integer(2));
		list.add(applicationObject3);
		applicationObjectDaoControl.setReturnValue(list);

		applicationObjectDao.updateApplicationObject(applicationObject2);
		applicationObjectDao.updateApplicationObject(applicationObject3);

		applicationObjectDaoControl.replay();

		administrationService.setApplicationDao(applicationDao);
		administrationService.setApplicationObjectDao(applicationObjectDao);

		administrationService.moveApplicationObjectViewIndex(applicationObject2, "down");
		assertEquals("applicationObject1.viewIndex", new Integer(0), applicationObject1.getViewIndex());
		assertEquals("applicationObject2.viewIndex", new Integer(2), applicationObject2.getViewIndex());
		assertEquals("applicationObject3.viewIndex", new Integer(1), applicationObject3.getViewIndex());
	}

	public void testInsertFieldDefinition_NoExisting() {
		Long objectId = new Long(1);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setObjectId(objectId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);
		administrationService.setSchemaManager(schemaManager);

		administrationService.insertFieldDefinition(fieldDefinition);
		assertEquals("fieldDefinition.viewIndex", new Integer(0), fieldDefinition.getViewIndex());
	}

	public void testInsertFieldDefinition_WithExisting() {
		Long objectId = new Long(1);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setObjectId(objectId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);
		administrationService.setSchemaManager(schemaManager);

		administrationService.insertFieldDefinition(fieldDefinition);
		assertEquals("fieldDefinition.viewIndex", new Integer(4), fieldDefinition.getViewIndex());
	}

	public void testMoveFieldDefinitionViewIndex_MoveUp() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(1));
		fieldDefinition1.setViewIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		fieldDefinition2.setViewIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		fieldDefinition3.setViewIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition1);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.moveFieldDefinitionViewIndex(fieldDefinition2, "up");
		assertEquals("fieldDefinition1.viewIndex", new Integer(1), fieldDefinition1.getViewIndex());
		assertEquals("fieldDefinition2.viewIndex", new Integer(0), fieldDefinition2.getViewIndex());
		assertEquals("fieldDefinition3.viewIndex", new Integer(2), fieldDefinition3.getViewIndex());
	}

	public void testMoveFieldDefinitionViewIndex_MoveDown() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(1));
		fieldDefinition1.setViewIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		fieldDefinition2.setViewIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		fieldDefinition3.setViewIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition3);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.moveFieldDefinitionViewIndex(fieldDefinition2, "down");
		assertEquals("fieldDefinition1.viewIndex", new Integer(0), fieldDefinition1.getViewIndex());
		assertEquals("fieldDefinition2.viewIndex", new Integer(2), fieldDefinition2.getViewIndex());
		assertEquals("fieldDefinition3.viewIndex", new Integer(1), fieldDefinition3.getViewIndex());
	}

	public void testMoveFieldDefinitionListIndex_MoveUp() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(1));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		fieldDefinition2.setListIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		fieldDefinition3.setListIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition1);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.moveFieldDefinitionListIndex(fieldDefinition2, "up");
		assertEquals("fieldDefinition1.listIndex", new Integer(1), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(0), fieldDefinition2.getListIndex());
		assertEquals("fieldDefinition3.listIndex", new Integer(2), fieldDefinition3.getListIndex());
	}

	public void testMoveFieldDefinitionListIndex_MoveDown() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(1));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		fieldDefinition2.setListIndex(new Integer(3));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		fieldDefinition3.setListIndex(new Integer(2));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition3);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.moveFieldDefinitionListIndex(fieldDefinition2, "down");
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(2), fieldDefinition2.getListIndex());
		assertEquals("fieldDefinition3.listIndex", new Integer(1), fieldDefinition3.getListIndex());
	}

	public void testAddFieldDefinitionListIndex_NoExisting() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(1));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.addFieldDefinitionListIndex(fieldDefinition2);
		assertNull("fieldDefinition1.listIndex", fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(0), fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testAddFieldDefinitionListIndex_WithExisting() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(0));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.addFieldDefinitionListIndex(fieldDefinition2);
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertEquals("fieldDefinition2.listIndex", new Integer(1), fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testRemoveFieldDefinitionListIndex() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		fieldDefinition1.setId(new Long(0));
		fieldDefinition1.setListIndex(new Integer(0));
		list.add(fieldDefinition1);
		FieldDefinition fieldDefinition2 = new FieldDefinition();
		fieldDefinition2.setObjectId(objectId);
		fieldDefinition2.setId(new Long(2));
		fieldDefinition2.setListIndex(new Integer(1));
		list.add(fieldDefinition2);
		FieldDefinition fieldDefinition3 = new FieldDefinition();
		fieldDefinition3.setObjectId(objectId);
		fieldDefinition3.setId(new Long(3));
		list.add(fieldDefinition3);
		fieldDefinitionDaoControl.setReturnValue(list);

		fieldDefinitionDao.updateFieldDefinition(fieldDefinition2);

		fieldDefinitionDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setFieldDefinitionDao(fieldDefinitionDao);

		administrationService.removeFieldDefinitionListIndex(fieldDefinition2);
		assertEquals("fieldDefinition1.listIndex", new Integer(0), fieldDefinition1.getListIndex());
		assertNull("fieldDefinition2.listIndex", fieldDefinition2.getListIndex());
		assertNull("fieldDefinition3.listIndex", fieldDefinition3.getListIndex());
	}

	public void testInsertOptionListItem_NoExisting() {
		Long optionListId = new Long(1);
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setOptionListId(optionListId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setOptionListDao(optionListDao);
		administrationService.setOptionListItemDao(optionListItemDao);

		administrationService.insertOptionListItem(optionListItem);
		assertEquals("optionListItem.viewIndex", new Integer(0), optionListItem.getViewIndex());
	}

	public void testInsertOptionListItem_WithExisting() {
		Long optionListId = new Long(1);
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setOptionListId(optionListId);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setOptionListDao(optionListDao);
		administrationService.setOptionListItemDao(optionListItemDao);

		administrationService.insertOptionListItem(optionListItem);
		assertEquals("optionListItem.viewIndex", new Integer(4), optionListItem.getViewIndex());
	}

	public void testMoveOptionListItemViewIndex_MoveUp() {
		Long optionListId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		optionListItem1.setId(new Long(1));
		optionListItem1.setOptionListId(optionListId);
		optionListItem1.setViewIndex(new Integer(0));
		list.add(optionListItem1);
		OptionListItem optionListItem2 = new OptionListItem();
		optionListItem2.setId(new Long(2));
		optionListItem2.setOptionListId(optionListId);
		optionListItem2.setViewIndex(new Integer(1));
		list.add(optionListItem2);
		OptionListItem optionListItem3 = new OptionListItem();
		optionListItem3.setId(new Long(3));
		optionListItem3.setOptionListId(optionListId);
		optionListItem3.setViewIndex(new Integer(2));
		list.add(optionListItem3);
		optionListItemDaoControl.setReturnValue(list);

		optionListItemDao.updateOptionListItem(optionListItem1);
		optionListItemDao.updateOptionListItem(optionListItem2);

		optionListItemDaoControl.replay();

		administrationService.setOptionListDao(optionListDao);
		administrationService.setOptionListItemDao(optionListItemDao);

		administrationService.moveOptionListItemViewIndex(optionListItem2, "up");
		assertEquals("optionListItem1.viewIndex", new Integer(1), optionListItem1.getViewIndex());
		assertEquals("optionListItem2.viewIndex", new Integer(0), optionListItem2.getViewIndex());
		assertEquals("optionListItem3.viewIndex", new Integer(2), optionListItem3.getViewIndex());
	}

	public void testMoveOptionListItemViewIndex_MoveDown() {
		Long optionListId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		optionListItem1.setId(new Long(1));
		optionListItem1.setOptionListId(optionListId);
		optionListItem1.setViewIndex(new Integer(0));
		list.add(optionListItem1);
		OptionListItem optionListItem2 = new OptionListItem();
		optionListItem2.setId(new Long(2));
		optionListItem2.setOptionListId(optionListId);
		optionListItem2.setViewIndex(new Integer(1));
		list.add(optionListItem2);
		OptionListItem optionListItem3 = new OptionListItem();
		optionListItem3.setId(new Long(3));
		optionListItem3.setOptionListId(optionListId);
		optionListItem3.setViewIndex(new Integer(2));
		list.add(optionListItem3);
		optionListItemDaoControl.setReturnValue(list);

		optionListItemDao.updateOptionListItem(optionListItem2);
		optionListItemDao.updateOptionListItem(optionListItem3);

		optionListItemDaoControl.replay();

		administrationService.setOptionListDao(optionListDao);
		administrationService.setOptionListItemDao(optionListItemDao);

		administrationService.moveOptionListItemViewIndex(optionListItem2, "down");
		assertEquals("optionListItem1.viewIndex", new Integer(0), optionListItem1.getViewIndex());
		assertEquals("optionListItem2.viewIndex", new Integer(2), optionListItem2.getViewIndex());
		assertEquals("optionListItem3.viewIndex", new Integer(1), optionListItem3.getViewIndex());
	}

	public void testInsertRelationship_NoExisting() {
		Long objectId1 = new Long(1);
		Long objectId2 = new Long(2);
		Relationship relationship1 = new Relationship();
		relationship1.setParentObjectId(objectId1);
		relationship1.setChildObjectId(objectId2);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setRelationshipDao(relationshipDao);
		administrationService.setSchemaManager(schemaManager);

		administrationService.insertRelationship(relationship1);
		assertEquals("relationship1.viewIndex", new Integer(0), relationship1.getViewIndex());
		Relationship relationship2 = (Relationship)matcher.actual[0];
		assertEquals("relationship2.viewIndex", new Integer(0), relationship2.getViewIndex());
	}

	public void testInsertRelationship_WithExisting() {
		Long objectId1 = new Long(1);
		Long objectId2 = new Long(2);
		Relationship relationship1 = new Relationship();
		relationship1.setParentObjectId(objectId1);
		relationship1.setChildObjectId(objectId2);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setRelationshipDao(relationshipDao);
		administrationService.setSchemaManager(schemaManager);

		administrationService.insertRelationship(relationship1);
		assertEquals("relationship1.viewIndex", new Integer(4), relationship1.getViewIndex());
		Relationship relationship2 = (Relationship)matcher.actual[0];
		assertEquals("relationship2.viewIndex", new Integer(4), relationship2.getViewIndex());
	}

	public void testMoveRelationshipViewIndex_MoveUp() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		relationship1.setId(new Long(1));
		relationship1.setParentObjectId(objectId);
		relationship1.setViewIndex(new Integer(0));
		list.add(relationship1);
		Relationship relationship2 = new Relationship();
		relationship2.setId(new Long(2));
		relationship2.setParentObjectId(objectId);
		relationship2.setViewIndex(new Integer(3));
		list.add(relationship2);
		Relationship relationship3 = new Relationship();
		relationship3.setId(new Long(3));
		relationship3.setParentObjectId(objectId);
		relationship3.setViewIndex(new Integer(2));
		list.add(relationship3);
		relationshipDaoControl.setReturnValue(list);

		relationshipDao.updateRelationship(relationship1);
		relationshipDao.updateRelationship(relationship2);

		relationshipDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setRelationshipDao(relationshipDao);

		administrationService.moveRelationshipViewIndex(relationship2, "up");
		assertEquals("relationship1.viewIndex", new Integer(1), relationship1.getViewIndex());
		assertEquals("relationship2.viewIndex", new Integer(0), relationship2.getViewIndex());
		assertEquals("relationship3.viewIndex", new Integer(2), relationship3.getViewIndex());
	}

	public void testMoveRelationshipViewIndex_MoveDown() {
		Long objectId = new Long(1);

		AdministrationServiceImpl administrationService = new AdministrationServiceImpl();

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
		relationship1.setId(new Long(1));
		relationship1.setParentObjectId(objectId);
		relationship1.setViewIndex(new Integer(0));
		list.add(relationship1);
		Relationship relationship2 = new Relationship();
		relationship2.setId(new Long(2));
		relationship2.setParentObjectId(objectId);
		relationship2.setViewIndex(new Integer(3));
		list.add(relationship2);
		Relationship relationship3 = new Relationship();
		relationship3.setId(new Long(3));
		relationship3.setParentObjectId(objectId);
		relationship3.setViewIndex(new Integer(2));
		list.add(relationship3);
		relationshipDaoControl.setReturnValue(list);

		relationshipDao.updateRelationship(relationship2);
		relationshipDao.updateRelationship(relationship3);

		relationshipDaoControl.replay();

		administrationService.setObjectDefinitionDao(objectDefinitionDao);
		administrationService.setRelationshipDao(relationshipDao);

		administrationService.moveRelationshipViewIndex(relationship2, "down");
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
