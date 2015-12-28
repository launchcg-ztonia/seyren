package com.seyren.core.store;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * The Data Cache provides an alternative means of interacting with the persistence layer of 
 * the Seyren application.  Instead of writing and reading to/from the database directly, children
 * of this class can reroute such operations in the name of efficiency.  Some child classes will
 * draw data from a cache, while others will queue database operations in groups for subsequent 
 * execution.
 * @author WWarren
 *
 */
public abstract class DataCache implements ChecksStore, AlertsStore, SubscriptionsStore, PermissionsStore, UserStore {
	/** The static flag to indicate the usage of the NonQueuedDataCache instance */
	public static final int SIMPLE_IN_MEMORY_CACHE = 0;
	/** The static flag to indicate the usage of the DBQueuedDataCache instance */
	public static final int DB_QUEUE_DATA_CACHE = 1;
	/** The static flag to indicate the usage of the LiveDataCache instance */
	public static final int LIVE_DATA_CACHE = 2;
	/** A cached instance of the alerts store, providing DB access */
	protected AlertsStore alertsStore;
	/** A cached instance of the checks store, providing DB access */
	protected ChecksStore checksStore;
	/** A cached instance of the permissions store, providing DB access */
	protected PermissionsStore permissionsStore;
	/** A cached instance of the users store, providing DB access */
	protected UserStore usersStore;
	/** A cached instance of the subscriptions store, providing DB access */
	protected SubscriptionsStore subscriptionsStore;
	/** A flag which indicates whether requests will pass through to the persistence layer.  This 
	 * switch can be useful for testing scenarios. */
	protected Boolean databaseSyncEnabled = true;
	/** The singleton instance for this class */
	private static DataCache instance;
	/** The current type of cache implemented by the instance */
	private static int currentCacheType = LIVE_DATA_CACHE;
	/** The logger used */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCache.class);

	/**
	 * Initializes the particular cache used for subsequent operations
	 * @param usersStore The persistence module used to handle User data
	 * @param permissionsStore The persistence module used to handle security and privilege data
	 * @param altertsStore The persistence module used to handle Alert data
	 * @param subscriptionsStore The persistence module used to handle Subscription data
	 * @param checksStore The persistence module used to handle Check data
	 */
	public static void init(UserStore usersStore, PermissionsStore permissionsStore, AlertsStore altertsStore,
			SubscriptionsStore subscriptionsStore, ChecksStore checksStore){
		if (instance == null){
			if (currentCacheType == DB_QUEUE_DATA_CACHE){
				DataCache.instance = new DBQueueDataCache();
			}
			else if (currentCacheType == SIMPLE_IN_MEMORY_CACHE){
				DataCache.instance = new SimpleInMemoryDataCache();
			}
			else {
				DataCache.instance = new LiveDataCache();
			}
		}
		DataCache.instance.usersStore = usersStore;
		DataCache.instance.permissionsStore = permissionsStore;
		DataCache.instance.alertsStore = altertsStore;
		DataCache.instance.subscriptionsStore = subscriptionsStore;
		DataCache.instance.checksStore = checksStore;
		
	}
	
	/**
	 * Get the singleton instance of this class
	 * @return The singleton instance
	 */
	public static DataCache instance(){
		if (instance == null){
			LOGGER.error("DataCache must be initialized before an instance can be referenced.");
		}
		return instance;
	}
	

	public static int getCurrentCacheType() {
		return currentCacheType;
	}

	public static void setCurrentCacheType(int currentCacheType) {
		DataCache.currentCacheType = currentCacheType;
	}

	public AlertsStore getAlertsStore() {
		return alertsStore;
	}

	public ChecksStore getChecksStore() {
		return checksStore;
	}

	public PermissionsStore getPermissionsStore() {
		return permissionsStore;
	}

	public UserStore getUsersStore() {
		return usersStore;
	}

	public SubscriptionsStore getSubscriptionsStore() {
		return subscriptionsStore;
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