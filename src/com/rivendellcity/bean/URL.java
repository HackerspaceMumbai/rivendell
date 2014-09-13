package com.rivendellcity.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;

public class URL {
	private String charset = "utf-8";
	
	public String GET(String url) throws MalformedURLException, IOException {		
		URLConnection connection = new java.net.URL(url).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		InputStream response = connection.getInputStream();
		
		return getResponseString(response);
	}
	
	private String getResponseString(InputStream response) {
		// to decode the response to string
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(response, charset).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	public String POST(String url, String query) throws IOException {
		URLConnection connection = new java.net.URL(url).openConnection();
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		try (OutputStream output = connection.getOutputStream()) {
		    output.write(query.getBytes(charset));
		}

		InputStream response = connection.getInputStream();
		
		return getResponseString(response);
	}
}
