package com.mwiesner.employee.domain;

import java.time.LocalDate;
import java.util.ArrayList;
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
	
	public List<Employee> getAllEmployees() {
		Iterable<Employee> findAll = employeeRepository.findAll();
		ArrayList<Employee> arrayList = new ArrayList<Employee>();
		findAll.forEach(arrayList::add);
		return arrayList;
	}
	
	@PostConstruct
	public void dataInitializer() {
		employeeRepository.deleteAll();
		Employee max = Employee.of("max", "no-reply@example.com", "Max", "Mustermann",LocalDate.now(), LocalDate.of(2000, 01, 01));
		employeeRepository.save(max);
		Employee mike = Employee.of("mike", "no-reply@mwiesner.com", "Mike1", "Wiesner", LocalDate.of(2007, 04, 01), LocalDate.of(2015,10,21));
		employeeRepository.save(mike);
	}

}
