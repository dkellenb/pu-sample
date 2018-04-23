package com.mwiesner.pto.domain;

import java.util.List;

public interface EmployeeSyncOutPort {

	List<Employee> fetchEmployeesFromEmployeeApp();

}
