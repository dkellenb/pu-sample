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
	
	public HREmployee getEmployeeByEmail(String email) {
		List<HREmployee> employeeList = employeeRepository.findByEmail(email);
		if (employeeList.size() != 1) {
			throw new IllegalStateException("Expected one matching employee, but found "+employeeList.size()+" with the email adress "+email);
		}
		return employeeList.get(0);
	}
	
	public List<HREmployee> getAllEmployees() {
		Iterable<HREmployee> findAll = employeeRepository.findAll();
		ArrayList<HREmployee> arrayList = new ArrayList<HREmployee>();
		findAll.forEach(arrayList::add);
		return arrayList;
	}
	
	@PostConstruct
	public void dataInitializer() {
		employeeRepository.deleteAll();
		HREmployee max = HREmployee.of("max", "no-reply@example.com", "Max", "Mustermann",LocalDate.now(), LocalDate.of(2000, 01, 01));
		employeeRepository.save(max);
		HREmployee mike = HREmployee.of("mike", "no-reply@mwiesner.com", "Mike", "Wiesner", LocalDate.of(2007, 04, 01), LocalDate.of(2015,10,21));
		employeeRepository.save(mike);
	}

}
