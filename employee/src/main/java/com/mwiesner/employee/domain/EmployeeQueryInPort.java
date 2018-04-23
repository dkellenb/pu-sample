package com.mwiesner.employee.domain;

import java.util.List;

public interface EmployeeQueryInPort {
	
	Employee getEmployeeByEmail(String email);

	List<Employee> getAllEmployees();

}
