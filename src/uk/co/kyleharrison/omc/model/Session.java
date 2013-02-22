package uk.co.kyleharrison.omc.model;

public class Session {

	private String username;
	private String full_name;
	private String avatar;
	
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
}
