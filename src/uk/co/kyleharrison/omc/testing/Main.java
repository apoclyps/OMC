package uk.co.kyleharrison.omc.testing;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import uk.co.kyleharrison.omc.connectors.TweetConnector;
import uk.co.kyleharrison.omc.stores.TweetStore;
import uk.co.kyleharrison.omc.stores.UserStore;
import uk.co.kyleharrison.omc.utils.CassandraHosts;
import uk.co.kyleharrison.omc.utils.MyConsistancyLevel;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//CassandraConnector CSU = new CassandraConnector();
		//CSU.insert("Posts", "a", "body", "The diddler strikes again");
		
		/*
		UserStore Author = new UserStore();
		
		Author.setName("a");
		Author.setUserName("a2");
		Author.setEmail("a@hotmail.com");
		Author.setBio("Cool a");
		Author.setAvatar("img/avatar.png");
		Author.setPassword("a");
		addUser(Author);
		*/

		
		
		TweetConnector TC = new TweetConnector();
		TweetStore TS = new TweetStore();
		
		
		TS.setUser("a");
		TS.setTweetID("a");
		TS.setContent("New Post");
		TS.setTags("#ProgrammingGod,#MasterOfTheUniverse #YEAH");
				
		TC.addTweet(TS);
	
		/*
		TS = TC.getTweet("1361481815303a");
		System.out.println("Content = " + TS.getContent());
		*/
	}
	
	public static boolean addUser(UserStore Author){
		Cluster c; //V2
		try{
			c=CassandraHosts.getCluster();
		}catch (Exception et){
			System.out.println("Couldn't connect to Cassandra. Maybe she is ignoring you. Probably best just to apologise. "+et);
			return false;
		}
		try{
			if (Author.getUserName() != null && Author.getEmail() != null)
			{
				System.out.println("User " + Author.getUserName() + " probably Doesn't exist, adding now.");
				ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
				Keyspace ks = HFactory.createKeyspace("OMC", c);  //V2
				ks.setConsistencyLevelPolicy(mcl);
				StringSerializer se = StringSerializer.get();
				Mutator<String> mutator = HFactory.createMutator(ks,se);
				mutator.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("name", Author.getName()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("username", Author.getUserName()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("bio", Author.getBio()))
					.addInsertion(Author.getEmail(), "User", HFactory.createStringColumn("avatarurl", Author.getAvatarUrl()));
				mutator.execute();
				mutator = HFactory.createMutator(ks,se);
				mutator.addInsertion(Author.getUserName(), "Username", HFactory.createStringColumn("email", Author.getEmail()));
				mutator.addInsertion(Author.getUserName(), "Username", HFactory.createStringColumn("password", Author.getPassword()));
				mutator.execute();
				
			} else {
				System.out.println("Username taken, or there was an error reading the database");
				return false;
			}
			return true;
		}catch (Exception et){
			System.out.println("Can't add the user :( Cassandra must be in a bad mood today." + et);
			return false;
		}
	}

}
