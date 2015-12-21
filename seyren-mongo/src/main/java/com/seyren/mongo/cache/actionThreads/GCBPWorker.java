package com.seyren.mongo.cache.actionThreads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import com.seyren.core.domain.AlertType;
import com.seyren.mongo.MongoStore;

public class GCBPWorker extends MongoAccessThread implements Runnable {

	private Boolean enabled;
	
	private List<String> checkFields = new ArrayList<String>();
	
	private List<Pattern> patterns = new ArrayList<Pattern>();

	public GCBPWorker(MongoStore mongoStore) {
		super(mongoStore);
	}

	@Override
	public void run() {
		this.getChecksByPattern(this.checkFields, this.patterns, this.enabled);
	}

	@Override
	protected void convertParams(Object[] params) {
		this.enabled = (Boolean)params[0];
		checkFields.add((String)params[1]);
		patterns.add((Pattern)params[2]);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getCheckFields() {
		return checkFields;
	}

	public void setCheckFields(List<String> checkFields) {
		this.checkFields = checkFields;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}

	
}
