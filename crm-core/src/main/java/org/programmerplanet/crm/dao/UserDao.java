package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.model.User;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface UserDao {

	List<User> getAllUsers();

	User getUser(UUID id);
	
	User getUser(String username, String password);

	void insertUser(User user);

	void updateUser(User user);

	void deleteUser(User user);
	
	boolean isUsernameUnique(UUID id, String username);

}
