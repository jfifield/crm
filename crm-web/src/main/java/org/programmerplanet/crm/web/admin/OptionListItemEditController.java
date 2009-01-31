package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.programmerplanet.crm.service.AdministrationService;
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

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;

		if (optionListItem.getId() != null) {
			administrationService.updateOptionListItem(optionListItem);
		}
		else {
			administrationService.insertOptionListItem(optionListItem);
		}

		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		administrationService.deleteOptionListItem(optionListItem);
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		String direction = request.getParameter("__move");
		administrationService.moveOptionListItemViewIndex(optionListItem, direction);
		return new ModelAndView(getSuccessView(), "id", optionListItem.getOptionListId());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long id = RequestUtil.getRequestId(request);
		if (id != null) {
			OptionListItem optionListItem = administrationService.getOptionListItem(id);
			return optionListItem;
		}
		else {
			OptionListItem optionListItem = new OptionListItem();
			Long optionListId = RequestUtil.getRequestId(request, "option_list_id");
			optionListItem.setOptionListId(optionListId);
			return optionListItem;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		OptionListItem optionListItem = (OptionListItem)command;
		Map data = new HashMap();

		OptionList optionList = administrationService.getOptionList(optionListItem.getOptionListId());
		data.put("optionList", optionList);

		return data;
	}

}
