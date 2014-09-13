package com.rivendellcity.server.resource;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.rivendellcity.Constants;
import com.rivendellcity.bean.RequestRide;

public class RequestRideResource extends RequestRide {
	
	public void create(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity requestdbo = new Entity(Constants.REQUEST_RIDE_ENTITY, this.getTripid());
		if(!isAlreadyRequested(this.getRiderid(), this.getTripid())){
			requestdbo.setProperty(Constants.RIDERID, this.getRiderid());
			requestdbo.setProperty(Constants.DRIVERID, this.getDriverid());
			requestdbo.setProperty(Constants.TRIPID, this.getTripid());
			requestdbo.setProperty(Constants.IS_ACCEPTED, false);
			
			datastore.put(requestdbo);
			
			// TODO: add Notification code about request for car pool
			// make new notification
			// for rider: req sent
			// for driver: req from rider, confirm?
		}
	}
	
	private boolean isAlreadyRequested(String riderid, String tripid) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Entity requestdbo = new Entity(Constants.REQUEST_RIDE_ENTITY, this.getTripid());
		Filter trip_id = new FilterPredicate(Constants.TRIPID, FilterOperator.EQUAL, tripid);
		Filter rider_id = new FilterPredicate(Constants.RIDERID, FilterOperator.EQUAL, riderid);
		Filter filter = CompositeFilterOperator.and(trip_id, rider_id);
		Query q = new Query(Constants.REQUEST_RIDE_ENTITY).setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			return true;
		}
		return false;
	}

	public void acceptRequest(String requestid, String tripid) throws EntityNotFoundException{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity requestdbo = datastore.get(KeyFactory.stringToKey(requestid));
		String riderid = (String)requestdbo.getProperty(Constants.RIDERID);
		requestdbo.setProperty(Constants.IS_ACCEPTED, true);
		datastore.put(requestdbo);
		new TripResource().add_rider(tripid, riderid);
		// don't add notification code.. already added in add_rider()
	}

}
