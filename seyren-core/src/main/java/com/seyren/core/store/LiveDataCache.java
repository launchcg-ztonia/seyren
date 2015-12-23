package com.seyren.core.store;

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

/**
 * 
 * @author WWarren
 *
 */
public class LiveDataCache extends DataCache {

	protected LiveDataCache(){
	}

	public void setDBUpdatesEnabled(boolean enabled) {
		this.databaseSyncEnabled = enabled;
	}

	@Override
	public User addUser(User user) {
		return DataCache.mongoStore.addUser(user);
	}

	@Override
	public String[] autoCompleteUsers(String userPattern) {
		return DataCache.mongoStore.autoCompleteUsers(userPattern);
	}

	@Override
	public User getUser(String username) {
		return DataCache.mongoStore.getUser(username);
	}

	@Override
	public SubscriptionPermissions getPermissions(String name) {
		return DataCache.mongoStore.getPermissions(name);
	}

	@Override
	public void createPermissions(String name, String[] subscriptions) {
		DataCache.mongoStore.createPermissions(name, subscriptions);
	}

	@Override
	public void updatePermissions(String name, String[] subscriptions) {
		DataCache.mongoStore.updatePermissions(name, subscriptions);
	}

	@Override
	public Subscription createSubscription(String checkId, Subscription subscription) {
		return DataCache.mongoStore.createSubscription(checkId, subscription);
	}

	@Override
	public void deleteSubscription(String checkId, String subscriptionId) {
		DataCache.mongoStore.deleteSubscription(checkId, subscriptionId);
	}

	@Override
	public void updateSubscription(String checkId, Subscription subscription) {
		DataCache.mongoStore.updateSubscription(checkId, subscription);
	}

	@Override
	public Alert createAlert(String checkId, Alert alert) {
		return DataCache.mongoStore.createAlert(checkId, alert);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		return DataCache.mongoStore.getAlerts(start, items);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		return DataCache.mongoStore.getAlerts(start, items);
	}

	@Override
	public void deleteAlerts(String checkId, DateTime before) {
		DataCache.mongoStore.deleteAlerts(checkId, before);
	}

	@Override
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return DataCache.mongoStore.getLastAlertForTargetOfCheck(target, checkId);
	}

	@Override
	public SeyrenResponse getChecksByPattern(List<String> checkFields, List<Pattern> patterns, Boolean enabled) {
		return DataCache.mongoStore.getChecksByPattern(checkFields, patterns, enabled);
	}

	@Override
	public SeyrenResponse<Check> getChecks(Boolean enabled, Boolean live) {
		return DataCache.mongoStore.getChecks(enabled, live);
	}

	@Override
	public SeyrenResponse<Check> getChecksByState(Set<String> states, Boolean enabled) {
		return DataCache.mongoStore.getChecksByState(states, enabled);
	}

	@Override
	public Check getCheck(String checkId) {
		return DataCache.mongoStore.getCheck(checkId);
	}

	@Override
	public void deleteCheck(String checkId) {
		DataCache.mongoStore.deleteCheck(checkId);
	}

	@Override
	public Check createCheck(Check check) {
		return DataCache.mongoStore.createCheck(check);
	}

	@Override
	public Check saveCheck(Check check) {
		return DataCache.mongoStore.saveCheck(check);
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		return DataCache.mongoStore.updateStateAndLastCheck(checkId, state, lastCheck);
	}
}