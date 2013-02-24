package uk.co.kyleharrison.omc.utils;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import me.prettyprint.hector.api.ddl.KeyspaceDefinition;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.service.*;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.*;

public final class Keyspaces {
	public Keyspaces(){
	}
	public static void SetUpKeySpaces(Cluster c){
		try{
				KeyspaceDefinition keyspaceDef = c.describeKeyspace("OMC");
				if (keyspaceDef == null) {
					createSchema(c);
				}
			
		}catch(Exception et){
			System.out.println("Other keyspace or column definition error" +et);
		}
	}
	
	private static void createSchema(Cluster c)
	{
		System.out.println("Keyspace probably doesn't exist, trying to create it!" );
		
		List<ColumnFamilyDefinition> cfs = new ArrayList<ColumnFamilyDefinition>(); 
		
		BasicColumnFamilyDefinition user = new BasicColumnFamilyDefinition(); 
		BasicColumnFamilyDefinition username = new BasicColumnFamilyDefinition(); 
		BasicColumnFamilyDefinition alltweets = new BasicColumnFamilyDefinition();
		BasicColumnFamilyDefinition usertweets = new BasicColumnFamilyDefinition();

		user.setName("User");
		username.setName("Username");
		alltweets.setName("AllTweets");
		usertweets.setName("UserTweets");

		user.setKeyspaceName("OMC");
		username.setKeyspaceName("OMC");
		alltweets.setKeyspaceName("OMC");
		usertweets.setKeyspaceName("OMC");

		user.setComparatorType(ComparatorType.BYTESTYPE);
		username.setComparatorType(ComparatorType.BYTESTYPE);
		alltweets.setComparatorType(ComparatorType.BYTESTYPE);
		usertweets.setComparatorType(ComparatorType.BYTESTYPE);
	
		
		ColumnFamilyDefinition userdef = new ThriftCfDef(user); 
		ColumnFamilyDefinition usernamedef = new ThriftCfDef(username);
		ColumnFamilyDefinition alltweetsdef = new ThriftCfDef(alltweets); 
		ColumnFamilyDefinition usertweetsdef = new ThriftCfDef(usertweets);
		
		cfs.add(userdef);
		cfs.add(usernamedef);
		cfs.add(alltweetsdef);
		cfs.add(usertweetsdef);
		
		KeyspaceDefinition ks=HFactory.createKeyspaceDefinition("OMC","org.apache.cassandra.locator.SimpleStrategy", 1, cfs);
		c.addKeyspace(ks);
		System.out.println("Keyspace created?");
		
		try{
			c.describeKeyspace("OMC");
		}catch(Exception e){
			System.out.println("Keyspace not created!" +e.getMessage());
		}
	}
}
