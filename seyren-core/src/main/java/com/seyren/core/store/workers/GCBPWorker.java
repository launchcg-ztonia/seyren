package com.seyren.core.store.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.seyren.core.store.ChecksStore;

public class GCBPWorker extends StorageAccessWorker  {

	private Boolean enabled;
	
	private List<String> checkFields = new ArrayList<String>();
	
	private List<Pattern> patterns = new ArrayList<Pattern>();

	public GCBPWorker(ChecksStore store) {
		this.setChecksStore(store);
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
