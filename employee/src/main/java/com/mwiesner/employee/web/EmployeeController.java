package com.mwiesner.employee.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mwiesner.employee.domain.Employee;
import com.mwiesner.employee.domain.EmployeeQueryInPort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/employee")
public class EmployeeController {
	
	@NonNull
	private EmployeeQueryInPort employeeQueryInPort;
	
	@NonNull
	private PTOGateway ptoGateway;
	
	@RequestMapping(method = RequestMethod.GET)
	public String listPTOs(Model model) {
		List<Employee> allEmployees = employeeQueryInPort.getAllEmployees();
		model.addAttribute("employeeList", allEmployees);
		ptoGateway.sendEmployeeToPTO();
		return "listEmployees";
	}
	
	@ModelAttribute(name = "username")
	public String addCurrentUserToModel(@CurrentUser String user) {
		return user;
	}

}
