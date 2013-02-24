package uk.co.kyleharrison.omc.testing;

import uk.co.kyleharrison.omc.connectors.UserConnector;
import uk.co.kyleharrison.omc.stores.UserStore;

//Used for testing
public class Main {

	public static void main(String[] args) {
		
		addUser();
	}
	
	public static void addUser()
	{
		UserStore Author = new UserStore();
		
		Author.setName("Admin");
		Author.setUserName("admin");
		Author.setSurname("Zeus");
		Author.setEmail("admin@admin.com");
		Author.setBio("There can be only one!");
		Author.setAvatar("img/avatar.png");
		Author.setPassword("password");
		Author.setAge("42");
		Author.setJoined();
		Author.setLocation("Dundee");
		Author.setPosts("0");
		Author.setFollowees("0");
		Author.setFollowers("0");	

		UserConnector UC = new UserConnector();
		UC.addUser(Author);
		System.out.println("Admin Added");
	}

}
