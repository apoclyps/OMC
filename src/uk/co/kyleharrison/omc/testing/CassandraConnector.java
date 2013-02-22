package uk.co.kyleharrison.omc.testing;


public class CassandraConnector {
	
	private HectorModel HectorMod;
	
	public CassandraConnector()
	{
		HectorMod = new HectorModel();
	}
	
	// FOR SERVLET
	public void insert(String columnFamily, String Key, String Column, String Value)
	{
		boolean connected = getHectorMod().establishConnection(columnFamily);
	
		if(connected)
		{
			try
			{
				//System.out.println("Template Generating:");
				getHectorMod().setupTemplate(getHectorMod().getClusterConnection().getKeyspaceString(), columnFamily);
				
				try{
				//System.out.println("Insert Attempt:");
				getHectorMod().insertKey(columnFamily,Key,Column,Value);
				}catch(Exception e)
				{
					System.out.println("Exception Inserting Record");
				}
			}catch(Exception e)
			{
				System.out.println("Exception Generating Template");
			}
		}
		connected=false;
	}
	
	// FOR SERVLET
	public void insertPost(String columnFamily, Long Key, String Column, String Value)
	{
		boolean connected = getHectorMod().establishConnection(columnFamily);
	
		if(connected)
		{
			try
			{
				//System.out.println("Template Generating:");
				getHectorMod().setupTemplate(getHectorMod().getClusterConnection().getKeyspaceString(), columnFamily);
				
				try{
				//System.out.println("Insert Attempt:");
				getHectorMod().insertKeyPost(columnFamily,Key,Column,Value);
				}catch(Exception e)
				{
					System.out.println("Exception Inserting Record");
				}
			}catch(Exception e)
			{
				System.out.println("Exception Generating Template");
			}
		}
		connected=false;
	}
	
	public boolean checkConnection(String columnFamily)
	{
		return getHectorMod().establishConnection(columnFamily);
	}
	
	public void readUser(String columnFamily, String key, String column)
	{
		boolean connected = getHectorMod().establishConnection(columnFamily);
	
		if(connected)
		{
			try
			{
				getHectorMod().setupTemplate(getHectorMod().getClusterConnection().getKeyspaceString(), columnFamily);
				boolean Record = getHectorMod().readKey(columnFamily, key, column);
				if(!Record)
					System.out.println("No Record");
			}catch(Exception e)
			{
				System.out.println("Exception Reading Record");
			}
		}
		connected=false;
	}

	public HectorModel getHectorMod() {
		return HectorMod;
	}

	public void setHectorMod(HectorModel hectorMod) {
		HectorMod = hectorMod;
	}
	
	
}
	


	



