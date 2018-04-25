package com.mwiesner.employee.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_={@Autowired}) 
@Service
public class EmployeeCommandImpl implements EmployeeCommandInPort {

	@NonNull
	private EmployeeRepository employeeRepository;
	
	
	public HREmployee newEmployee(HREmployee employee) {
		HREmployee newEmployee = employee.toBuilder()//
			.id(employee.getFirstname().toLowerCase())//TODO: We really need to fix that!
			.build();
		HREmployee savedEmployee = employeeRepository.save(newEmployee);
		return savedEmployee;
	}
	
	
}
