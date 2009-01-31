package org.programmerplanet.crm.model;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class User extends Entity {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private boolean administrator;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public boolean isAdministrator() {
		return administrator;
	}

}
