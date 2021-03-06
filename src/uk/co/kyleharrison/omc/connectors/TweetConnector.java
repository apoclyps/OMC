package uk.co.kyleharrison.omc.connectors;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import uk.co.kyleharrison.omc.stores.TweetStore;
import uk.co.kyleharrison.omc.utils.CassandraHosts;
import uk.co.kyleharrison.omc.utils.MyConsistancyLevel;


public class TweetConnector {
	public TweetConnector()
	{
		
	}
	
	/*
	 * Deletes a tweet and all references to it
	 */
	public void deleteTweet(String tweetID)
	{
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Can't Connect to Cassandra. Check she is OK?");
			return;
		}
		
		try{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(ks,se);
			
			TweetStore store = getTweet(tweetID);
			if (store == null) return; //stop of there it can't get the tweet				
			String username = store.getUser();
			mutator.delete(username, "UserTweets", tweetID, se);
			mutator.execute();
			mutator = HFactory.createMutator(ks,se);
			mutator.delete(tweetID, "AllTweets", null, se);
			mutator.execute();
			mutator = HFactory.createMutator(ks,se);
		}
		catch (Exception e)
		{
			System.out.println("Deleting the tweet totally failed :(" + e);
		}
	}
	
	/*
	 * Adds a tweet to the alltweets column family, and attributes it to a user.
	 */
	public boolean addTweet(TweetStore store)
	{
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Can't Connect to Cassandra. Check she is OK?");
			return false;
		}
		try
		{
			System.out.println("User to add tweet:" + store.getUser());
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(ks,se);
			Long now = System.currentTimeMillis(); // Get the current time to store
			/* Set the tweet ID as a combination of username and time, 
			 * making ordering work and guaranteeing that it is unique
			 */
			java.util.Date date= new java.util.Date();
			
			store.setTweetID(new Timestamp(date.getTime()) +" "+ store.getUser()); 
			String time = now.toString();
			
			mutator.addInsertion(store.getUser(), "UserTweets", HFactory.createStringColumn(store.getTweetID(), time));
			mutator.execute();
			mutator = HFactory.createMutator(ks,se);
			mutator.addInsertion(store.getTweetID(), "AllTweets", HFactory.createStringColumn("user", store.getUser()));
			mutator.addInsertion(store.getTweetID(), "AllTweets", HFactory.createStringColumn("content", store.getContent()));
			mutator.addInsertion(store.getTweetID(), "AllTweets", HFactory.createStringColumn("timestamp", time));
			mutator.addInsertion(store.getTweetID(), "AllTweets", HFactory.createStringColumn("tags", store.getTags()));
			mutator.execute();
	}
		catch (Exception e)
		{
			System.out.println("Adding the tweet totally failed :(" + e);
			return false;
		}
		return true;
	}

	/*
	 * Gets a single tweet based on ID.
	 */
	public TweetStore getTweet(String tweetID)
	{
		TweetStore result = new TweetStore();
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
			StringSerializer se = StringSerializer.get();
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			q.setColumnFamily("AllTweets")
			.setKey(tweetID)
			.setColumnNames("user", "content", "timestamp", "tags");
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			//result.setReplyToUser(slice.getColumnByName("replyToUser").getValue());
			String time = slice.getColumnByName("timestamp").getValue();
			if (time != null && !time.equals(""))
			{
				result.setTimeStamp(Long.parseLong(time));
			}
			result.setTweetID(tweetID);
			result.setUser(slice.getColumnByName("user").getValue());
			result.setContent(slice.getColumnByName("content").getValue());
			result.setTags(slice.getColumnByName("tags").getValue());
		}
		catch (Exception e)
		{
			System.out.println("There was a problem getting the tweet: " + e);
			return null;
		}
		return result;
	}
	
	/*
	 * Gets a users recent tweets
	 */
	public List<TweetStore> getTweets(String username)
	{
		List<TweetStore> list = new LinkedList<TweetStore>();
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
			StringSerializer se = StringSerializer.get();
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			q.setColumnFamily("UserTweets")
			.setKey(username)
			.setRange("", "", true, 50); // Get the 50 newest
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			List<HColumn<String, String>> slices = slice.getColumns();
			for (HColumn<String, String> column: slices)
			{
				TweetStore store = new TweetStore();
				store.setTweetID(column.getName());
				if (column.getValue() != null && !column.getValue().equals(""))
				{
					store.setTimeStamp(Long.parseLong(column.getValue()));
				}
				try {
					TweetStore store2 = getTweet(column.getName());
					store.setUser(store2.getUser());
					store.setContent(store2.getContent());
					store.setTags(store2.getTags());
					store.setTimeStamp(store2.getTimeStamp());
				}
				catch (Exception e)
				{
					System.out.println("Tweet Error!" + e);
				}
				list.add(store);
			}
			return list;
		}
		catch (Exception e)
		{
			System.out.println("Getting tweets failed miserably. Oh dear" + e);
			return null;
		}
	}
}
