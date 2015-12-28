package com.seyren.core.store.workers;

import com.seyren.core.store.AlertsStore;

public class GLAFTOCWorker extends StorageAccessWorker {

	private String target;
	
	private String checkId;

	public GLAFTOCWorker(AlertsStore alertsStore) {
		this.setAlertsStore(alertsStore);
	}

	@Override
	public void run() {
		this.getLastAlertForTargetOfCheck(this.target, this.checkId);
	}

	@Override
	protected void convertParams(Object[] params) {
		this.target = (String)params[0];
		this.checkId = (String)params[1];
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	
	
}
