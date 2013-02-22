package uk.co.kyleharrison.omc.model;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;

public class Register {
	
	ColumnFamilyTemplate<String, String> usersTemplate;
	ColumnFamilyTemplate<String, String> loginTemplate;
	
	String first_name;
	String surname;
	String username;
	String password;
	String email;
	String avatar;
	
	public Register(String _first_name, String _surname, String _username, String _password, String _email, String _avatar)
	{
		first_name = _first_name;
		surname = _surname;
		username = _username;
		password = _password;
		email = _email;
		avatar = _avatar;
		
	}
	
	public boolean execute()
	{
		CassandraConnection cassandraConnection = new CassandraConnection();

		System.out.println(first_name + surname + username + password + email + avatar);
		if (cassandraConnection.connect())
		{
			if (cassandraConnection.createAccount(first_name, surname, username, password, email, avatar))
				return true;
		}
		else
		{
			System.out.println("cassandraConnection.execute() failed.");
			return false;
		}
		return false;
	}

}
