package com.seyren.mongo.cache.actionThreads;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import com.seyren.core.domain.AlertType;
import com.seyren.mongo.MongoStore;

public class GCBSWorker extends MongoAccessThread {

	private Boolean enabled;
	
	private Set<String> states;

	public GCBSWorker(MongoStore mongoStore) {
		super(mongoStore);
	}

	@Override
	public void run() {
		this.getChecksByState(this.states, this.enabled);
	}

	@Override
	protected void convertParams(Object[] params) {
		this.enabled = (Boolean)params[0];
		HashSet<String> set = new HashSet<String>();
		for (int i=1;i<params.length;i++){
			set.add((String)params[i]);
		}
		this.states = set;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<String> getStates() {
		return states;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	
}
