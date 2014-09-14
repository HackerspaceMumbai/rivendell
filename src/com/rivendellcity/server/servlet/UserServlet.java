package com.rivendellcity.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.rivendellcity.Constants;
import com.rivendellcity.server.resource.UserResource;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO retrieve user with, REMEMBER: replace password hash with dummy string (for session)
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter(Constants.ACTION);
		
		if (action.equals(Constants.CREATE)) {
			try {
				// create UserResource
				String data = req.getParameter(Constants.DATA);
				Gson gson = new Gson();		
				resp.setHeader("Access-Control-Allow-Origin", "*"); // CORS hack
				UserResource user = gson.fromJson(data, UserResource.class);				
				if (user.isExistingUser()) {
					resp.sendError(403, "email already in use");
				} else{
					if (user.create()) {	
						resp.sendError(201, "user registered successfully");
					} else {
						resp.sendError(500, "server could not complete your request");
					}
				}
					
				
			} catch (Exception e) {
				resp.sendError(500, "user could not register due to this error: " + e.getMessage());
			}
			
		} else if (action.equals(Constants.RETRIEVE)) {			
			// retrieve user
			resp.setHeader("Access-Control-Allow-Origin", "*"); // CORS hack
			String json = req.getParameter(Constants.DATA);
			Gson gson = new Gson();
			UserResource user = gson.fromJson(json, UserResource.class);
			try {
				user = user.get();
				String data = gson.toJson(user);
				// send response
				resp.setContentType("application/json; charset=UTF-8");			
				// Get the printwriter object from response to write the required json object to the output stream			
				PrintWriter out = resp.getWriter();
				out.print(data);
				// Or
	            // printout.write(JObject.toString()); 
				// out.write(data.toString());
				out.flush();
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (action.equals(Constants.UPDATE)) {
			// update user
			
			
		} else if (action.equals(Constants.DELETE)) {
			// delete user
			String data = req.getParameter(Constants.DATA);
			Gson gson = new Gson();
			UserResource user = gson.fromJson(data, UserResource.class);
			try {
				if (! user.delete(req.getSession(false))) {
					resp.sendError(401, "could not authorize user");
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (action.equals(Constants.LOGIN)) {
			// login
			String data = req.getParameter(Constants.DATA);
			Gson gson = new Gson();
			UserResource user = gson.fromJson(data, UserResource.class);
			try {
//				if(req.getSession(false) == null) {
					if (!user.login(req.getSession())) {
						resp.sendError(401, "could not authorize user");
					}
//				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else if(action.equals(Constants.LOGOUT)){
			HttpSession session = req.getSession(false);
			UserResource user = null;
			if (session != null)
				user = (UserResource) session.getAttribute("user");
			if(user != null)
				user.logout(req.getSession(false));
		}
		
	}


	
}
