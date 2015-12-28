package com.seyren.core.store.workers;

import org.joda.time.DateTime;

import com.seyren.core.domain.AlertType;
import com.seyren.core.store.ChecksStore;

public class USALCWorker extends StorageAccessWorker {

	private String checkId;
	
	private AlertType state;
	
	private DateTime lastCheck;

	public USALCWorker(ChecksStore checksStore) {
		this.setChecksStore(checksStore);
	}

	@Override
	public void run() {
		this.updateStateAndLastCheck(this.checkId, this.state, this.lastCheck);
	}

	@Override
	protected void convertParams(Object[] params) {
		this.checkId = (String)params[0];
		this.state = (AlertType)params[1];
		this.lastCheck = (DateTime)params[2];
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public AlertType getState() {
		return state;
	}

	public void setState(AlertType state) {
		this.state = state;
	}

	public DateTime getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(DateTime lastCheck) {
		this.lastCheck = lastCheck;
	}

	
}
