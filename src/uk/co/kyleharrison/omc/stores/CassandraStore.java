package uk.co.kyleharrison.omc.stores;

public class CassandraStore {
	
	private String Host = "31.220.241.162"; // Cassandra Server I.P.
	//private String Host = "127.0.0.1"; // Cassandra Server I.P.
	private String Port = "9160";
	private String ClusterName = "Test Cluster";
	private String Admin = "kyle";
	private String KeyspaceName = "OMC";
	
	public CassandraStore()
	{
	}
	
	static private CassandraStore _instance = null;
	
	static public CassandraStore instance()
	{
		if (null == _instance)
		{
			_instance = new CassandraStore();
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

	public String getKeyspaceName() {
		return KeyspaceName;
	}

	public void setKeyspaceName(String keyspaceName) {
		KeyspaceName = keyspaceName;
	}
	
}
