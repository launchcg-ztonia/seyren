package com.seyren.mongo.cache.actionThreads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import com.seyren.core.domain.AlertType;
import com.seyren.mongo.MongoStore;

public class GLAFTOCWorker extends MongoAccessThread {

	private String target;
	
	private String checkId;

	public GLAFTOCWorker(MongoStore mongoStore) {
		super(mongoStore);
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
