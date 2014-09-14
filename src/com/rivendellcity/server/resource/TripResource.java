package com.rivendellcity.server.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.rivendellcity.Constants;
import com.rivendellcity.bean.Trip;

@SuppressWarnings("serial")
public class TripResource extends Trip {

	public void create(String useremail){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key driverid = KeyFactory.createKey(Constants.USER_ENTITY, useremail);
		
		Entity tripdbo = new Entity(Constants.TRIP_ENTITY, driverid);
		
//		driverdbo.setProperty(Constants.ID, null);
		tripdbo.setProperty(Constants.DRIVERID, driverid);
		tripdbo.setProperty(Constants.ORIGIN, this.getOrigin());
		tripdbo.setProperty(Constants.DESTINATION, this.getDestination());
		tripdbo.setProperty(Constants.TIME, this.getTime());
		tripdbo.setProperty("origin_string", this.getOrigin_string());
		tripdbo.setProperty("destination_string", this.getDestination_string());
		
		datastore.put(tripdbo);
		
	}
	
	public ArrayList<TripResource> get(Date day){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		ArrayList<TripResource> list = new ArrayList<TripResource>();

		Filter equalto_present_day = new FilterPredicate(Constants.TIME, FilterOperator.GREATER_THAN_OR_EQUAL, day);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(day); 
		c.add(Calendar.DATE, 1);
		day = c.getTime();
		
		Filter lessthan_next_day = new FilterPredicate(Constants.TIME, FilterOperator.LESS_THAN, day);
		
		Filter day_filter = CompositeFilterOperator.and(equalto_present_day, lessthan_next_day);
		
		Query query = new Query(Constants.TRIP_ENTITY).setFilter(day_filter);
		PreparedQuery pq = datastore.prepare(query);

		for (Entity result : pq.asIterable()) {
			TripResource d = new TripResource();

			String driverid = KeyFactory.keyToString( ((Key)result.getProperty("driverid")) );
			GeoPt origin = ((GeoPt)result.getProperty("origin"));			
			GeoPt destination = ((GeoPt)result.getProperty("destination"));
			Date time = (Date) result.getProperty("time");
			String origin_string = (String) result.getProperty("origin_string");
			String destination_string = (String) result.getProperty("destination_string");
			ArrayList<String> riderid = (ArrayList<String>) result.getProperty("riderid");
			
			d.setDestination(destination);
			d.setOrigin(origin);
			d.setTime(time);
			d.setDriverid(driverid);
			d.setRiderid(riderid);
			d.setOrigin_string(origin_string);
			d.setDestination_string(destination_string);
			d.setTripid(KeyFactory.keyToString(result.getKey()));
			list.add(d);
			}
		
		return list;
		
	}
	
	public void add_rider(String tripid, String riderid) throws EntityNotFoundException{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity tripdbo = datastore.get(KeyFactory.stringToKey(tripid));
		ArrayList<String> rider = (ArrayList<String>) tripdbo.getProperty(Constants.RIDERID);
 		if(rider == null) rider = new ArrayList<String>(); 
		rider.add(riderid);
		tripdbo.setProperty(Constants.RIDERID, rider);
		datastore.put(tripdbo);
		
		//TODO: Add Notification code.. for confirmed rider and share details
		
	}
	
}
