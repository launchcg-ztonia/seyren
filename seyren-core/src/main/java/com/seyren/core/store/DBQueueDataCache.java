package com.seyren.core.store;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.seyren.mongo.cache.actionThreads.AlertCRUDWorker;
import com.seyren.mongo.cache.actionThreads.CheckCRUDWorker;
import com.seyren.mongo.cache.actionThreads.MongoAccessThread;
import com.seyren.mongo.cache.actionThreads.SubscriptionCRUDWorker;
import com.seyren.mongo.cache.actionThreads.USALCWorker;
/**
 * 
 * @author WWarren
 *
 */
public class DBQueueDataCache extends DataCache {

	private static final HashMap<String, Check> checksByID = new HashMap<String, Check>();
	
	private static final ArrayListMultimap<String, Check> checksByState = ArrayListMultimap.create();
	
	private static final ArrayListMultimap<String, Alert> alertsByCheckAndTarget = ArrayListMultimap.create();

	private static final ArrayListMultimap<String, Alert> alertsByCheckId = ArrayListMultimap.create();

	static final HashMap<String, Alert> mostRecentAlertByCheckAndTarget = new HashMap<String, Alert>();
	
	public static int ALERT_HISTORY_THRESHOLD = 100;
	
	private static final ArrayList<MongoAccessThread> databaseQueue = new ArrayList<MongoAccessThread>();
	
	public static final int QUEUE_THRESHOLD = 50;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCache.class);
	
	protected DBQueueDataCache(){
		initialize();
	}

	private void initialize() {
		initializeChecks(checksStore.getChecks(true, true));
		initializeChecks(checksStore.getChecks(false, true));
		initializeChecks(checksStore.getChecks(false, false));
		initializeChecks(checksStore.getChecks(true, false));
		Iterator<Alert> alerts = alertsStore.getAlerts(0, ALERT_HISTORY_THRESHOLD).getValues().iterator();
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
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.CREATE;
		worker.setCheckId(checkId);
		worker.setSubscription(subscription);
		databaseQueue.add(worker);
		Check check = checksByID.get(checkId);
		if (check != null){
			check.getSubscriptions().add(subscription);
		}
		this.checkIfQueueMustBeFlushed();
		return subscription;
	}

	@Override
	public void deleteSubscription(String checkId, String subscriptionId) {
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.DELETE;
		worker.setCheckId(checkId);
		worker.setSubscriptionId(subscriptionId);
		databaseQueue.add(worker);
		DBQueueDataCache.databaseQueue.add(worker);
		Check check = checksByID.get(checkId);
		if (check != null){
			Iterator<Subscription> checkSubs = check.getSubscriptions().iterator();
			while (checkSubs.hasNext()){
				Subscription subscription = checkSubs.next();
				if (subscription.getId().equals(subscriptionId)){
					checkSubs.remove();
				}
			}
		}
		this.checkIfQueueMustBeFlushed();
	}

	@Override
	public void updateSubscription(String checkId, Subscription subscription) {
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.UPDATE;
		worker.setCheckId(checkId);
		worker.setSubscription(subscription);
		databaseQueue.add(worker);
		DBQueueDataCache.databaseQueue.add(worker);
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
		this.checkIfQueueMustBeFlushed();
	}

	@Override
	public Alert createAlert(String checkId, Alert alert) {
		AlertCRUDWorker worker = new AlertCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.CREATE;
		worker.setCheckId(checkId);
		worker.setAlert(alert);
		databaseQueue.add(worker);
		String key = checkId + alert.getTarget();
		mostRecentAlertByCheckAndTarget.put(key, alert);
		alertsByCheckAndTarget.put(key, alert);
		this.checkIfQueueMustBeFlushed();
		return alert;
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
		AlertCRUDWorker worker = new AlertCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.DELETE;
		worker.setCheckId(checkId);
		worker.setBefore(before);
		databaseQueue.add(worker);
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
		this.checkIfQueueMustBeFlushed();
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
		CheckCRUDWorker worker = new CheckCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.DELETE;
		worker.setCheckId(checkId);
		databaseQueue.add(worker);
		checksByID.remove(checkId);
		Iterator<Check> iterator = checksByState.values().iterator();
		while (iterator.hasNext()){
			Check check = iterator.next();
			if (check.getId().equals(checkId)){
				iterator.remove();
			}
		}
		this.checkIfQueueMustBeFlushed();
	}

	@Override
	public Check createCheck(Check check) {
		CheckCRUDWorker worker = new CheckCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.CREATE;
		worker.setCheck(check);
		databaseQueue.add(worker);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		this.checkIfQueueMustBeFlushed();
		return check;
	}

	@Override
	public Check saveCheck(Check check) {
		CheckCRUDWorker worker = new CheckCRUDWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.CREATE;
		worker.setCheck(check);
		databaseQueue.add(worker);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		this.checkIfQueueMustBeFlushed();
		return check;
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		USALCWorker worker = new USALCWorker(DataCache.mongoStore);
		worker.operationType = MongoAccessThread.UPDATE;
		worker.setCheckId(checkId);
		worker.setState(state);
		worker.setLastCheck(lastCheck);
		databaseQueue.add(worker);
		Check check = checksByID.get(checkId);
		check.setState(state);
		check.setLastCheck(lastCheck);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		this.checkIfQueueMustBeFlushed();
		return check;
	}
	
	public synchronized void checkIfQueueMustBeFlushed(){
		if (databaseQueue.size() > QUEUE_THRESHOLD){
			Iterator<MongoAccessThread> iterator = databaseQueue.iterator();
			LOGGER.info("Database worker threshold reached, initiating queries.");
			while (iterator.hasNext()){
				MongoAccessThread worker = iterator.next();
				Thread thread = new Thread(worker);
				LOGGER.info("Starting database worker thread.");
				thread.start();
			}
		}
	}
}