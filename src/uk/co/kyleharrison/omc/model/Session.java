package uk.co.kyleharrison.omc.model;

public class Session {

	private String userID;
	private String username;
	private String full_name;
	private String avatar;
	private boolean isActive;
	private String email;
	
	public void setUsername(String _username)
	{
		username = _username;
	}
	
	public void setFullName(String _full_name)
	{
		full_name = _full_name;
	}
	
	public void setAvatar(String _avatar)
	{
		avatar = _avatar;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getFullName()
	{
		return full_name;
	}
	
	public String getAvatar()
	{
		return avatar;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
