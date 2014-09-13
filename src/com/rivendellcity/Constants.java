
package com.rivendellcity;

public interface Constants {
	
	/* 	Debug */
//	public static boolean DEBUG = this.isDebug();
	
	/*	 DB Entities 	*/
	public static final String USER_ENTITY = "user";

	public static final String TRIP_ENTITY = "trip";
	public static final String REQUEST_RIDE_ENTITY = "request_ride";

//	public static final String EMAIL_ENTITY = "email";
//	public static final String ADDRESS_ENTITY = "address";
//	public static final String URL_ENTITY = "url";
//	public static final String SOCIAL_ENTITY = "social";
	
	/*	 DB Property Fields 	*/	
	/*	 user	*/
	public static final String ID = "id";
	public static final String USERID = "userid";
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String FIRST_NAME = "firstname";
	public static final String LAST_NAME = "lastname";	
	public static final String PHONE = "phone";
	public static final String COUNTRY = "country";

	
	/*	driver  */
	public static final String ORIGIN = "origin";
	public static final String DESTINATION = "destination";
	public static final String TIME = "time";
	public static final String DATE = "date";
	

	public static final String DRIVERID = "driverid";
	public static final String RIDERID = "riderid";
	public static final String TRIPID = "tripid";
	
	public static final String IS_ACCEPTED = "is-accepted";
	
	/* enums */
	public static final String KEY = "key";

	
	/* servlet */
	public static final String ACTION = "actn";
	public static final String DATA = "data";
	
	/*	Resource constants CRUD	*/
	public static final String USER_RESOURCE="user";
	public static final String DRIVER_RESOURCE="driver";
	public static final String RIDER_RESOURCE="rider";
	public static final String CREATE="create";
	public static final String RETRIEVE="get";
	public static final String UPDATE="update";
	public static final String DELETE="delete";
	
	/*	Resource user constants 	*/
	public static final String LOGIN="login";
	public static final String LOGOUT="logout";
	
	/*	Resource contact specific constants 	*/
	
	/* Response entity messages */
	public static final String USER_CREATED="User successfull created";
	
	/*	Utilities 	*/
	/*
	 * curl -X POST -H "Content-Type: application/json" -d '{"username":"sahilgandhi","password":"pass"}' http://localhost:8080/myapp/rest/user/login
	 * "Content-Type:text/xml"
	 */

	
}
