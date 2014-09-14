package com.rivendellcity.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.GeoPt;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rivendellcity.Constants;
import com.rivendellcity.server.resource.TripResource;
import com.rivendellcity.server.resource.UserResource;

public class TripServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter(Constants.ACTION);
		
		if(action.equals(Constants.CREATE)){
			String data = req.getParameter(Constants.DATA);
			Gson gson = new Gson();		
			JsonObject jsonobj = gson.fromJson(data, JsonObject.class);
			// date
			int day = jsonobj.get("day").getAsInt();
			int month = jsonobj.get("month").getAsInt();
			int year = jsonobj.get("year").getAsInt();
			int hour = jsonobj.get("hour").getAsInt();
			int min = jsonobj.get("min").getAsInt();
			int sec = jsonobj.get("sec").getAsInt();
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hour, min, sec);
			Date date = cal.getTime();
			String orig[] = jsonobj.get("origin").getAsString().split(",");
			String dest[] = jsonobj.get("destination").getAsString().split("[,]");
			GeoPt origin = new GeoPt(Float.parseFloat(orig[0]), Float.parseFloat(orig[1]));			
			GeoPt destination = new GeoPt(Float.parseFloat(dest[0]), Float.parseFloat(dest[1]));
			String origin_string = orig[2];
			String destination_string = dest[2];
			HttpSession session = req.getSession(false);
			
			if(session != null) {
				UserResource user = (UserResource) session.getAttribute("user");
				String json = new Gson().toJson(user);
				jsonobj = new Gson().fromJson(json, JsonObject.class);
				String email = jsonobj.get(Constants.EMAIL).getAsString();
				
				TripResource driver = new TripResource();
				driver.setOrigin(origin);
				driver.setDestination(destination);
				driver.setTime(date);
				driver.setOrigin_string(origin_string);
				driver.setDestination_string(destination_string);
				driver.create(email);
				resp.sendError(201, "trip added successfully");
				
			} else {
				resp.sendError(401, "user is not loggid in");
			}
						
		} else if(action.equals(Constants.RETRIEVE)){
			String data = req.getParameter(Constants.DATA);
			Gson gson = new Gson();
			JsonObject jsonobj = gson.fromJson(data, JsonObject.class);
			int day = jsonobj.get("day").getAsInt();
			int month = jsonobj.get("month").getAsInt();
			int year = jsonobj.get("year").getAsInt();
			int hour = jsonobj.get("hour").getAsInt();
			int min = jsonobj.get("min").getAsInt();
			int sec = jsonobj.get("sec").getAsInt();
			
			Calendar c = Calendar.getInstance();
			c.set(year, month, day, hour, min, sec);
			
			ArrayList<TripResource> list = new TripResource().get(c.getTime());
			String result = gson.toJson(list);
			resp.setContentType("application/json; charset=UTF-8");			
			PrintWriter out = resp.getWriter();
			out.print(result);
			out.flush();
		}
		
	}
}
