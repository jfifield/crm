package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface OptionListItemDao {

	List getOptionListItems(OptionList optionList);

	OptionListItem getOptionListItem(UUID id);

	void insertOptionListItem(OptionListItem optionListItem);

	void updateOptionListItem(OptionListItem optionListItem);

	void deleteOptionListItem(OptionListItem optionListItem);

	boolean isValueUnique(UUID optionListId, UUID id, String value);

}
