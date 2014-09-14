package com.rivendellcity.server.resource;

import java.util.ArrayList;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.rivendellcity.Constants;
import com.rivendellcity.bean.Event;

public class EventResource extends Event {

	public void create(String useremail){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key userid = KeyFactory.createKey(Constants.USER_ENTITY, useremail);
		Entity e = new Entity("event", userid);
		
		e.setProperty("category", this.getCategory());
		e.setProperty("place", this.getPlace());
		e.setProperty("time", this.getTime());
		
		datastore.put(e);
	}
	
	public ArrayList<EventResource> get(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		ArrayList<EventResource> list = new ArrayList<>();
		Query query = new Query("event");
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Map<String, Object> map = result.getProperties();
			String json = new Gson().toJson(map);
			EventResource er = new Gson().fromJson(json, EventResource.class);
			list.add(er);
		}
		return list;
	}
} 
