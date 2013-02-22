package uk.co.kyleharrison.omc.model;

public class User {
	private boolean loggedIn;
	private String username;
	private String password;
	private boolean passwordConfirmation;
	
	public User()
	{
		loggedIn=false;
		username = null;
		password = null;
		passwordConfirmation = false;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword() {	
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean getPasswordConfirmation()
	{
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(boolean passwordConfirmation) 
	{
		this.passwordConfirmation = passwordConfirmation;
	}
	
	public void logout(){
		loggedIn=false;
		username = "";
	}
	public void login(String username){
		this.username=username;
		loggedIn=true;
	}
	public boolean isloggedIn(){
		System.out.println("Logged "+loggedIn + ":"+this.username);
		return loggedIn;
	}
	
	
}