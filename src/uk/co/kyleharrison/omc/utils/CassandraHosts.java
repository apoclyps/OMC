package uk.co.kyleharrison.omc.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import me.prettyprint.cassandra.service.CassandraHost;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;
import uk.co.kyleharrison.omc.stores.CassandraStore;

public final class CassandraHosts {
	static Cluster c=null;
	
	public static Cluster getCluster()
	{
			c = HFactory.getOrCreateCluster(CassandraStore.instance().getClusterName(), CassandraStore.instance().getHost() + ":" + CassandraStore.instance().getPort());
			getHosts();	
			Keyspaces.SetUpKeySpaces(c);
			return c;
	}
	
	public static String getHost() {
		return CassandraStore.instance().getHost();
	}
	
	public static String[] getHosts() {
		if (c==null){
			 System.out.println("Creating cluster connection");
			c = HFactory.getOrCreateCluster(CassandraStore.instance().getClusterName(), CassandraStore.instance().getHost() + ":" + CassandraStore.instance().getPort());
		}
		Set <CassandraHost>hosts= c.getKnownPoolHosts(false);

		String sHosts[] = new String[hosts.size()];
		Iterator<CassandraHost> it =hosts.iterator();
		int i=0;
		while (it.hasNext()) {
			CassandraHost ch=it.next();
			   
			sHosts[i]=(String)ch.getHost();
			//System.out.println("Hosts"+sHosts[i]);
			i++;
		}
		return sHosts;
	}
	
	public CassandraHosts() {
	}
	
	public List<CassandraHost> getDownedPoolHosts()
	{
		if (c==null){
			 System.out.println("Creating cluster connection");
			c = HFactory.getOrCreateCluster(CassandraStore.instance().getClusterName(), CassandraStore.instance().getHost() + ":" + CassandraStore.instance().getPort());
		}
		Set <CassandraHost>hosts= c.getConnectionManager().getDownedHosts();
		Iterator<CassandraHost> it =hosts.iterator();
		List<CassandraHost> returnHosts = new LinkedList<CassandraHost>();
		while (it.hasNext()) {
			CassandraHost ch=it.next();
			returnHosts.add(ch);
		}
		return returnHosts;
	}
	
	public List<CassandraHost> getPoolHosts()
	{
		if (c==null){
			 System.out.println("Creating cluster connection");
			c = HFactory.getOrCreateCluster(CassandraStore.instance().getClusterName(), CassandraStore.instance().getHost() + ":" + CassandraStore.instance().getPort());
		}
		Set <CassandraHost>hosts= c.getKnownPoolHosts(false);

		Iterator<CassandraHost> it =hosts.iterator();
		List<CassandraHost> returnHosts = new LinkedList<CassandraHost>();
		while (it.hasNext()) {
			
			CassandraHost ch=it.next();
			returnHosts.add(ch);
		}
		return returnHosts;
	}	
}