package com.seyren.mongo.cache.actionThreads;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import com.seyren.core.domain.Alert;
import com.seyren.core.domain.AlertType;
import com.seyren.core.domain.Check;
import com.seyren.core.domain.SeyrenResponse;
import com.seyren.core.domain.Subscription;
import com.seyren.core.domain.SubscriptionPermissions;
import com.seyren.core.domain.User;
import com.seyren.mongo.MongoStore;

public abstract class MongoAccessThread implements Runnable {

	protected final MongoStore mongoStore;
	
	public static final int CREATE = 0;
	
	public static final int READ = 1;
	
	public static final int UPDATE = 2;
	
	public static final int DELETE = 3;
	
	public int operationType = 0;
	
	public MongoAccessThread(MongoStore mongoStore){
		this.mongoStore = mongoStore;
	}
	
	public void setParams(Object[] params){
		this.convertParams(params);
	}
	
	protected abstract void convertParams(Object[] params);
	
	protected void setOperationType(int type){
		this.operationType = type;
	}
	
	public User addUser(User user) {
		return this.mongoStore.addUser(user);
	}

	
	public String[] autoCompleteUsers(String userPattern) {
		return this.mongoStore.autoCompleteUsers(userPattern);
	}

	
	public User getUser(String username) {
		return this.mongoStore.getUser(username);
	}

	
	public SubscriptionPermissions getPermissions(String name) {
		return this.mongoStore.getPermissions(name);
	}

	
	public void createPermissions(String name, String[] subscriptions) {
		this.mongoStore.createPermissions(name, subscriptions);
	}

	
	public void updatePermissions(String name, String[] subscriptions) {
		this.mongoStore.updatePermissions(name, subscriptions);
	}

	
	public Subscription createSubscription(String checkId, Subscription subscription) {
		return this.mongoStore.createSubscription(checkId, subscription);
	}

	
	public void deleteSubscription(String checkId, String subscriptionId) {
		this.mongoStore.deleteSubscription(checkId, subscriptionId);
	}

	
	public void updateSubscription(String checkId, Subscription subscription) {
		this.mongoStore.updateSubscription(checkId, subscription);
	}

	
	public Alert createAlert(String checkId, Alert alert) {
		return this.mongoStore.createAlert(checkId, alert);
	}

	
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		return this.mongoStore.getAlerts(start, items);
	}

	
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		return this.mongoStore.getAlerts(start, items);
	}

	
	public void deleteAlerts(String checkId, DateTime before) {
		this.mongoStore.deleteAlerts(checkId, before);
	}

	
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return this.mongoStore.getLastAlertForTargetOfCheck(target, checkId);
	}

	
	public SeyrenResponse getChecksByPattern(List<String> checkFields, List<Pattern> patterns, Boolean enabled) {
		return this.mongoStore.getChecksByPattern(checkFields, patterns, enabled);
	}

	
	public SeyrenResponse<Check> getChecks(Boolean enabled, Boolean live) {
		return this.mongoStore.getChecks(enabled, live);
	}

	
	public SeyrenResponse<Check> getChecksByState(Set<String> states, Boolean enabled) {
		return this.mongoStore.getChecksByState(states, enabled);
	}

	
	public Check getCheck(String checkId) {
		return this.mongoStore.getCheck(checkId);
	}

	
	public void deleteCheck(String checkId) {
		this.mongoStore.deleteCheck(checkId);
	}

	
	public Check createCheck(Check check) {
		return this.mongoStore.createCheck(check);
	}

	
	public Check saveCheck(Check check) {
		return this.mongoStore.saveCheck(check);
	}

	
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		return this.mongoStore.updateStateAndLastCheck(checkId, state, lastCheck);
	}	
}
