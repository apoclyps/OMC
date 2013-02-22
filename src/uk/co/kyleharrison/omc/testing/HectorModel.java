package uk.co.kyleharrison.omc.testing;

import java.util.Arrays;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

public class HectorModel {

	private ColumnFamilyTemplate<String, String> template;
	private ColumnFamilyTemplate<String, Long> Posttemplate;
	private Connection connection;
	private KeyspaceDefinition keyspaceDef;
	private ColumnFamilyDefinition cfDef;
	private int replicationFactor = 1;
	
	public HectorModel()
	{
	}
		
	public Connection getClusterConnection() {
		return connection;
	}

	public void setClusterConnection(Connection clusterConnection) {
		connection = clusterConnection;
	}
	
	public ColumnFamilyTemplate<String, String> getTemplate() {
		return template;
	}

	public void setTemplate(ColumnFamilyTemplate<String, String> template) {
		this.template = template;
	}
	
	public void setTemplatePost(ColumnFamilyTemplate<String, Long> template) {
		this.Posttemplate = Posttemplate;
	}
	
	// USED TO SET UP DATABASE
	public void setupTemplate(String keyspace, String columnFamily)
	{
		Keyspace ksp = HFactory.createKeyspace(keyspace, connection.getCluster());
		setTemplate(new ThriftColumnFamilyTemplate<String, String>(ksp, columnFamily, StringSerializer.get(),StringSerializer.get()));
	}
	
	public void setupTemplateLong(String keyspace, long columnFamily)
	{
		Keyspace ksp = HFactory.createKeyspace(keyspace, connection.getCluster());
		//setTemplatePost(new ThriftColumnFamilyTemplate<String, Long>(ksp, columnFamily, StringSerializer.get(),LongSerializer.get()));
	}

	public void insertKey(String columnFamily, String key,String columnType, String columnValue)
	{	
		Keyspace keyspaceOperator = HFactory.createKeyspace(connection.getKeyspaceString(), connection.getCluster());
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, StringSerializer.get());
            mutator.insert(key, columnFamily, HFactory.createStringColumn(columnType, columnValue));
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
            columnQuery.setColumnFamily(columnFamily).setKey(key).setName(columnType);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();
            
            System.out.println("Attempting to insert \n\t\tKey: \t\t"+key+"\n\t\tUsing Column :\t"+columnType+"\n\t\tWith Value :\t"+columnValue);
            System.out.println("Read HColumn from Cassandra: " + result.get());
            System.out.println("Verify on CLI with: get "+connection.getKeyspaceString()+".User['"+key+"'] ");
            
        } catch (HectorException e) {
            e.printStackTrace();
        }
        //getCluster().getConnectionManager().
	}
	
	public void insertKeyPost(String columnFamily, long key,String columnType, String columnValue)
	{	
		Keyspace keyspaceOperator = HFactory.createKeyspace(connection.getKeyspaceString(), connection.getCluster());
        try {
            Mutator<Long> mutator = HFactory.createMutator(keyspaceOperator, LongSerializer.get()); 
            mutator.insert(key, columnFamily, HFactory.createStringColumn(columnType, columnValue));
          //  ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
           // columnQuery.setColumnFamily(columnFamily).setKey(key).setName(columnType);
          //  QueryResult<HColumn<String, String>> result = columnQuery.execute();
            
            System.out.println("Attempting to insert \n\t\tKey: \t\t"+key+"\n\t\tUsing Column :\t"+columnType+"\n\t\tWith Value :\t"+columnValue);
          //  System.out.println("Read HColumn from Cassandra: " + result.get());
            System.out.println("Verify on CLI with: get "+connection.getKeyspaceString()+".User['"+key+"'] ");
            
        } catch (HectorException e) {
            e.printStackTrace();
        }
        //getCluster().getConnectionManager().
	}
	

	
	//READ ATTRIBUTE OF ROW FROM KEY
	public boolean readKey(String columnFamily, String key, String column)
	{
		
		try {
			
			//setupTemplate("OnMyCampus",columnFamily);
			
		    ColumnFamilyResult<String, String> res = getTemplate().queryColumns(key);
		    String value = res.getString(column);
		    // value should be "www.datastax.com" as per our previous insertion.
		    if(!(value==null))
		    {
		    	System.out.println("\nReading "+columnFamily);
		    	System.out.println("Key : \t\t"+key +"\n"+ column+" : \t"+value);
		    	return true;
		    }
		    else
		    {
		    	return false;
		    }
		} catch (HectorException e) {
		    // do something ...
		}
		return true;
	}
	
	
	// DELETE ATTRIBUTE OF ROW
	public void deleteColumn(String Key, String Column)
	{
		try {
		    getTemplate().deleteColumn(Key, Column);
		} catch (HectorException e) {
		    // do something
		}
	}
	
	// DELETE ATTRIBUTE OF ROW
	public void deleteKey(String Key)
	{
		try {
			getTemplate().deleteRow(Key);
		} catch (HectorException e) {
		    // do something
		}
	}
	
	
	public void updateColumnFamily(String Key, String Column, String Value)
	{	
		// <String, String> correspond to key and Column name.
		ColumnFamilyUpdater<String, String> updater = getTemplate().createUpdater(Key);
		updater.setString(Column, Value);
		//updater.setLong("time", System.currentTimeMillis());

		try {
		    getTemplate().update(updater);
		} catch (HectorException e) {
		    // do something ...
		}
	}


	
	
	/*
	public void insertUser(String columnFamily, String key, String columnType, String columnValue)
	{
		
		Keyspace keyspaceOperator = HFactory.createKeyspace("OnMyCampus", getCluster());
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, StringSerializer.get());
            mutator.insert(key, columnFamily, HFactory.createStringColumn(ColumnType, ColumnValue));
            mutator.insert(key, columnFamily, HFactory.createStringColumn(ColumnType2, ColumnValue2));
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
            columnQuery.setColumnFamily(columnFamily).setKey(key).setName(ColumnType);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();
            
            System.out.println("Read HColumn from cassandra: " + result.get());
            System.out.println("Verify on CLI with: get Keyspace1.User['jsmith'] ");
            
        } catch (HectorException e) {
            e.printStackTrace();
        }
        getCluster().getConnectionManager().shutdown();
	}
	*/
	
	public boolean establishConnection(String columnFamily)
	{
		try
		{
			Cluster cluster;
			connection = new Connection();
			//setCluster(connection.getCluster());
		
			if(connection.getCluster()==null)
			{
				System.out.println("Cluster is Null - Changing to Default Cluster");
				cluster = HFactory.getOrCreateCluster("Test Cluster", "192.168.2.2:9160");
			}
			else
			{
				cluster = connection.getCluster();
			}
				
			Connection connection = new Connection();
			keyspaceDef = cluster.describeKeyspace(connection.getKeyspaceString());
			
			if (keyspaceDef == null) {
				keyspaceDef = HFactory.createKeyspaceDefinition(connection.getKeyspaceString(), ThriftKsDef.DEF_STRATEGY_CLASS,replicationFactor, Arrays.asList(cfDef));
				cfDef = HFactory.createColumnFamilyDefinition(connection.getKeyspaceString(),columnFamily, ComparatorType.BYTESTYPE);
				cluster.addKeyspace(keyspaceDef, true);	
			}
			else{
				System.out.println("Keyspace : \n\t\t"+connection.getKeyspaceString()+" Already Created");
			}
		}
		catch(Exception e)
		{
			System.out.println("Connection Exception");
			return false;
		}
		return true;
	}
	
}
