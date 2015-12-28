package com.seyren.core.store.workers;

import com.seyren.core.domain.Check;
import com.seyren.core.store.ChecksStore;

/**
 * The DB access thread that handles the create, read, update, and delete operations 
 * for the Check objects. 
 * @author WWarren
 *
 */
public class CheckCRUDWorker extends StorageAccessWorker {

	private Check check;
	
	private String checkId;
	
	public CheckCRUDWorker(ChecksStore store) {
		this.setChecksStore(store);
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
