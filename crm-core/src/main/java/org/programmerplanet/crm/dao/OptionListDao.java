package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.model.OptionList;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface OptionListDao {

	List<OptionList> getOptionLists();

	OptionList getOptionList(UUID id);

	void insertOptionList(OptionList optionList);

	void updateOptionList(OptionList optionList);

	void deleteOptionList(OptionList optionList);

	boolean isNameUnique(UUID id, String name);

}
