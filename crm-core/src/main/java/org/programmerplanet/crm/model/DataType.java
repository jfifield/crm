package org.programmerplanet.crm.model;

import java.util.List;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DataType extends ValuedEnum {

	public static final DataType SHORT_TEXT = new DataType(1, "short_text", "Short Text");
	public static final DataType LONG_TEXT = new DataType(2, "long_text", "Long Text");
	public static final DataType AUTO_NUMBER = new DataType(3, "autonumber", "Autonumber");
	public static final DataType NUMBER = new DataType(4, "number", "Number");
	public static final DataType MONEY = new DataType(5, "money", "Money");
	public static final DataType PERCENT = new DataType(6, "percent", "Percent");
	public static final DataType DATE = new DataType(7, "date", "Date");
	public static final DataType DATE_TIME = new DataType(8, "date_time", "Date & Time");
	public static final DataType BOOLEAN = new DataType(9, "boolean", "Boolean");
	public static final DataType USER = new DataType(10, "user", "User");
	public static final DataType EMAIL = new DataType(11, "email", "Email");
	public static final DataType URL = new DataType(12, "url", "URL");
	public static final DataType OPTION_LIST = new DataType(13, "option_list", "Option List");
	public static final DataType FILE = new DataType(14, "file", "File");
	public static final DataType OBJECT = new DataType(15, "object", "Object");

	private String title;

	private DataType(int value, String name, String title) {
		super(name, value);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static DataType getEnum(int value) {
		return (DataType)ValuedEnum.getEnum(DataType.class, value);
	}

	public static List<DataType> getEnumList() {
		return Enum.getEnumList(DataType.class);
	}

}
