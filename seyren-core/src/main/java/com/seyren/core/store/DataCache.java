package com.seyren.core.store;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.seyren.core.util.config.SeyrenConfig;

/**
 * 
 * @author WWarren
 *
 */
public abstract class DataCache implements ChecksStore, AlertsStore, SubscriptionsStore, PermissionsStore, UserStore {

	public static final int NON_QUEUED_DATA_CACHE = 0;

	public static final int DB_QUEUE_DATA_CACHE = 1;

	public static final int LIVE_DATA_CACHE = 2;
	
	protected static AlertsStore alertsStore;
	
	protected static ChecksStore checksStore;
	
	protected static PermissionsStore permissionsStore;
	
	protected static UserStore usersStore;
	
	protected static SubscriptionsStore subscriptionsStore;

	protected Boolean databaseSyncEnabled = true;
	
	private static DataCache instance;
	
	private static int currentCacheType = LIVE_DATA_CACHE;

	private static final Logger LOGGER = LoggerFactory.getLogger(DataCache.class);
	
	protected DataCache(){
	}	
	
	public static void init(ChecksStore checksStore, AlertsStore alertsStore){
		if (instance == null){
			DataCache.checksStore = checksStore;
			DataCache.alertsStore = alertsStore;
			if (currentCacheType == DB_QUEUE_DATA_CACHE){
				DataCache.instance = new DBQueueDataCache();
			}
			else if (currentCacheType == NON_QUEUED_DATA_CACHE){
				DataCache.instance = new NonQueuedDataCache();
			}
			else {
				DataCache.instance = new LiveDataCache();
			}
		}
	}
	
	public static DataCache instance(){
		if (instance == null){
			LOGGER.error("DataCache must be initialized before an instance can be referenced.");
		}
		return instance;
	}
	
	public void setDBUpdatesEnabled(boolean enabled) {
		this.databaseSyncEnabled = enabled;
	}

	@Override
	public User addUser(User user) {
		return this.usersStore.addUser(user);
	}

	@Override
	public String[] autoCompleteUsers(String userPattern) {
		return this.usersStore.autoCompleteUsers(userPattern);
	}

	@Override
	public User getUser(String username) {
		return this.usersStore.getUser(username);
	}

	@Override
	public SubscriptionPermissions getPermissions(String name) {
		return this.permissionsStore.getPermissions(name);
	}

	@Override
	public void createPermissions(String name, String[] subscriptions) {
		this.permissionsStore.createPermissions(name, subscriptions);
	}

	@Override
	public void updatePermissions(String name, String[] subscriptions) {
		this.permissionsStore.updatePermissions(name, subscriptions);
	}

	@Override
	public Subscription createSubscription(String checkId, Subscription subscription) {
		return this.subscriptionsStore.createSubscription(checkId, subscription);
	}

	@Override
	public void deleteSubscription(String checkId, String subscriptionId) {
		this.subscriptionsStore.deleteSubscription(checkId, subscriptionId);
	}

	@Override
	public void updateSubscription(String checkId, Subscription subscription) {
		this.subscriptionsStore.updateSubscription(checkId, subscription);
	}

	@Override
	public Alert createAlert(String checkId, Alert alert) {
		return this.alertsStore.createAlert(checkId, alert);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		return this.alertsStore.getAlerts(start, items);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		return this.alertsStore.getAlerts(start, items);
	}

	@Override
	public void deleteAlerts(String checkId, DateTime before) {
		this.alertsStore.deleteAlerts(checkId, before);
	}

	@Override
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return this.alertsStore.getLastAlertForTargetOfCheck(target, checkId);
	}

	@Override
	public SeyrenResponse getChecksByPattern(List<String> checkFields, List<Pattern> patterns, Boolean enabled) {
		return this.checksStore.getChecksByPattern(checkFields, patterns, enabled);
	}

	@Override
	public SeyrenResponse<Check> getChecks(Boolean enabled, Boolean live) {
		return this.checksStore.getChecks(enabled, live);
	}

	@Override
	public SeyrenResponse<Check> getChecksByState(Set<String> states, Boolean enabled) {
		return this.checksStore.getChecksByState(states, enabled);
	}

	@Override
	public Check getCheck(String checkId) {
		return this.checksStore.getCheck(checkId);
	}

	@Override
	public void deleteCheck(String checkId) {
		this.checksStore.deleteCheck(checkId);
	}

	@Override
	public Check createCheck(Check check) {
		return this.checksStore.createCheck(check);
	}

	@Override
	public Check saveCheck(Check check) {
		return this.checksStore.saveCheck(check);
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		return this.checksStore.updateStateAndLastCheck(checkId, state, lastCheck);
	}
}