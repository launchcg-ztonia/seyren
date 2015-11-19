package com.seyren.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandFailureException;
import com.mongodb.DBCollection;

public class AlertsTTL {
	
	private static AlertsTTL instance;
	
	private int ttlInSeconds = 3600;
	
	private AlertsTTL(){
		
	}
	
	public static AlertsTTL instance(){
		if (instance == null){
			instance = new AlertsTTL();
		}
		return instance;
	}
	
	public void drop(DBCollection alertsCollection){
		BasicDBObject index = new BasicDBObject();
		index.put("timestamp", -1);
		BasicDBObject options = new BasicDBObject();
		options.put("expireAfterSeconds", this.ttlInSeconds);
		try {
			alertsCollection.dropIndex(index);
        } catch (CommandFailureException e) {
            if (e.getCode() != -5) {
                // -5 is the code which appears when the index doesn't exist (which we're happy with, anything else is bad news) 
                throw e;
            }
        }
	}
	
	public void apply(DBCollection alertsCollection){
		BasicDBObject index = new BasicDBObject();
		index.put("timestamp", -1);
		BasicDBObject options = new BasicDBObject();
		options.put("expireAfterSeconds", this.ttlInSeconds);
		try {
			alertsCollection.dropIndex(index);
        } catch (CommandFailureException e) {
            if (e.getCode() != -5) {
                // -5 is the code which appears when the index doesn't exist (which we're happy with, anything else is bad news) 
                throw e;
            }
        }
		alertsCollection.createIndex(index, options);
	}
	
	public void setTTLInSeconds(int seconds){
		this.ttlInSeconds = seconds;
	}
	
	
	
}
