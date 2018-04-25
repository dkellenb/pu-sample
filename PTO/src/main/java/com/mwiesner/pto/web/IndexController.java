package com.mwiesner.pto.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mwiesner.pto.domain.Employee;
import com.mwiesner.pto.domain.EmployeeRepository;
import com.mwiesner.pto.domain.EmployeeSyncOutPort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class IndexController {
	
	@NonNull
	private EmployeeSyncOutPort syncOutPort;
	
	@NonNull
	private EmployeeRepository employeeRepository;

	@RequestMapping("/")
	public String redirectToStart() {
		return "redirect:/PTO";
	}
	
	@RequestMapping(value="/", params="sync")
	public String syncEmployees() {
		//TODO: We really need to ask Mike how to do that with Kafka
		List<Employee> employees = syncOutPort.fetchEmployeesFromEmployeeApp();
		employeeRepository.saveAll(employees);
		return "redirect:/PTO";
	}
	
}
