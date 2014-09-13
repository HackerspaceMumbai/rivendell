/**
 * 
 */
package com.rivendellcity.server.resource;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.Gson;
import com.rivendellcity.Constants;
import com.rivendellcity.bean.PasswordHash;
import com.rivendellcity.bean.User;

@SuppressWarnings("serial")
public class UserResource extends User {
	
	/**
	 * Default Constructor
	 */
	public UserResource() {	}

//	/**
//	 * @param username
//	 * @param email
//	 * @param password
//	 * @param firstname
//	 * @param lastname
//	 */
//	public UserResource(String email, String password,
//			String firstname, String lastname) {
//		this.setEmail(email);		
//		this.setPassword(password);
//		this.setFirstname(firstname);
//		this.setLastname(lastname);
//	}
//	

	// create
	public boolean create() throws NoSuchAlgorithmException, InvalidKeySpecException  {
		boolean isCreated = false;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Transaction txn = datastore.beginTransaction();
		try {	
			Entity user = new Entity(Constants.USER_ENTITY, this.getEmail());			
			user.setProperty(Constants.EMAIL, this.getEmail());
			user.setProperty(Constants.PASSWORD, PasswordHash.createHash(this.getPassword()));
			user.setProperty(Constants.FIRST_NAME, this.getFirstname());
			user.setProperty(Constants.LAST_NAME, this.getLastname());	
			user.setProperty(Constants.COUNTRY, this.getCountry());
//			user.setProperty(Constants.PHONE, this.getPhone());
			// persist user
			datastore.put(user);
			txn.commit();
			isCreated = true;
		} finally {
		    if (txn.isActive()) {
		        txn.rollback();
		    }
		}

		return isCreated;
	}
	
	// retrieve ALWAYS with password
	public UserResource get (String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		UserResource user = null;
		try {
			user = this.get(email);
			if(user != null && (PasswordHash.validatePassword(password, user.getPassword()))){
				user.setPassword("dummy");
			} else {
				user = null;
			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public UserResource get() throws NoSuchAlgorithmException, InvalidKeySpecException {
		return this.get(this.getEmail(), this.getPassword());
	}
	
	// retrieve without password 
	private UserResource get (String email) throws EntityNotFoundException {
		UserResource user = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key key = KeyFactory.createKey(Constants.USER_ENTITY, email);
		Entity userdbo = datastore.get(key);
		if(userdbo!=null){
			Map<String, Object> usermap = userdbo.getProperties();
			Gson gson = new Gson();
			String json = gson.toJson(usermap);
			user = gson.fromJson(json, UserResource.class);
		}	
		return user;
	}
	
	// update 
	public void update () {
		// TODO: update user
	}
	
	// delete
	public boolean delete (HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException {
		boolean isRemoved = false;
		UserResource user = this.get(this.getEmail(), this.getPassword());
		if(user != null && session !=null){
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Key key = KeyFactory.createKey(Constants.USER_ENTITY, user.getEmail());
			// delete all dependent children
//			new ContactResource().delete(key);
			this.logout(session); // remove from session
			datastore.delete(key);
			isRemoved = true;
		} 
		return isRemoved;
	}
	
	// login
	public boolean login(HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException {
		boolean isLoggedin= false;
		UserResource user = this.get(this.getEmail(), this.getPassword());
		if(user != null){
			isLoggedin = true;
			user.setSession(session);
		}
		return isLoggedin;
	}
	
	// logout
	public void logout(HttpSession session){
		session.invalidate();
	}
	
	// duplicate username check
	public boolean isExistingUser() {
		boolean isDuplicate = false;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate(Constants.EMAIL, FilterOperator.EQUAL, this.getEmail());
		Query query = new Query(Constants.USER_ENTITY)
											.setFilter(filter);
		PreparedQuery pq = datastore.prepare(query);
		Entity userdbo = pq.asSingleEntity();
		if(userdbo!=null){
			isDuplicate = true;
		}	

		return isDuplicate;
	}
	
	private void setSession(HttpSession session){
		session.setMaxInactiveInterval(60*60);
		session.setAttribute("user", this);
	}	
	
	
}
