package uk.co.kyleharrison.omc.utils;
import me.prettyprint.hector.api.*;

import me.prettyprint.cassandra.service.OperationType;

public final class MyConsistancyLevel implements ConsistencyLevelPolicy {

	@Override
	  public  HConsistencyLevel get(OperationType op) 
	{
		switch (op){
		case READ:return HConsistencyLevel.ONE;
		case WRITE: return HConsistencyLevel.ONE;
		default: return HConsistencyLevel.QUORUM;	
		}
	  }

	  @Override
	  public HConsistencyLevel get(OperationType op, String cfName) 
	  {
		  switch (op){
		  case READ:return HConsistencyLevel.QUORUM;
		  case WRITE: return HConsistencyLevel.ONE;
		  default: return HConsistencyLevel.QUORUM; 
		  }
	  }
	}
