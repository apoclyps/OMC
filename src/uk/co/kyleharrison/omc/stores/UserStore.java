package uk.co.kyleharrison.omc.stores;



public class UserStore {
	private boolean loggedIn = false;
	private String name, email, bio, username, avatarurl = null;
	private String password= null;
	
	public UserStore(){
	}
	
	public String getAvatarUrl() {
		return avatarurl;
	}
	public String getUserName() { return username; }
	public String getBio() { return bio; }
	public String getName(){ return name; }
	public String getEmail(){ return email;	}
	public void setName(String name) { this.name = name; }
	public void setEmail(String email) { this.email = email; }
	public void setUserName(String username) { this.username = username; }
	public void setBio(String bio) { this.bio = bio; }
	
	public void setAvatar(String avatarUrl) { 
		this.avatarurl = avatarUrl; 
		if (avatarUrl.equals("") || avatarUrl == null) {
			//this.avatarurl = "http://www.gravatar.com/avatar/" + MD5Util.md5Hex(email) + "?d=mm";
		}
		
	}
	public void logout(){
		loggedIn=false;
		email = "";
	}
	public void login(String email){
		this.email=email;
		loggedIn=true;
	}
	public boolean isloggedIn(){
		System.out.println("Logged "+loggedIn);
		return loggedIn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}