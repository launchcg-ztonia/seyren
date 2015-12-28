package com.seyren.core.store.workers;

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
import com.seyren.core.store.AlertsStore;
import com.seyren.core.store.ChecksStore;
import com.seyren.core.store.PermissionsStore;
import com.seyren.core.store.SubscriptionsStore;
import com.seyren.core.store.UserStore;

/**
 * The Storage Access Worker is essentially a packaged operation designed 
 * to operate on the persistence layer, but runnable in a thread that is
 * separate from the main execution Thread.
 * @author WWarren
 *
 */
public abstract class StorageAccessWorker implements Runnable {
	/** A static int flag used to indicate that, when run, this worker should
	 * execute its 'Create' type of operation */
	public static final int CREATE = 0;
	/** A static int flag used to indicate that, when run, this worker should
	 * execute its 'Read' type of operation */
	public static final int READ = 1;
	/** A static int flag used to indicate that, when run, this worker should
	 * execute its 'Update' type of operation */
	public static final int UPDATE = 2;
	/** A static int flag used to indicate that, when run, this worker should
	 * execute its 'Delete' type of operation */
	public static final int DELETE = 3;
	/** The operation type (from the static integers above) that will be executed when 
	 * this worker is run */
	public int operationType = 0;
	/** A cached instance of the alerts store, providing DB access */
	protected AlertsStore alertsStore;
	/** A cached instance of the checks store, providing DB access */
	protected ChecksStore checksStore;
	/** A cached instance of the subscriptions store, providing DB access */
	protected SubscriptionsStore subscriptionsStore;
	/** A cached instance of the users store, providing DB access */
	protected UserStore usersStore;
	/** A cached instance of the permissions store, providing DB access */
	protected PermissionsStore permissionsStore;
	
	public StorageAccessWorker(){
		
	}
	
	
	
	public AlertsStore getAlertsStore() {
		return alertsStore;
	}



	public void setAlertsStore(AlertsStore alertsStore) {
		this.alertsStore = alertsStore;
	}



	public ChecksStore getChecksStore() {
		return checksStore;
	}



	public void setChecksStore(ChecksStore checksStore) {
		this.checksStore = checksStore;
	}



	public SubscriptionsStore getSubscriptionsStore() {
		return subscriptionsStore;
	}



	public void setSubscriptionsStore(SubscriptionsStore subscriptionsStore) {
		this.subscriptionsStore = subscriptionsStore;
	}

	public UserStore getUsersStore() {
		return usersStore;
	}



	public void setUsersStore(UserStore usersStore) {
		this.usersStore = usersStore;
	}



	public PermissionsStore getPermissionsStore() {
		return permissionsStore;
	}



	public void setPermissionsStore(PermissionsStore permissionsStore) {
		this.permissionsStore = permissionsStore;
	}



	public void setParams(Object[] params){
		this.convertParams(params);
	}
	
	protected abstract void convertParams(Object[] params);
	
	protected void setOperationType(int type){
		this.operationType = type;
	}
	
	public User addUser(User user) {
		return this.usersStore.addUser(user);
	}

	
	public String[] autoCompleteUsers(String userPattern) {
		return this.usersStore.autoCompleteUsers(userPattern);
	}

	
	public User getUser(String username) {
		return this.usersStore.getUser(username);
	}

	
	public SubscriptionPermissions getPermissions(String name) {
		return this.permissionsStore.getPermissions(name);
	}

	
	public void createPermissions(String name, String[] subscriptions) {
		this.permissionsStore.createPermissions(name, subscriptions);
	}

	
	public void updatePermissions(String name, String[] subscriptions) {
		this.permissionsStore.updatePermissions(name, subscriptions);
	}

	
	public Subscription createSubscription(String checkId, Subscription subscription) {
		return this.subscriptionsStore.createSubscription(checkId, subscription);
	}

	
	public void deleteSubscription(String checkId, String subscriptionId) {
		this.subscriptionsStore.deleteSubscription(checkId, subscriptionId);
	}

	
	public void updateSubscription(String checkId, Subscription subscription) {
		this.subscriptionsStore.updateSubscription(checkId, subscription);
	}

	
	public Alert createAlert(String checkId, Alert alert) {
		return this.alertsStore.createAlert(checkId, alert);
	}

	
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		return this.alertsStore.getAlerts(start, items);
	}

	
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		return this.alertsStore.getAlerts(start, items);
	}

	
	public void deleteAlerts(String checkId, DateTime before) {
		this.alertsStore.deleteAlerts(checkId, before);
	}

	
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return this.alertsStore.getLastAlertForTargetOfCheck(target, checkId);
	}

	
	public SeyrenResponse getChecksByPattern(List<String> checkFields, List<Pattern> patterns, Boolean enabled) {
		return this.checksStore.getChecksByPattern(checkFields, patterns, enabled);
	}

	
	public SeyrenResponse<Check> getChecks(Boolean enabled, Boolean live) {
		return this.checksStore.getChecks(enabled, live);
	}

	
	public SeyrenResponse<Check> getChecksByState(Set<String> states, Boolean enabled) {
		return this.checksStore.getChecksByState(states, enabled);
	}

	
	public Check getCheck(String checkId) {
		return this.checksStore.getCheck(checkId);
	}

	
	public void deleteCheck(String checkId) {
		this.checksStore.deleteCheck(checkId);
	}

	
	public Check createCheck(Check check) {
		return this.checksStore.createCheck(check);
	}

	
	public Check saveCheck(Check check) {
		return this.checksStore.saveCheck(check);
	}

	
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		return this.checksStore.updateStateAndLastCheck(checkId, state, lastCheck);
	}	
}
