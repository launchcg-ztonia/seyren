package com.seyren.mongo.cache;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.seyren.core.domain.Alert;
import com.seyren.core.domain.AlertType;
import com.seyren.core.domain.Check;
import com.seyren.core.domain.SeyrenResponse;
import com.seyren.core.domain.Subscription;
import com.seyren.core.domain.SubscriptionPermissions;
import com.seyren.core.domain.User;
import com.seyren.core.util.config.SeyrenConfig;

/**
 * 
 * @author WWarren
 *
 */
public abstract class LiveDataCache extends DataCache {

	public LiveDataCache(PasswordEncoder passwordEncoder, @Value("${admin.username}") String adminUsername,
			@Value("${admin.password}") String adminPassword,
			@Value("${authentication.service}") String serviceProvider, SeyrenConfig seyrenConfig) {
		super(passwordEncoder, adminUsername, adminPassword, serviceProvider, seyrenConfig);
	}

	public void setDBUpdatesEnabled(boolean enabled) {
		this.databaseSyncEnabled = enabled;
	}

	@Override
	public User addUser(User user) {
		return this.mongoStore.addUser(user);
	}

	@Override
	public String[] autoCompleteUsers(String userPattern) {
		return this.mongoStore.autoCompleteUsers(userPattern);
	}

	@Override
	public User getUser(String username) {
		return this.mongoStore.getUser(username);
	}

	@Override
	public SubscriptionPermissions getPermissions(String name) {
		return this.mongoStore.getPermissions(name);
	}

	@Override
	public void createPermissions(String name, String[] subscriptions) {
		this.mongoStore.createPermissions(name, subscriptions);
	}

	@Override
	public void updatePermissions(String name, String[] subscriptions) {
		this.mongoStore.updatePermissions(name, subscriptions);
	}

	@Override
	public Subscription createSubscription(String checkId, Subscription subscription) {
		return this.mongoStore.createSubscription(checkId, subscription);
	}

	@Override
	public void deleteSubscription(String checkId, String subscriptionId) {
		this.mongoStore.deleteSubscription(checkId, subscriptionId);
	}

	@Override
	public void updateSubscription(String checkId, Subscription subscription) {
		this.mongoStore.updateSubscription(checkId, subscription);
	}

	@Override
	public Alert createAlert(String checkId, Alert alert) {
		return this.mongoStore.createAlert(checkId, alert);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		return this.mongoStore.getAlerts(start, items);
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		return this.mongoStore.getAlerts(start, items);
	}

	@Override
	public void deleteAlerts(String checkId, DateTime before) {
		this.mongoStore.deleteAlerts(checkId, before);
	}

	@Override
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return this.mongoStore.getLastAlertForTargetOfCheck(target, checkId);
	}

	@Override
	public SeyrenResponse getChecksByPattern(List<String> checkFields, List<Pattern> patterns, Boolean enabled) {
		return this.mongoStore.getChecksByPattern(checkFields, patterns, enabled);
	}

	@Override
	public SeyrenResponse<Check> getChecks(Boolean enabled, Boolean live) {
		return this.mongoStore.getChecks(enabled, live);
	}

	@Override
	public SeyrenResponse<Check> getChecksByState(Set<String> states, Boolean enabled) {
		return this.mongoStore.getChecksByState(states, enabled);
	}

	@Override
	public Check getCheck(String checkId) {
		return this.mongoStore.getCheck(checkId);
	}

	@Override
	public void deleteCheck(String checkId) {
		this.mongoStore.deleteCheck(checkId);
	}

	@Override
	public Check createCheck(Check check) {
		return this.mongoStore.createCheck(check);
	}

	@Override
	public Check saveCheck(Check check) {
		return this.mongoStore.saveCheck(check);
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		return this.mongoStore.updateStateAndLastCheck(checkId, state, lastCheck);
	}
}