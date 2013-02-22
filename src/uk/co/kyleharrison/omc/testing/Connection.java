package uk.co.kyleharrison.omc.testing;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

public class Connection {
	
	//private String Host = "192.168.2.2"; // Cassandra Server I.P.
	private String Host = "127.0.0.1"; // Cassandra Server I.P.
	private String Port = "9160";
	private String ClusterName = "Test Cluster";
	private String Admin = "kyle";
	static private Connection _instance = null;
	private String Keyspace = "OnMyCampusTest";
	
	public Connection()
	{

	}
	
	static public Connection instance()
	{
		if (null == _instance)
		{
			_instance = new Connection();
		}
		return _instance;
	}
	
	public void setClusterName(String clusterName) {
		ClusterName = clusterName;
	}
	public String getClusterName() {
		return ClusterName;
	}
	public void setPort(String port) {
		Port = port;
	}

	public String getPort() {
		return Port;
	}
	public void setHost(String host) {
		Host = host;
	}

	public String getHost() {
		return Host;
	}
	
	public String getAdmin()
	{
		return Admin;
	}

	public void setKeyspace(String keyspace) {
		Keyspace = keyspace;
	}
	
	public Cluster getCluster()
	{
		return HFactory.getOrCreateCluster(ClusterName, (Host+":"+Port));
	}
	
	public String getKeyspaceString()
	{
		return Keyspace;
	}
	
	public Keyspace getKeyspace()
	{
		SetUpKeySpaces(getCluster());
		return HFactory.createKeyspace(getKeyspaceString(), getCluster());
	}
	
	public void SetUpKeySpaces(Cluster c){
		try{
			try{
				@SuppressWarnings("unused")
				KeyspaceDefinition kd =c.describeKeyspace(getKeyspaceString());
			}catch(Exception et){
				System.out.println("Keyspace probably doesn't exist, trying to create it" + et);
				List<ColumnFamilyDefinition> cfs = new ArrayList<ColumnFamilyDefinition>();
				BasicColumnFamilyDefinition cf = new BasicColumnFamilyDefinition();

				//Setup Column Families in keyspace.
				
				cf.setName("Users");
				cf.setKeyspaceName(getKeyspaceString());
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				ColumnFamilyDefinition cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);
				
				cf.setName("Subscriptions");
				cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);
				
				cf.setName("UserTweets");
				cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);
				
				cf.setName("Followers");
				cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);
				
				cf.setName("Tweets");
				cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);

				KeyspaceDefinition ks=HFactory.createKeyspaceDefinition(getKeyspaceString(),"org.apache.cassandra.locator.SimpleStrategy", 1, cfs);
				c.addKeyspace(ks);
			}


		}catch(Exception et){
			System.out.println("Other keyspace or column definition error" +et);
		}

	}

}
