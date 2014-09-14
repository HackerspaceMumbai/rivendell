package com.rivendellcity.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rivendellcity.Constants;
import com.rivendellcity.server.resource.RequestRideResource;
import com.rivendellcity.server.resource.UserResource;

public class RequestRideServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	
		String action = req.getParameter(Constants.ACTION);
		
		if(action.equals(Constants.CREATE)){
			String data = req.getParameter(Constants.DATA);
			RequestRideResource r = new Gson().fromJson(data, RequestRideResource.class);
			
			HttpSession session = req.getSession(false);
			if(session.getAttribute("user") != null){
				UserResource user = (UserResource) session.getAttribute("user");
				String json = new Gson().toJson(user);
				JsonObject jsonobj = new Gson().fromJson(json, JsonObject.class);
				String email = jsonobj.get(Constants.EMAIL).getAsString();
				
				r.create(email);
				resp.sendError(201, "request added successfully");
			}  else {
				resp.sendError(401, "user is not loggid in");
			}
		} else if(action.equals("accept_request")){
			String data = req.getParameter(Constants.DATA);
			JsonObject jsonobj = new Gson().fromJson(data, JsonObject.class);
			String requestid = jsonobj.get("requestid").getAsString();
			
			HttpSession session = req.getSession(false);
			if(session.getAttribute("user") != null){
				new RequestRideResource().acceptRequest(requestid);
				resp.sendError(201, "request accepted successfully");
			}  else {
				resp.sendError(401, "user is not loggid in");
			}
		}
	
	}
}
