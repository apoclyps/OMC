package uk.co.kyleharrison.omc.testing;

import me.prettyprint.cassandra.serializers.*;
import me.prettyprint.cassandra.service.*;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;

public class Authenticate 
{	
	private Cluster cluster = HFactory.getOrCreateCluster("Test Cluster", new CassandraHostConfigurator("192.168.2.2:9160"));
	private Keyspace keyspace = HFactory.createKeyspace("OnMyCampus-Test", cluster);
	private ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
	private String Key, Password, UPassword;
	
	public Authenticate(String username, String password)
	{
		Key = username;
		Password = password;
	}
	
	public boolean setup()
	{
		if (Key.equals("") || Password.equals(""))
		{
			return false;
		}
		else
			return true;
	}
	
	public boolean verifyAuthentication()
	{
			columnQuery.setColumnFamily("Username").setKey(Key).setName("password");
			
			ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(keyspace, "Username",
			StringSerializer.get(), StringSerializer.get());
			
			ColumnFamilyResult<String, String> result = template.queryColumns(Key);
			
			UPassword = result.getString("password");
			
			if(Password.equals(UPassword))
			{
				return true;
			}
			else
			{
				return false;
			}
	}
}