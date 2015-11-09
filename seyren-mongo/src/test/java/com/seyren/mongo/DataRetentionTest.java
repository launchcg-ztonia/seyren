package com.seyren.mongo;
import static com.seyren.mongo.NiceDBObject.object;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.seyren.core.domain.Alert;
import com.seyren.core.util.config.SeyrenConfig;
import com.seyren.core.util.hashing.TargetHash;

public class DataRetentionTest {
	
	@Mock Mongo mongo;
	
	@Mock DB db;
	
	@Mock MongoStore mongoStore;
	
	@Mock DBCollection alertsCollection;
	
	@Mock DBCursor cursor;
	
	@Mock SeyrenConfig config;
	
	@Mock WriteResult writeResult;
	
	private static int retainedPreviousAlerts = -1;
	
	private static final ArrayList<BasicDBObject> alerts = new ArrayList<BasicDBObject>();
	
	private static int currentIndex = 0;
	
	@Before
	public void setUp() throws Exception {
		for (int i=0;i<12;i++){
			BasicDBObject alertRecord = new BasicDBObject();
			alertRecord.put("_id",Integer.toString(i));
			alertRecord.put("warn","120");
			alertRecord.put("toType","OK");
			alertRecord.put("targetHash","�w}`pɪ�t���?%�");
			alertRecord.put("fromType","WARN");
			alertRecord.put("error","160");
			alertRecord.put("checkId","-3556770559393616786");
			alertRecord.put("value","40");
			alertRecord.put("target","carbon.agents.HQEXPEDIALinux01-a.creates");
			alertRecord.put("timestamp",new Date());
			alerts.add(alertRecord);
		}
		cursor = Mockito.mock(DBCursor.class);
		Mockito.when(cursor.next()).thenAnswer(new Answer<DBObject>() {
		     public DBObject answer(InvocationOnMock invocation) throws Throwable {
		 		DBObject next = alerts.get(currentIndex);
				currentIndex++;
				return next;
		     }
		}); 
		Mockito.when(cursor.hasNext()).thenAnswer(new Answer<Boolean>() {
		     public Boolean answer(InvocationOnMock invocation) throws Throwable {
		    	 if (currentIndex < (alerts.size() - 1)){
		    		 return true;
		    	 }
		    	 else {
		    		 return false;
		    	 }
		     }
		 });
		
		Mockito.doAnswer(new Answer<DBCursor>(){ 
			public DBCursor answer(InvocationOnMock invocation) throws Throwable {
				return cursor;
			}})
	    .when(cursor).limit(
	          Mockito.any(Integer.class));
		
		Mockito.doAnswer(new Answer<DBCursor>(){ 
			public DBCursor answer(InvocationOnMock invocation) throws Throwable {
				return cursor;
			}})
	    .when(cursor).sort(
	          Mockito.any(BasicDBObject.class));
		
		Mockito.when(cursor.sort(new BasicDBObject())).thenAnswer(new Answer<DBCursor>() {
		     public DBCursor answer(InvocationOnMock invocation) throws Throwable {
		         return cursor;
		     }
		});
		alertsCollection = Mockito.mock(DBCollection.class);
		Mockito.when(alertsCollection.remove(new BasicDBObject())).thenAnswer(new Answer<WriteResult>() {
		     public WriteResult answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Removing record #" + currentIndex);
		    	 alerts.remove(currentIndex);
		         return writeResult;
		     }
		});
		
		NiceDBObject query = NiceDBObject.object("checkId", "-3556770559393616786");
		query.append("targetHash" , ";a@cL���ivΈ8�");
		Mockito.when(alertsCollection.find(query)).thenAnswer(new Answer<DBCursor>() {
		     public DBCursor answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Finding records");
		    	 return cursor;
		     }
		});
		Mockito.when(alertsCollection.find()).thenAnswer(new Answer<DBCursor>() {
		     public DBCursor answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Finding any records");
		    	 return cursor;
		     }
		});
		
		Mockito.doAnswer(new Answer<DBCursor>(){ 
			public DBCursor answer(InvocationOnMock invocation) throws Throwable {
				return cursor;
			}})
	    .when(alertsCollection).find(
	          Mockito.any(NiceDBObject.class));
		
		config = Mockito.mock(SeyrenConfig.class);
		Mockito.when(config.getRetainedPreviousAlerts()).thenAnswer(new Answer<Integer>() {
		     public Integer answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Getting retained alerts env var");
		         return retainedPreviousAlerts;
		     }
		});
		Mockito.when(config.getMongoUrl()).thenAnswer(new Answer<String>() {
		     public String answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Getting URL of Mongo DB");
		         return "mongodb://somedomain.com:12345/seyren";
		     }
		});

		
		//Mockito.doNothing().when(mongoStore).bootstrapMongo();
		DB db = Mockito.mock(DB.class);
		Mockito.when(db.getCollection("alerts")).thenAnswer(new Answer<DBCollection>() {
		     public DBCollection answer(InvocationOnMock invocation) throws Throwable {
		    	 System.out.println("Getting the alerts collection");
		         return alertsCollection;
		     }
		});
		
		
		mongoStore = new MongoStore(config, db);
		mongoStore.setConfig(config);
		
		
		writeResult = Mockito.mock(WriteResult.class);
		Mockito.when(writeResult.getN()).thenAnswer(new Answer<Integer>() {
		     public Integer answer(InvocationOnMock invocation) throws Throwable {
		         return 1;
		     }
		});
	}


	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testRententionStrategy() {
		retainedPreviousAlerts = -1;
		mongoStore.setConfig(config);
		Alert alert = mongoStore.getLastAlertForTargetOfCheck("someTarget", "-3556770559393616786");
		assertNotNull(alert);
		assertEquals(alert.getId(),"0");
		assertEquals(alerts.size(), 12);
		config.setPreviousAlertsRetained(10);
		mongoStore.getConfig().setPreviousAlertsRetained(10);
		alert = mongoStore.getLastAlertForTargetOfCheck("someTarget", "-3556770559393616786");
		System.out.println(alerts.size());
		assertEquals(alerts.size(), 10);
	}

}
