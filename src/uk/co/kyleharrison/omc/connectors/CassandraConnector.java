package uk.co.kyleharrison.omc.connectors;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import uk.co.kyleharrison.omc.stores.CassandraStore;
import uk.co.kyleharrison.omc.stores.ProfileStore;
import uk.co.kyleharrison.omc.stores.UserStore;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

public class CassandraConnector {
	
	ColumnFamilyTemplate<String, String> UsersTemplate;
	ColumnFamilyTemplate<String, String> LoginTemplate;
	ColumnFamilyTemplate<UUID, String> UUIDTemplate;
	ColumnFamilyTemplate<String, String> PostsTemplate;
	ColumnFamilyTemplate<String, String> SubscribedToByTemplate;
	ColumnFamilyTemplate<String, String> SubscribesToTemplate;
	ColumnFamilyTemplate<String, Long> UserPostTemplate;

	Cluster cassandraCluster;
	Keyspace cassandraKeyspace;
	String _CF;
	
	String Host; 
	String Port;
	String ClusterName;
	String KeyspaceName;
	String IP;
	
	public CassandraConnector()
	{
		CassandraStore CS = CassandraStore.instance();
		ClusterName = CS.getClusterName();
		IP = CS.getHost() +":"+ CS.getPort();
		KeyspaceName = CS.getKeyspaceName();
		
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
			
			//DEFINE TEMPLATE FOR COLUMNFAMILY
			SubscribedToByTemplate = new ThriftColumnFamilyTemplate<String, String>(cassandraKeyspace,
			"SUBSCRIBED_TO_BY",
			StringSerializer.get(),
			StringSerializer.get());	

			//DEFINE TEMPLATE FOR COLUMNFAMILY
			SubscribesToTemplate = new ThriftColumnFamilyTemplate<String, String>(cassandraKeyspace,
			"SUBSCRIBES_TO",
			StringSerializer.get(),
			StringSerializer.get());	
			
			//DEFINE TEMPLATE FOR COLUMNFAMILY
			UserPostTemplate = new ThriftColumnFamilyTemplate<String, Long>(cassandraKeyspace,
			"REVERSE_TEST",
			StringSerializer.get(),
			LongSerializer.get());	

			
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
				//System.out.println("Attempting Login");
				res = LoginTemplate.queryColumns(login_username);
			}
			catch (HectorException e)
			{
				System.out.println("Hector Exception in Attempt Login");
				return false;
			}
			
		String value = res.getString("password");
	//	System.out.println("value = "+value);
	//	System.out.println("login_password = "+login_password);
		
	    if (value != null)
	    {
		    if (value.equals(login_password))
		    {
		 //   	System.out.println("CassCon Login Success");
		    	return true;
		    }
		    else
		    	return false;
	    }else{
	    	return false;}
	}
	
	/*
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
	}*/
	
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
	
		public ProfileStore fetchProfile(String username)
		{
		String first_name;
		String surname;
		Date dob;
		String email;
		String city;
		String country;
		String avatar;
		
		ProfileStore user_profile = new ProfileStore();
		
		ColumnFamilyResult<String, String> res = null;
		boolean connected = false;
		boolean err_found = false;
		
		while (!connected)
		{
		err_found = false;
		
		try
		{
		//res = UserTemplate.queryColumns(username);
			UserStore US = new UserStore();
			US.getUserName();
		}
		catch (HectorException e)
		{
		err_found = true;	
		}
		
		if (!err_found)
		{
		connected = true;
		}
		}
		
		first_name = res.getString("first_name");
		surname = res.getString("surname");
		dob = res.getDate("dob");
		email = res.getString("email");
		city = res.getString("city");
		country = res.getString("country");
		avatar = res.getString("avatar");
		
		
		user_profile.setUsername(username);
		user_profile.setFirstName(first_name);
		user_profile.setSurname(surname);
		user_profile.setDOB(dob);
		user_profile.setEmail(email);
		user_profile.setCity(city);
		user_profile.setCountry(country);
		user_profile.setAvatar(avatar);
		
		return user_profile;
		}
		
		
		
		/*
		public LinkedList<ColumnFamilyResult<Long, String>> querySubscriptionPosts(String _username) //LinkedList<Row<Long, String, String>>
		{	
		LinkedList<ColumnFamilyResult<Long, String>> all_sub_posts = new LinkedList<ColumnFamilyResult<Long, String>>();

		//Find user's subscriptions
		ColumnFamilyResult<String, String> res = null;
		boolean connected = false;
		boolean err_found = false;


		while (!connected)
		{
		err_found = false;

		try
		{
		res = SubscribesToTemplate.queryColumns(_username);
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

		Collection<String> subscriptions = res.getColumnNames();
		for (Iterator<String> iterator = subscriptions.iterator(); iterator.hasNext();)
		{
		String sub_username = (String)iterator.next();

		ColumnFamilyResult<String, Long> sub_post_list = null;

		boolean conn = false;
		boolean err = false;

		while (!conn)
		{
		err = false;

		try
		{
		sub_post_list = UserPostTemplate.queryColumns(sub_username);
		}
		catch (HectorException e)
		{
		System.out.println(e.getMessage());
		err = true;	
		}

		if (!err)
		{
		conn = true;
		}
		}

		Collection<Long> sub_post_ids = sub_post_list.getColumnNames();

		for (Iterator<Long> post_iterator = sub_post_ids.iterator(); post_iterator.hasNext();)
		{

		String post_id = post_iterator.next();
		ColumnFamilyResult<String, String> sub_post = null;
		sub_post = PostsTemplate.queryColumns(post_id);


		System.out.println(sub_post.toString());
		all_sub_posts.add(sub_post);
		}
		}
		return all_sub_posts;
		}


		public LinkedList<ProfileStore> getSubscriptions(String _username)
		{
		LinkedList<ProfileStore> subscription_profiles = new LinkedList<ProfileStore>();
		ColumnFamilyResult<String, String> res = null;
		boolean connected = false;
		boolean err_found = false;

		while (!connected)
		{
		err_found = false;

		try
		{
		res = SubscribesToTemplate.queryColumns(_username);
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

		Collection<String> subscriptions = res.getColumnNames();
		for (Iterator<String> iterator = subscriptions.iterator(); iterator.hasNext();)
		{
		String subscription_username = iterator.next();
		System.out.println(subscription_username);
		ProfileStore profile = fetchProfile(subscription_username);
		subscription_profiles.add(profile);
		System.out.println(profile.getFirstName());
		}

		return subscription_profiles;
		}
		
		*/


		public LinkedList<ProfileStore> getSubscribers(String _username)
		{
		LinkedList<ProfileStore> subscriber_profiles = new LinkedList<ProfileStore>();
		ColumnFamilyResult<String, String> res = null;
		boolean connected = false;
		boolean err_found = false;

		while (!connected)
		{
		err_found = false;

		try
		{
		res = SubscribedToByTemplate.queryColumns(_username);
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

		Collection<String> subscribers = res.getColumnNames();
		for (Iterator<String> iterator = subscribers.iterator(); iterator.hasNext();)
		{
		String subscriber_username = iterator.next();
		System.out.println(subscriber_username);
		ProfileStore profile = fetchProfile(subscriber_username);
		subscriber_profiles.add(profile);
		System.out.println(profile.getFirstName());
		}

		return subscriber_profiles;
		}



}
