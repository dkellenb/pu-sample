package com.mwiesner.employee.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mwiesner.employee.domain.HREmployee;
import com.mwiesner.employee.domain.EmployeeQueryInPort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/employee")
public class EmployeeController {
	
	@NonNull
	private EmployeeQueryInPort employeeQueryInPort;
	
	
	@RequestMapping(method = RequestMethod.GET, produces="text/html")
	public String listPTOs(Model model) {
		List<HREmployee> allEmployees = employeeQueryInPort.getAllEmployees();
		model.addAttribute("employeeList", allEmployees);
		return "listEmployees";
	}
	
	@ModelAttribute(name = "username")
	public String addCurrentUserToModel(@CurrentUser String user) {
		return user;
	}

}
