package com.mwiesner.pto.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@RequestMapping(method=RequestMethod.POST)
	public void createEmployee(String test) {
		System.out.println("------------->" + test);
	}

}
