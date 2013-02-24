package uk.co.kyleharrison.omc.connectors;

import java.util.Iterator;
import java.util.LinkedList;

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
import uk.co.kyleharrison.omc.stores.CassandraStore;
import uk.co.kyleharrison.omc.stores.UserStore;

public class CassandraConnector {

	private Cluster cassandraCluster;
	private Keyspace cassandraKeyspace;
	private String ClusterName;
	private String IP;
	private String KeyspaceName;
	private ColumnFamilyTemplate<String, String> LoginTemplate;
	private ColumnFamilyTemplate<String, String> PostsTemplate;
	private ColumnFamilyTemplate<String, String> UsersTemplate;

	public CassandraConnector() {
		CassandraStore CS = CassandraStore.instance();
		ClusterName = CS.getClusterName();
		IP = CS.getHost() + ":" + CS.getPort();
		KeyspaceName = CS.getKeyspaceName();
	}

	public boolean attemptLogin(String login_username, String login_password) {
		ColumnFamilyResult<String, String> res = null;

		try {
			res = LoginTemplate.queryColumns(login_username);
		} catch (HectorException e) {
			System.out.println("Hector Exception in Attempt Login");
			return false;
		}

		if (res.getString("password") != null
				&& res.getString("password").equals(login_password))
			return true;
		else
			return false;
	}

	public boolean connect() {
		try {

			cassandraCluster = HFactory.getOrCreateCluster(ClusterName, IP);
			cassandraKeyspace = HFactory.createKeyspace(KeyspaceName,
					cassandraCluster);

			UsersTemplate = new ThriftColumnFamilyTemplate<String, String>(
					cassandraKeyspace, "Users", StringSerializer.get(),
					StringSerializer.get());
			LoginTemplate = new ThriftColumnFamilyTemplate<String, String>(
					cassandraKeyspace, "Username", StringSerializer.get(),
					StringSerializer.get());

			PostsTemplate = new ThriftColumnFamilyTemplate<String, String>(
					cassandraKeyspace, "Posts", StringSerializer.get(),
					StringSerializer.get());

			return true;
		} catch (HectorException e) {
			System.out.println("HectorException @ DBConnection.connect(): "
					+ e.getMessage());
			return false;
		}
	}

	public boolean createAccount(String _first_name, String _surname,
			String _username, String _password, String _email, String _avatar) {
		ColumnFamilyUpdater<String, String> first_name = UsersTemplate
				.createUpdater(_username);
		first_name.setString("first_name", _first_name);
		first_name.setLong("time", System.currentTimeMillis());

		ColumnFamilyUpdater<String, String> surname = UsersTemplate
				.createUpdater(_username);
		surname.setString("surname", _surname);
		surname.setLong("time", System.currentTimeMillis());

		ColumnFamilyUpdater<String, String> email = UsersTemplate
				.createUpdater(_username);
		email.setString("email", _email);
		email.setLong("time", System.currentTimeMillis());
		
		ColumnFamilyUpdater<String, String> avatar = UsersTemplate
				.createUpdater(_username);
		avatar.setString("avatar", _avatar);
		avatar.setLong("time", System.currentTimeMillis());

		ColumnFamilyUpdater<String, String> password = LoginTemplate
				.createUpdater(_username);
		password.setString("password", _password);
		password.setLong("time", System.currentTimeMillis());

		try {
			UsersTemplate.update(first_name);
			UsersTemplate.update(surname);
			UsersTemplate.update(email);
			UsersTemplate.update(avatar);
			LoginTemplate.update(password);
		} catch (HectorException e) {
			System.out.println(e.getMessage());
		}

		return true;
	}

	public boolean createPost(String _full_name, String _body, String _tags) {
		Long timestamp = System.currentTimeMillis();
		timestamp.toString();

		ColumnFamilyUpdater<String, String> nameUpdater = PostsTemplate
				.createUpdater(timestamp.toString());
		nameUpdater.setString("full_name", _full_name);
		nameUpdater.setLong("time", System.currentTimeMillis());

		ColumnFamilyUpdater<String, String> bodyUpdater = PostsTemplate
				.createUpdater(timestamp.toString());
		bodyUpdater.setString("body", _body);
		bodyUpdater.setLong("time", System.currentTimeMillis());

		ColumnFamilyUpdater<String, String> tagsUpdater = PostsTemplate
				.createUpdater(timestamp.toString());
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

	public UserStore fetchProfile(String email) {
		UserStore user_profile = new UserStore();

		try {
			ColumnFamilyResult<String, String> res = null;

			try {
				res = UsersTemplate.queryColumns(email);
			} catch (HectorException e) {
				System.out.println("Hector Exception in Attempt Login");
			}

			user_profile.setName(res.getString("name"));
			user_profile.setSurname(res.getString("surname"));
			user_profile.setAge(res.getString("age"));
			user_profile.setLocation(res.getString("location"));
			user_profile.setAvatar(res.getString("avatar"));

		} catch (HectorException e) {
			System.out.println("Error in Profile Connector " + e.getMessage());
		}
		return user_profile;
	}

	public ColumnFamilyTemplate<String, String> getUsersTemplate() {
		return UsersTemplate;
	}
	
	public LinkedList<Row<Long, String, String>> queryPosts() {
		LinkedList<Row<Long, String, String>> list = new LinkedList<Row<Long, String, String>>();

		int row_count = 100;

		RangeSlicesQuery<Long, String, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(cassandraKeyspace,
						LongSerializer.get(), StringSerializer.get(),
						StringSerializer.get()).setColumnFamily("Posts")
				.setRange("", "", false, 100).setRowCount(row_count);

		rangeSlicesQuery.setKeys(null, null);
		QueryResult<OrderedRows<Long, String, String>> result = rangeSlicesQuery
				.execute();
		OrderedRows<Long, String, String> rows = result.get();
		Iterator<Row<Long, String, String>> rowsIterator = rows.iterator();

		while (rowsIterator.hasNext()) {
			Row<Long, String, String> row = rowsIterator.next();

			if (row.getColumnSlice().getColumns().isEmpty())
				continue;

			list.add(row);
		}
		return list;
	}

	public void setUsersTemplate(
			ColumnFamilyTemplate<String, String> usersTemplate) {
		UsersTemplate = usersTemplate;
	}

}
