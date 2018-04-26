package com.mwiesner.employee.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mwiesner.employee.domain.EmployeeCommandInPort;
import com.mwiesner.employee.domain.EmployeeQueryInPort;
import com.mwiesner.employee.domain.HREmployee;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/employee")
public class EmployeeController {
	
	@NonNull
	private EmployeeQueryInPort employeeQueryInPort;
	
	@NonNull
	private EmployeeCommandInPort employeeCommandInPort;
	
	
	@GetMapping
	public String list(Model model) {
		List<HREmployee> allEmployees = employeeQueryInPort.getAllEmployees();
		model.addAttribute("employeeList", allEmployees);
		return "listEmployees";
	}
	
	@GetMapping(params = "form")
	public String createForm(Model uiModel) {
		uiModel.addAttribute("employee", HREmployee.of(null, null, null, null, null, null));
		return "createEmployee";
	}
	
	@PostMapping
	public String create(HREmployee employee, BindingResult bindingResult, Model uiModel) {

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("employee", employee);
			return "createEmployee";
		}
		
		employeeCommandInPort.newEmployee(employee);
	
		return "redirect:/employee";
	}
 
	
	@ModelAttribute(name = "username")
	public String addCurrentUserToModel(@CurrentUser String user) {
		return user;
	}
	
	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.initDirectFieldAccess();
	}

}
