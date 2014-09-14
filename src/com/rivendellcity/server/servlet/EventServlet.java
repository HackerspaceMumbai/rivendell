package com.rivendellcity.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rivendellcity.Constants;
import com.rivendellcity.server.resource.EventResource;
import com.rivendellcity.server.resource.UserResource;

public class EventServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String action = req.getParameter(Constants.ACTION);
		
		if(action.equals(Constants.CREATE)){
			
			String data = req.getParameter(Constants.DATA);
			Gson gson = new Gson();		
			EventResource e = new EventResource();
			
			JsonObject jsonobj = gson.fromJson(data, JsonObject.class);
			// date
			int day = jsonobj.get("day").getAsInt();
			int month = jsonobj.get("month").getAsInt();
			month--;
			int year = jsonobj.get("year").getAsInt();
			int hour = jsonobj.get("hour").getAsInt();
			int min = jsonobj.get("min").getAsInt();
			int sec = jsonobj.get("sec").getAsInt();
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hour, min, sec);
			Date date = cal.getTime();
			String category = jsonobj.get("category").getAsString();
			String place = jsonobj.get("place").getAsString();
			
			e.setPlace(place);
			e.setCategory(category);
			e.setTime(date);
			
			HttpSession session = req.getSession(false);
			
			if(session != null) {
				UserResource user = (UserResource) session.getAttribute("user");
				String json = gson.toJson(user);
				jsonobj = gson.fromJson(json, JsonObject.class);
				String useremail = jsonobj.get(Constants.EMAIL).getAsString();
				e.create(useremail);
				resp.sendError(201, "event added successfully");
				
			} else {
				resp.sendError(401, "user is not loggid in");
			}
			
		} else if(action.equals(Constants.RETRIEVE)){
			
			ArrayList<EventResource> list = new EventResource().get();
			String result = new Gson().toJson(list);
			resp.setContentType("application/json; charset=UTF-8");			
			PrintWriter out = resp.getWriter();
			out.print(result);
			out.flush();
		}
	}
}
