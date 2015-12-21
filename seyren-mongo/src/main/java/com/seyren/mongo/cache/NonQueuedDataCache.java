package com.seyren.mongo.cache;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.ArrayListMultimap;
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
public abstract class NonQueuedDataCache extends DataCache {

	private static final HashMap<String, Check> checksByID = new HashMap<String, Check>();
	
	private static final ArrayListMultimap<String, Check> checksByState = ArrayListMultimap.create();
	
	private static final ArrayListMultimap<String, Alert> alertsByCheckAndTarget = ArrayListMultimap.create();

	private static final ArrayListMultimap<String, Alert> alertsByCheckId = ArrayListMultimap.create();

	static final HashMap<String, Alert> mostRecentAlertByCheckAndTarget = new HashMap<String, Alert>();
	
	public static int ALERT_HISTORY_THRESHOLD = 100;
	
	public NonQueuedDataCache(PasswordEncoder passwordEncoder, @Value("${admin.username}") String adminUsername,
			@Value("${admin.password}") String adminPassword,
			@Value("${authentication.service}") String serviceProvider, SeyrenConfig seyrenConfig) {
		super(passwordEncoder, adminUsername, adminPassword, serviceProvider, seyrenConfig);
		initialize();
	}

	private void initialize() {
		initializeChecks(mongoStore.getChecks(true, true));
		initializeChecks(mongoStore.getChecks(false, true));
		initializeChecks(mongoStore.getChecks(false, false));
		initializeChecks(mongoStore.getChecks(true, false));
		Iterator<Alert> alerts = mongoStore.getAlerts(0, ALERT_HISTORY_THRESHOLD).getValues().iterator();
		while (alerts.hasNext()){
			Alert alert = alerts.next();
			String key = alert.getCheckId() + alert.getTarget();
			if (!mostRecentAlertByCheckAndTarget.containsKey(key)){
				mostRecentAlertByCheckAndTarget.put(key, alert);
			}
			alertsByCheckAndTarget.put(alert.getCheckId() + alert.getTarget(), alert);
			alertsByCheckId.put(alert.getCheckId(), alert);
		}
	}
	
