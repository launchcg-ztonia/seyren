package com.seyren.mongo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.seyren.core.domain.Alert;
import com.seyren.core.util.config.SeyrenConfig;

public class TTLTester {

	private static final Alert alert = new Alert();

	private static MongoStore mongoStore;
	
	@Mock private static SeyrenConfig config;
	
	@Before
	public void setUp() throws Exception {
		
	}


	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testTTL() {

	}

	
	

}

