package com.seyren.mongo;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.seyren.core.domain.Alert;
import com.seyren.core.domain.AlertType;
import com.seyren.core.util.config.SeyrenConfig;

public class TTLTester {

	private static final Alert alert = new Alert();

	private static MongoStore mongoStore;
	
	private static DB mongo;
	
	@Mock private static SeyrenConfig config;
	
	@Before
	public void setUp() throws Exception {
		config = SeyrenConfigMocker.getConfig("default");
		mongoStore = new MongoStore(new StandardPasswordEncoder(), null, null, null, config);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testTTL() {
		String checkID = "someCheckID";
		Alert alert = getAlert();
		mongoStore.createAlert(checkID, alert);
		try {
            String uri = config.getMongoUrl();
            MongoClientURI mongoClientUri = new MongoClientURI(uri);
            MongoClient mongoClient = new MongoClient(mongoClientUri);
            mongo = mongoClient.getDB(mongoClientUri.getDatabase());
            mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }		
		DBCollection alerts = mongo.getCollection("alerts");
		DBObject query = new BasicDBObject();
		query.put("id", alert.getId());
		DBCursor cursor = alerts.find(query);
		if (!cursor.hasNext()){
			fail("TTL test - Alert was never stored.");
		}
		int ttl = config.getAlertsTTL();
		try {
			Thread.sleep((ttl * 1000) + 1000);
		}
		catch (Exception e){
			fail("TTL test failed due to an Exception while attempting to pause the thread.");
		}
		DBCursor secondCursor = alerts.find(query);
		if (secondCursor.hasNext()){
			fail("TTL test failed due to Alert not being deleted.");
		}


	}
	
	private Alert getAlert(){
		Alert alert = new Alert();
		alert.setCheckId("someCheckID");
		alert.setId("theTestAlertID");
		alert.setValue(new BigDecimal(6.0));
		alert.setFromType(AlertType.OK);
		alert.setToType(AlertType.ERROR);
		DateTime now = new DateTime();
		alert.setTimestamp(now);
		return alert;
	}

	
	

}

