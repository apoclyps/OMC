package uk.co.kyleharrison.omc.model;

import uk.co.kyleharrison.omc.connectors.CassandraConnector;
import uk.co.kyleharrison.omc.model.Session;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;

public class Login {
	
	ColumnFamilyTemplate<String, String> template;
	CassandraConnector cassandraConnector = new CassandraConnector();
	String login_username = null;
	String login_password = null;
	
	public Login(String _username, String _password)
	{
		login_username = _username;
		login_password = _password;
	}
	
	public boolean setup()
	{
		return cassandraConnector.connect();
	}
	
	public boolean execute()
	{
		if (cassandraConnector.attemptLogin(login_username, login_password))
			return true;
		else
			return false;		
	}
	
	public Session createSession()
	{
		Session thisSession = new Session();
	
		thisSession.setUsername(login_username);
		
		return thisSession;
	}

}
