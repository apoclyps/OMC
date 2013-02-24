package uk.co.kyleharrison.omc.connectors;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import uk.co.kyleharrison.omc.stores.UserStore;
import uk.co.kyleharrison.omc.utils.CassandraHosts;
import uk.co.kyleharrison.omc.utils.MyConsistancyLevel;


public class UserConnector {
	/*
	 * Adds a user to the database
	 */
	public boolean addUser(UserStore Author){
		Cluster c;
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception e){
			System.out.println("Couldn't connect to Cassandra. "+e.getMessage());
			return false;
		}
		try{
			if (Author.getUserName() != null && Author.getEmail() != null)
			{
				System.out.println("User " + Author.getUserName() + " Doesn't exist, adding now.");
				ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
				Keyspace ks = HFactory.createKeyspace("OMC", c);  
				ks.setConsistencyLevelPolicy(mcl);
				StringSerializer se = StringSerializer.get();
				Mutator<String> mutator = HFactory.createMutator(ks,se);
				
				mutator.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("name", Author.getName()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("username", Author.getUserName()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("location", Author.getLocation()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("joined", Author.getJoined()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("posts", Author.getPosts()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("followers", Author.getFollowers()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("followees", Author.getFollowees()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("surname", Author.getSurname()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("bio", Author.getBio()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("age", Author.getAge()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("joined", Author.getJoined()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("avatarurl", Author.getAvatarUrl()));
					
				mutator.execute();
				mutator = HFactory.createMutator(ks,se);
				mutator.addInsertion(Author.getUserName(), "Username", HFactory.createStringColumn("email", Author.getEmail()));
				mutator.addInsertion(Author.getUserName(), "Username", HFactory.createStringColumn("password", Author.getPassword()));
				mutator.execute();
				System.out.println("User "+Author.getUserName()+" Added");
				
			} else {
				System.out.println("Username taken, or there was an error reading the database");
				return false;
			}
			return true;
		}catch (Exception et){
			System.out.println("Can't add the user!" + et);
			return false;
		}
	}
		
	public UserConnector(){
	}
	
	/*
	 * Deletes a user
	 */
	public void deleteUser(String username, String email)
	{
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Couldn't connect to Cassandra."+et);
			return;
		}
		try
		{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(ks,se);
			mutator.delete(username, "UserTweets", null, se);
			mutator.execute();
			mutator = HFactory.createMutator(ks,se);
			mutator.delete(username, "Username", null, se);
			mutator.execute();
			mutator = HFactory.createMutator(ks,se);
			mutator.delete(email, "User", null, se);
			mutator.execute();
			
		} catch(Exception e)
		{
			System.out.println("Error in deleting " + e);
		}
		
	}
	
	/*
	 * Gets a user and all their attributes based on email
	 */
	public UserStore getProfileByEmail(String email) 
	{
		UserStore user = new UserStore();
		Cluster c;
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Can't connect to Cassandra. Maybe she is spending the night in Appolo's temple? -"+et);
			return null;
		}
		try
		{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			q.setColumnFamily("User").setKey(email)	.setColumnNames("name","surname","age","location", "bio", "username", "avatarurl","joined","posts","followers","followees");
			
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			
			user.login(email);
			user.setName(slice.getColumnByName("name").getValue());
			user.setSurname(slice.getColumnByName("surname").getValue());
			user.setUserName(slice.getColumnByName("username").getValue().toLowerCase());
			user.setBio(slice.getColumnByName("bio").getValue());
			user.setAge(slice.getColumnByName("age").getValue());
			user.setLocation(slice.getColumnByName("location").getValue());
			user.setAvatar(slice.getColumnByName("avatarurl").getValue());
			user.setJoinedDate(slice.getColumnByName("joined").getValue());
			user.setPosts(slice.getColumnByName("posts").getValue());
			user.setFollowers(slice.getColumnByName("followers").getValue());
			user.setFollowees(slice.getColumnByName("followees").getValue());
			
			return user;
		}catch (Exception et){
			System.out.println("Can't get user by em - "+et);
			return null;
		}
	}

	/*
	 * Gets a user and all their attributes based on email
	 */
	public UserStore getUserByEmail(String email) 
	{
		UserStore user = new UserStore();
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Can't connect to Cassandra. Maybe she is spending the night in Appolo's temple? -"+et);
			return null;
		}
		try
		{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			q.setColumnFamily("User")
			.setKey(email)
			.setColumnNames("name", "bio", "username", "avatarurl");
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			user.login(email);
			user.setName(slice.getColumnByName("name").getValue());
			user.setUserName(slice.getColumnByName("username").getValue().toLowerCase());
			user.setBio(slice.getColumnByName("bio").getValue());
			user.setAvatar(slice.getColumnByName("avatarurl").getValue());
			return user;
		}catch (Exception et){
			System.out.println("Can't get user by email :( Nicht so gut ja? - "+et);
			return null;
		}
	}
	
	/*
	 * Gets a user with attributes username and email based on username
	 */
	public UserStore getUserByUsername(String username)
	{
		UserStore user = new UserStore();
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Can't Connect to Cassandra. Check she is OK?");
			return null;
		}
		try
		{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			ColumnQuery<String, String, String> columnQuery =
				HFactory.createStringColumnQuery(ks);
			columnQuery.setColumnFamily("Username").setKey(username).setName("email");
			QueryResult<HColumn<String, String>> result = columnQuery.execute();
			user.login(result.get().getValue());
			user.setUserName(username);
			user.setEmail(result.get().getValue());
			return user;
		}catch (Exception et){
			System.out.println("Evil errors occured getting the user by username!- "+et);
			return null;
		}
	}


	/*
	 * Updates a users details
	 */
	public boolean updateUser(UserStore Author){
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Couldn't connect to Cassandra. Maybe she is ignoring you. Probably best just to apologise. "+et);
			return false;
		}
		try{
			if (this.getUserByUsername(Author.getUserName()) != null)
			{
				System.out.println("User " + Author.getUserName() + " exists, updating now.");
				ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
				Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
				ks.setConsistencyLevelPolicy(mcl);
				StringSerializer se = StringSerializer.get();
				Mutator<String> mutator = HFactory.createMutator(ks,se);
				mutator.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("name", Author.getName()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("bio", Author.getBio()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("avatarurl", Author.getAvatarUrl()));
				mutator.execute();			
			} else {
				System.out.println("Username not found");
				return false;
			}
			return true;
		}catch (Exception et){
			System.out.println("Can't update the user." + et);
			return false;
		}
	}
	
	
	/*
	 * Updates a users details
	 */
	public boolean updateUserProfile(UserStore Author){
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Couldn't connect to Cassandra. Maybe she is ignoring you. Probably best just to apologise. "+et);
			return false;
		}
		try{
			if (this.getUserByUsername(Author.getUserName()) != null)
			{
				System.out.println("User " + Author.getUserName() + " exists, updating now.");
				ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
				Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
				ks.setConsistencyLevelPolicy(mcl);
				StringSerializer se = StringSerializer.get();
				Mutator<String> mutator = HFactory.createMutator(ks,se);
				mutator.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("name", Author.getName()))
				.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("location", Author.getLocation()))
				.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("surname", Author.getSurname()))
				.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("bio", Author.getBio()))
				.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("age", Author.getAge()));
				mutator.execute();			
			} else {
				System.out.println("Username not found");
				return false;
			}
			return true;
		}catch (Exception et){
			System.out.println("Can't update the user." + et);
			return false;
		}
	}
	
}
