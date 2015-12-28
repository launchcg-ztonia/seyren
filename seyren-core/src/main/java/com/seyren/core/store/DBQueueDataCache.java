package com.seyren.core.store;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seyren.core.domain.Alert;
import com.seyren.core.domain.AlertType;
import com.seyren.core.domain.Check;
import com.seyren.core.domain.Subscription;
import com.seyren.core.store.workers.StorageAccessWorker;
import com.seyren.core.store.workers.AlertCRUDWorker;
import com.seyren.core.store.workers.CheckCRUDWorker;
import com.seyren.core.store.workers.SubscriptionCRUDWorker;
import com.seyren.core.store.workers.USALCWorker;

/**
 * This Data Cache class adopts the strategy of not only serving read requests
 * from an in-memory cache, but also queueing DB create, update, and delete requests in
 * worker threads and executing them in bulk when appropriate.
 * @author WWarren
 *
 */
public class DBQueueDataCache extends SimpleInMemoryDataCache {
	/** A cache of checks, using the check id as the key, and the check itself as the value */
	
	private static final ArrayList<StorageAccessWorker> databaseQueue = new ArrayList<StorageAccessWorker>();
	
	public static final int QUEUE_THRESHOLD = 50;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataCache.class);
	
	
	@Override
	public Subscription createSubscription(String checkId, Subscription subscription) {
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(this.subscriptionsStore);
		worker.operationType = StorageAccessWorker.CREATE;
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
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(this.subscriptionsStore);
		worker.operationType = StorageAccessWorker.DELETE;
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
		SubscriptionCRUDWorker worker = new SubscriptionCRUDWorker(this.subscriptionsStore);
		worker.operationType = StorageAccessWorker.UPDATE;
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
		AlertCRUDWorker worker = new AlertCRUDWorker(this.alertsStore);
		worker.operationType = StorageAccessWorker.CREATE;
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
	public void deleteAlerts(String checkId, DateTime before) {
		AlertCRUDWorker worker = new AlertCRUDWorker(this.alertsStore);
		worker.operationType = StorageAccessWorker.DELETE;
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
	public void deleteCheck(String checkId) {
		CheckCRUDWorker worker = new CheckCRUDWorker(this.checksStore);
		worker.operationType = StorageAccessWorker.DELETE;
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
		CheckCRUDWorker worker = new CheckCRUDWorker(this.checksStore);
		worker.operationType = StorageAccessWorker.CREATE;
		worker.setCheck(check);
		databaseQueue.add(worker);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		this.checkIfQueueMustBeFlushed();
		return check;
	}

	@Override
	public Check saveCheck(Check check) {
		CheckCRUDWorker worker = new CheckCRUDWorker(this.checksStore);
		worker.operationType = StorageAccessWorker.CREATE;
		worker.setCheck(check);
		databaseQueue.add(worker);
		checksByID.put(check.getId(), check);
		checksByState.put(check.getState().toString(), check);
		this.checkIfQueueMustBeFlushed();
		return check;
	}

	@Override
	public Check updateStateAndLastCheck(String checkId, AlertType state, DateTime lastCheck) {
		USALCWorker worker = new USALCWorker(this.checksStore);
		worker.operationType = StorageAccessWorker.UPDATE;
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
			Iterator<StorageAccessWorker> iterator = databaseQueue.iterator();
			LOGGER.info("Database worker threshold reached, initiating queries.");
			while (iterator.hasNext()){
				StorageAccessWorker worker = iterator.next();
				Thread thread = new Thread(worker);
				LOGGER.info("Starting database worker thread.");
				thread.start();
			}
		}
	}
}