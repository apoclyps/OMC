package uk.co.kyleharrison.omc.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

public class CassandraConnection {
	
	ColumnFamilyTemplate<String, String> UsersTemplate;
	ColumnFamilyTemplate<String, String> LoginTemplate;
	ColumnFamilyTemplate<UUID, String> UUIDTemplate;
	ColumnFamilyTemplate<String, String> PostsTemplate;

	Cluster cassandraCluster;
	Keyspace cassandraKeyspace;
	String _CF;
	
	//String Host = "192.168.2.2"; // Cassandra Server I.P.
	String Host = "31.220.241.162"; // Cassandra Server I.P.
	String Port = "9160";
	String IP = Host+":"+Port;
	String ClusterName = "Test Cluster";
	String KeyspaceName = "OMC";
	
	public CassandraConnection()
	{
		
	}
	
	public boolean connect()
	{		
		try {
			//INITIALISE CONNECTION
			
			cassandraCluster = HFactory.getOrCreateCluster(ClusterName,IP);
			cassandraKeyspace = HFactory.createKeyspace(KeyspaceName, cassandraCluster);
					
			//DEFINE TEMPLATE FOR COLUMNFAMILY
			UsersTemplate =  new ThriftColumnFamilyTemplate<String, String>(cassandraKeyspace,"Users", StringSerializer.get(),StringSerializer.get());	
			
			//DEFINE TEMPLATE FOR COLUMNFAMILY
			LoginTemplate =  new ThriftColumnFamilyTemplate<String, String>(cassandraKeyspace,"Username",StringSerializer.get(),StringSerializer.get());	
				
			PostsTemplate =  new ThriftColumnFamilyTemplate<String, String>(cassandraKeyspace,"Posts",StringSerializer.get(),StringSerializer.get());	
			
			return true;
		}
		catch (HectorException e)
		{
			System.out.println("HectorException @ DBConnection.connect(): " + e.getMessage());
			return false;
		}
	}
	
	public boolean attemptLogin(String login_username, String login_password)
	{
		ColumnFamilyResult<String, String> res = null;

			try
			{
				System.out.println("Attempting Login");
				res = LoginTemplate.queryColumns(login_username);
			}
			catch (HectorException e)
			{
				System.out.println("Hector Exception in Attempt Login");
				return false;
			}
			
		String value = res.getString("password");
		System.out.println("value = "+value);
		System.out.println("login_password = "+login_password);
		
	    if (value != null)
	    {
		    if (value.equals(login_password))
		    {
		    	System.out.println("CassCon Login Success");
		    	return true;
		    }
		    else
		    	return false;
	    }
	    else
	    	return false;
	}
	
	public boolean getPost(String login_username)
	{
		ColumnFamilyResult<String, String> res = null;

			try
			{
				System.out.println("Attempting Login");
				//res = SinglePostTemplate.queryColumns(login_username);
			}
			catch (HectorException e)
			{
				System.out.println("Hector Exception in Attempt Login");
				return false;
			}
			
		String value = res.getString("body");
		System.out.println("value = "+value);
		
	    if (value != null)
	    {
		    	return true;
	    }
	    else
	    	return false;
	}
	
	public boolean createAccount(String _first_name, String _surname, String _username, String _password, String _email, String _avatar)
	{
		long timestamp = System.currentTimeMillis();
		// UPDATE / CREATE CODE
		// <String, String> correspond to key and Column name.
		//ColumnFamilyUpdater<UUID, String> nameUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> first_name = UsersTemplate.createUpdater(_username);
		first_name.setString("first_name", _first_name);
		first_name.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> bodyUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> surname = UsersTemplate.createUpdater(_username);
		surname.setString("surname", _surname);
		surname.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> tagsUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> email = UsersTemplate.createUpdater(_username);
		email.setString("email", _email);
		email.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> tagsUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> avatar = UsersTemplate.createUpdater(_username);
		avatar.setString("avatar", _avatar);
		avatar.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> tagsUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> password = LoginTemplate.createUpdater(_username);
		password.setString("password", _password);
		password.setLong("time", System.currentTimeMillis());

		boolean connected = false;
		boolean err_found = false;
		
		while (!connected)
		{
			err_found = false;
			
			try
			{
				UsersTemplate.update(first_name);
				UsersTemplate.update(surname);
				UsersTemplate.update(email);	
				UsersTemplate.update(avatar);	
			
				LoginTemplate.update(password);
			}
			catch (HectorException e)
			{
				System.out.println(e.getMessage());
				err_found = true;		
			}
			
			if (!err_found)
			{
				connected = true;
			}
		}
		return true;
	}
	
	public LinkedList<Row<Long, String, String>> queryPosts()
	{		
		LinkedList<Row<Long, String, String>> llist = new LinkedList<Row<Long, String, String>>();
		
		int row_count = 100;

        RangeSlicesQuery<Long, String, String> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(cassandraKeyspace, LongSerializer.get(), StringSerializer.get(), StringSerializer.get())
            .setColumnFamily("Posts")
            .setRange("", "", false, 100)
            .setRowCount(row_count);

        rangeSlicesQuery.setKeys(null, null);
        QueryResult<OrderedRows<Long, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<Long, String, String> rows = result.get();
        Iterator<Row<Long, String, String>> rowsIterator = rows.iterator();
 

        while (rowsIterator.hasNext()) 
        {
        	Row<Long, String, String> row = rowsIterator.next();

            if (row.getColumnSlice().getColumns().isEmpty()) 
                continue;

            llist.add(row);
        }

	    
	    return llist;
	}
	
	private UUID generateTimeUUID()
	{
		UUID timeUUID = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
		
		return timeUUID;
	}
		
	
	public boolean createPost(String _full_name, String _body, String _tags)
	{
		Long timestamp = System.currentTimeMillis();
		timestamp.toString();
		// UPDATE / CREATE CODE
		// <String, String> correspond to key and Column name.
		//ColumnFamilyUpdater<UUID, String> nameUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> nameUpdater = PostsTemplate.createUpdater(timestamp.toString());
		nameUpdater.setString("full_name", _full_name);
		nameUpdater.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> bodyUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> bodyUpdater = PostsTemplate.createUpdater(timestamp.toString());
		bodyUpdater.setString("body", _body);
		bodyUpdater.setLong("time", System.currentTimeMillis());
		
		//ColumnFamilyUpdater<UUID, String> tagsUpdater = UUIDTemplate.createUpdater(timeUUID);
		ColumnFamilyUpdater<String, String> tagsUpdater = PostsTemplate.createUpdater(timestamp.toString());
		tagsUpdater.setString("tags", _tags);
		tagsUpdater.setLong("time", System.currentTimeMillis());

		try {
			PostsTemplate.update(nameUpdater);
			PostsTemplate.update(bodyUpdater);
			PostsTemplate.update(tagsUpdater);		    
		} catch (HectorException e) {
			System.out.println(e.getMessage());
		    return false;
		}
		
		return true;
	}

}
