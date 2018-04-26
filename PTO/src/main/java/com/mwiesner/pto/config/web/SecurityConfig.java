package com.mwiesner.pto.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.mwiesner.pto.domain.EmployeeQueryInPort;
import com.mwiesner.pto.domain.UserRoleQueryInPort;
import com.mwiesner.pto.web.PTOGrantedAuthoritiesMapper;
import com.mwiesner.pto.web.PTOUserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private EmployeeQueryInPort employeeQueryInPort;
	
	@Autowired
	private UserRoleQueryInPort userRoleQueryInPort;
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.mvcMatchers("/PTO")//
					.hasAnyRole("Employee")//
				.anyRequest()//
					.authenticated()//
					.and()//
				.csrf()//
					.disable()//
				.oauth2Login()//
					.clientRegistrationRepository(this.clientRegistrationRepository)//
					.authorizedClientService(this.authorizedClientService())//
					.userInfoEndpoint()//
						.userAuthoritiesMapper(authoritiesMapper())//
						.oidcUserService(ptoUserService());
	}
	
	
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
		return new InMemoryOAuth2AuthorizedClientService(this.clientRegistrationRepository);
	}
	
	@Bean
	public PTOGrantedAuthoritiesMapper authoritiesMapper() {
		return new PTOGrantedAuthoritiesMapper(userRoleQueryInPort);
	}
	
	@Bean
	public PTOUserService ptoUserService() {
		return new PTOUserService(this.employeeQueryInPort);
	}

}
