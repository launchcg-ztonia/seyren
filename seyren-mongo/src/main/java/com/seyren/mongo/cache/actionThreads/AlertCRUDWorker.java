package com.seyren.mongo.cache.actionThreads;

import org.joda.time.DateTime;

import com.seyren.core.domain.Alert;
import com.seyren.core.domain.Check;
import com.seyren.mongo.MongoStore;

public class AlertCRUDWorker extends MongoAccessThread {

	private Alert alert;
	
	private String checkId;
	
	private int start;
	
	private int items;
	
	private DateTime before;
	
	public AlertCRUDWorker(MongoStore mongoStore) {
		super(mongoStore);
	}
	
	@Override
	protected void convertParams(Object[] params) {
		switch (this.operationType){
			case CREATE: {
				this.checkId = (String)params[0];
				this.alert = (Alert)params[1];
				return;
			}
			case READ: {
				if (params.length > 2){
					checkId = (String)params[2];
				}
				start = (Integer)params[0];
				items = (Integer)params[1];
				return;
			}
			case DELETE: {
				this.deleteAlerts(checkId, before);
				return;
			}
		}
	}	

	@Override
	public void run() {
		switch (this.operationType){
			case CREATE: {
				this.createAlert(this.checkId, this.alert);
				return;
			}
			case READ: {
				if (this.checkId == null){
					this.getAlerts(start, items);
				}
				else {
					this.getAlerts(this.checkId, this.start, this.items);
				}
				return;
			}
			case DELETE: {
				this.deleteAlerts(checkId, before);
				return;
			}
		}
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public DateTime getBefore() {
		return before;
	}

	public void setBefore(DateTime before) {
		this.before = before;
	}
	
	
	
}
