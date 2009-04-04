package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.OptionList;
import org.programmerplanet.crm.metadata.OptionListItem;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionListItemEditController extends SimpleMultiActionFormController {

	private MetadataManager metadataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		metadataManager.saveOptionListItem(optionListItem);
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		metadataManager.deleteOptionListItem(optionListItem);
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		String direction = request.getParameter("__move");
		metadataManager.moveOptionListItemViewIndex(optionListItem, direction);
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			OptionListItem optionListItem = metadataManager.getOptionListItem(id);
			return optionListItem;
		}
		else {
			OptionListItem optionListItem = new OptionListItem();
			UUID optionListId = RequestUtil.getRequestId(request, "option_list_id");
			optionListItem.setOptionListId(optionListId);
			return optionListItem;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		Map data = new HashMap();

		OptionList optionList = metadataManager.getOptionList(optionListItem.getOptionListId());
		data.put("optionList", optionList);

		return data;
	}

}
