package com.seyren.core.store.workers;

import java.util.HashSet;
import java.util.Set;

import com.seyren.core.store.ChecksStore;

public class GCBSWorker extends StorageAccessWorker {

	private Boolean enabled;
	
	private Set<String> states;

	public GCBSWorker(ChecksStore checksStore) {
		this.setChecksStore(checksStore);
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
