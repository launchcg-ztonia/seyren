package com.seyren.mongo.cache.actionThreads;

import com.seyren.core.domain.Subscription;
import com.seyren.mongo.MongoStore;

public class SubscriptionCRUDWorker extends MongoAccessThread {
	
	private String checkId;
	
	private Subscription subscription;

	private String subscriptionId;
	
	public SubscriptionCRUDWorker(MongoStore mongoStore) {
		super(mongoStore);
	}
	
	@Override
	protected void convertParams(Object[] params) {
		switch (this.operationType){
			case CREATE: {
					this.checkId = (String)params[0];
					this.subscription = (Subscription)params[1];
					return;
				}
			case UPDATE: {
					this.checkId = (String)params[0];
					this.subscription = (Subscription)params[1];
					return;
				}
			case DELETE: {
					this.checkId = (String)params[0];
					this.subscriptionId = (String)params[1];
					return;
				}
		}
	}	

	@Override
	public void run() {
		switch (this.operationType){
			case CREATE: {
				this.createSubscription(this.checkId, this.subscription);
			}
			case UPDATE: {
				this.updateSubscription(this.checkId, this.subscription);
			}
			case DELETE: {
				this.deleteSubscription(this.checkId, this.subscriptionId);
			}
		}
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	
}
