package com.mwiesner.pto.domain;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_={@Autowired}) 
@Service
public class EmployeeQueryImpl implements EmployeeQueryInPort {
	
	@NonNull
	private EmployeeRepository employeeRepository;
	
	
	public Employee getEmployeeByEmail(String email) {
		List<Employee> employeeList = employeeRepository.findByEmail(email);
		if (employeeList.size() != 1) {
			throw new IllegalStateException("Expected one matching employee, but found "+employeeList.size()+" with the email adress "+email);
		}
		return employeeList.get(0);
	}
	
	@PostConstruct
	public void dataInitializer() {
		employeeRepository.deleteAll();
		Employee max = Employee.of("max", "no-reply@example.com", "Max", "Mustermann");
		employeeRepository.save(max);
		Employee mike = Employee.of("mike", "no-reply@mwiesner.com", "Mike", "Wiesner");
		employeeRepository.save(mike);
		
		
		
	}

}
