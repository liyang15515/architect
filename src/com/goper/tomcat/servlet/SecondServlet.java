package com.goper.tomcat.servlet;


import com.goper.tomcat.http.GPRequest;
import com.goper.tomcat.http.GPResponse;
import com.goper.tomcat.http.GPServlet;

public class SecondServlet extends GPServlet {

	public void doGet(GPRequest request, GPResponse response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(GPRequest request, GPResponse response) throws Exception {
//		response.write("This is Second Serlvet");
		response.write2("This is Second Serlvet");

	}

}
