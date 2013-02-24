package uk.co.kyleharrison.omc.model;

// Validation of Register - Not Implemented 
public class User {
	private boolean loggedIn;
	private String username;
	private String surname;
	private String password;
	private boolean passwordConfirmation;
	
	public User()
	{
		loggedIn=false;
		username = null;
		setSurname(null);
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
}