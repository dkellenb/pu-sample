package com.mwiesner.employee.web;

public class PTOGateway extends ApiBinding{

	public PTOGateway(String accessToken) {
		super(accessToken);
	}
	
	public void sendEmployeeToPTO() {
		restTemplate.postForLocation("http://localhost:8083/employee", "test");
	}



}
