package org.programmerplanet.crm.user;

import java.util.List;
import java.util.UUID;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface UserManager {

	List<User> getUsers();

	User getUser(UUID id);

	User getUser(String username, String password);

	void saveUser(User user);
	
	void deleteUser(User user);

	boolean isUsernameUnique(UUID id, String username);

}
