package com.seyren.mongo.cache.actionThreads;

import com.seyren.core.domain.Check;
import com.seyren.mongo.MongoStore;

public class CheckCRUDWorker extends MongoAccessThread {

	private Check check;
	
	private String checkId;
	
	public CheckCRUDWorker(MongoStore mongoStore) {
		super(mongoStore);
	}
	
	@Override
	protected void convertParams(Object[] params) {
		switch (this.operationType){
			case CREATE: {
				this.check = (Check)params[0];
				return;
			}
			case READ: {
				this.checkId = (String)params[0];
				return;
			}
			case DELETE: {
				this.checkId = (String)params[0];
				return;
			}
		}
	}	

	@Override
	public void run() {
		switch (this.operationType){
			case CREATE: {
				this.createCheck(this.check);
				return;
			}
			case READ: {
				this.getCheck(this.checkId);
				return;
			}
			case DELETE: {
				this.deleteCheck(checkId);
				return;
			}
		}
	}

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	
	
}
