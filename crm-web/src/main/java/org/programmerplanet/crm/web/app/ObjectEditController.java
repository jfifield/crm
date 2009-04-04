package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.ConversionException;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.FileInfo;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectEditController extends ObjectController {

	private MetadataManager metadataManager;
	private DataManager dataManager;
	private Map converters;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void setConverters(Map converters) {
		this.converters = converters;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (isCancelAction(request)) {
			return handleCancel(request, response);
		}
		else if (isSaveAction(request)) {
			return handleSave(request, response);
		}
		else if (isDeleteAction(request)) {
			return handleDelete(request, response);
		}
		else {
			return handleDefault(request, response);
		}
	}

	public ModelAndView handleDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		ObjectData objectData = formBackingObject(request);

		Map model = new HashMap();
		model.put("objectDefinition", objectData.getObjectDefinition());
		model.put("fieldDefinitions", objectData.getFieldDefinitions());
		model.put("data", objectData.getData());
		model.put("object", objectData);

		return new ModelAndView("edit", model);
	}

	public ModelAndView handleCancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);
		UUID id = RequestUtil.getRequestId(request);

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		String sourceObjectId = request.getParameter("source_object_id");

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = sourceObjectId;
		}
		else {
			destinationObject = objectName;
			destinationObjectId = ObjectUtils.toString(id, null);
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	public ModelAndView handleSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		ObjectData objectData = formBackingObject(request);
		BindException bindException = bindAndValidate(request, objectData);

		if (bindException.hasErrors()) {
			Map model = new HashMap();

			Map errorsModel = bindException.getModel();
			model.putAll(errorsModel);

			model.put("objectDefinition", objectData.getObjectDefinition());
			model.put("fieldDefinitions", objectData.getFieldDefinitions());
			model.put("data", objectData.getData());

			return new ModelAndView("edit", model);
		}

		// TODO: how to encapsulate this in the insert/update function?
		// insert files...
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
			Map fileMap = multipartRequest.getFileMap();
			for (Iterator i = fileMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry)i.next();
				String name = (String)entry.getKey();
				MultipartFile multipartFile = (MultipartFile)entry.getValue();
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setFileName(multipartFile.getOriginalFilename());
					fileInfo.setFileSize(multipartFile.getSize());
					fileInfo.setMimeType(multipartFile.getContentType());
					dataManager.saveFile(fileInfo, multipartFile.getInputStream());
					objectData.getData().put(name, fileInfo.getId());
				}
			}
		}

		UUID id = objectData.getId();
		boolean newObject = (id == null);
		if (newObject) {
			id = dataManager.insertObject(objectData.getObjectDefinition(), objectData.getFieldDefinitions(), objectData.getData());
		}
		else {
			dataManager.updateObject(objectData.getObjectDefinition(), objectData.getFieldDefinitions(), objectData.getData(), id);
		}

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		UUID sourceObjectId = RequestUtil.getRequestId(request, "source_object_id");

		if (StringUtils.isNotEmpty(sourceObject) && newObject) {
			String parentObjectName = sourceObject;
			String childObjectName = objectName;

			ObjectDefinition parentObjectDefinition = metadataManager.getObjectDefinition(parentObjectName);
			ObjectDefinition childObjectDefinition = metadataManager.getObjectDefinition(childObjectName);

			UUID parentId = sourceObjectId;
			UUID childId = id;

			dataManager.saveObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);
		}

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = ObjectUtils.toString(sourceObjectId, null);
		}
		else {
			destinationObject = objectName;
			destinationObjectId = ObjectUtils.toString(id, null);
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, action == add, go to view
		else if ("list".equals(source) && StringUtils.isNotEmpty(destinationObjectId) && newObject) {
			destination = "view";
		}
		// if source == list, action == update, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	private ObjectData formBackingObject(HttpServletRequest request) throws Exception {
		String objectName = getObjectName(request);

		ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectName);
		List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectView(objectDefinition);

		Map data = null;
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			data = dataManager.getObject(objectDefinition, fieldDefinitions, id);
		}
		else {
			data = new HashMap();
		}

		ObjectData objectData = new ObjectData();
		objectData.setId(id);
		objectData.setData(data);
		objectData.setObjectDefinition(objectDefinition);
		objectData.setFieldDefinitions(fieldDefinitions);

		return objectData;
	}

	private BindException bindAndValidate(HttpServletRequest request, ObjectData objectData) {
		BindException bindException = new BindException(objectData, "object");

		// convert values
		bind(request, objectData, bindException);

		// validate object
		if (!bindException.hasErrors()) {
			validate(objectData, bindException);
		}
		return bindException;
	}

	private void bind(HttpServletRequest request, ObjectData objectData, BindException bindException) {
		for (Iterator i = objectData.getFieldDefinitions().iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			Object value = request.getParameter(columnName);

			// attempt to convert value
			if (value instanceof String || value == null) {
				Converter converter = (Converter)converters.get(fieldDefinition.getDataType());
				if (converter != null) {
					try {
						value = converter.convert((String)value, fieldDefinition);
					}
					catch (ConversionException e) {
						bindException.rejectValue("data[" + columnName + "]", "typeMismatch.dataType." + fieldDefinition.getDataType().getName());
					}
				}
			}

			// set value
			objectData.getData().put(columnName, value);
		}
	}

	private void validate(ObjectData objectData, BindException bindException) {
		ObjectDataValidator objectValidator = new ObjectDataValidator();
		objectValidator.validate(objectData, bindException);
	}

	public ModelAndView handleDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);
		ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectName);
		UUID id = RequestUtil.getRequestId(request);
		dataManager.deleteObject(objectDefinition, id);

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		String sourceObjectId = request.getParameter("source_object_id");

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = sourceObjectId;
		}
		else {
			destinationObject = objectName;
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	private boolean isSaveAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__save");
	}

	private boolean isDeleteAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__delete");
	}

	private boolean isCancelAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__cancel");
	}

}