	private void initializeChecks(SeyrenResponse<Check> response){
		Iterator<Check> iterator = response.getValues().iterator();
		while (iterator.hasNext()){
			Check check = iterator.next();
			checksByID.put(check.getId(), check);
			checksByState.put(check.getState().toString(), check);
		}
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
	
	//****** Begin proxied database functions ******

	@Override
	public Subscription createSubscription(String checkId, Subscription subscription) {
		Check check = checksByID.get(checkId);
		if (check != null){
			check.getSubscriptions().add(subscription);
		}
		if (this.databaseSyncEnabled){
			return this.mongoStore.createSubscription(checkId, subscription);
		}
		else {
			return subscription;
		}
	}

	@Override
	public void deleteSubscription(String checkId, String subscriptionId) {
		Check check = checksByID.get(checkId);
		if (check != null){
			Iterator<Subscription> checkSubs = check.getSubscriptions().iterator();
			while (checkSubs.hasNext()){
				Subscription subscription = checkSubs.next();
				if (subscription.getId().equals(subscriptionId)){
					checkSubs.remove();
					if (this.databaseSyncEnabled){
						mongoStore.deleteSubscription(checkId, subscriptionId);
					}
					return;
				}
			}
		}
	}

	@Override
	public void updateSubscription(String checkId, Subscription subscription) {
		Check check = checksByID.get(checkId);
		if (check != null){
			List<Subscription> subs = check.getSubscriptions();
			Iterator<Subscription> checkSubs = subs.iterator();
			while (checkSubs.hasNext()){
				Subscription checkSub = checkSubs.next();
				if (checkSub.getId().equals(subscription.getId())){
					checkSubs.remove();
					subs.add(subscription);
					check.setSubscriptions(subs);
								
				}
			}
		}
		if (this.databaseSyncEnabled){
			this.mongoStore.updateSubscription(checkId, subscription);
		}
	}

	@Override
	public Alert createAlert(String checkId, Alert alert) {
		String key = checkId + alert.getTarget();
		mostRecentAlertByCheckAndTarget.put(key, alert);
		alertsByCheckAndTarget.put(key, alert);
		if (this.databaseSyncEnabled){
			return this.mongoStore.createAlert(checkId, alert);
		}
		else {
			return alert;
		}
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(String checkId, int start, int items) {
		List<Alert> alerts = alertsByCheckId.get(checkId);
		int counter = 0;
		ArrayList<Alert> results = new ArrayList<Alert>();
		if (alerts != null){
			Iterator<Alert> iterator = alerts.iterator();
			while (iterator.hasNext()){
				counter++;
				if (items > 0){
					if (counter >= items){
						break;
					}
				}
				if (counter < start){
					continue;
				}
				Alert alert = iterator.next();
				results.add(alert);
			}
		}
		SeyrenResponse<Alert> result = new SeyrenResponse<Alert>();
		result.setValues(results);
		result.setStart(start);
		result.setTotal(results.size());
		result.setItems(items);
		return result;
	}

	@Override
	public SeyrenResponse<Alert> getAlerts(int start, int items) {
		ArrayList<Alert> results = new ArrayList<Alert>();
		Iterator<Alert> allAlerts = alertsByCheckId.values().iterator();
		int counter = 0;
		while (allAlerts.hasNext()){
			counter++;
			if (items > 0){
				if (counter >= items){
					break;
				}
			}
			if (counter < start){
				continue;
			}
			Alert alert = allAlerts.next();
			results.add(alert);		
		}
		SeyrenResponse<Alert> result = new SeyrenResponse<Alert>();
		result.setValues(results);
		result.setStart(start);
		result.setTotal(results.size());
		result.setItems(items);
		return result;		
	}

	@Override
	public void deleteAlerts(String checkId, DateTime before) {
		List<Alert> alerts = alertsByCheckId.get(checkId);
		if (alerts != null){
			Iterator<Alert> iterator = alerts.iterator();
			while (iterator.hasNext()){
				Alert alert = iterator.next();
				if (alert.getTimestamp().isBefore(before)){
					iterator.remove();
				}
			}
		}
		if (this.databaseSyncEnabled){
			this.mongoStore.deleteAlerts(checkId, before);
		}
	}

	@Override
	public Alert getLastAlertForTargetOfCheck(String target, String checkId) {
		return mostRecentAlertByCheckAndTarget.get(checkId + target);
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
		ArrayList<Check> results = new ArrayList<Check>();
		Iterator<String> statesIterator = states.iterator();
		while (statesIterator.hasNext()){
			String state = statesIterator.next();
			List<Check> checks = checksByState.get(state);
			if (checks != null){
				Iterator<Check> checksIterator = checks.iterator();
				while (checksIterator.hasNext()){
					Check check = checksIterator.next();
					if (check.isEnabled() == enabled){
						results.add(check);
					}
				}
			}
		}
		SeyrenResponse<Check> result = new SeyrenResponse<Check>();
		result.setValues(results);
		result.setStart(0);
		result.setTotal(results.size());
		result.setItems(0);
		return result;
	}

	@Override
	public Check getCheck(String checkId) {
		return checksByID.get(checkId);
	}

	@Override
	public void deleteCheck(String checkId) {
		checksByID.remove(checkId);
		Iterator<Check> iterator = checksByState.values().iterator();
		while (iterator.hasNext()){
			Check check = iterator.next();
			if (check.getId().equals(checkId)){
				iterator.remove();
			}
		}
		if (this.databaseSyncEnabled){
			this.mongoStore.deleteCheck(checkId);
		}
	}

	@Override
	public Check createCheck(Check check) {
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		if (this.databaseSyncEnabled){
			return this.mongoStore.createCheck(check);
		}
		else {
			return check;
		}
	}

	@Override
	public Check saveCheck(Check check) {
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		if (this.databaseSyncEnabled){
			return this.mongoStore.saveCheck(check);
		}
		else {
			return check;
		}
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		Check check = checksByID.get(checkId);
		check.setState(state);
		check.setLastCheck(lastCheck);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		if (this.databaseSyncEnabled){
			return this.mongoStore.updateStateAndLastCheck(checkId, state, lastCheck);
		}
		else {
			return check;
		}
	}
}