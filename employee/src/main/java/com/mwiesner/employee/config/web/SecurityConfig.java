package com.mwiesner.employee.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.mwiesner.employee.web.PUGrantedAuthoritiesMapper;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;



	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withDefaultPasswordEncoder().username("mike").password("test").roles("HR").build());
		return manager;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/sync/**")//
					.permitAll()//
				.anyRequest()//
					.hasRole("HR")
					.and()//
				.formLogin()//
					.and()//
				.csrf()//
					.disable()//
				.oauth2Login()//
					.clientRegistrationRepository(this.clientRegistrationRepository)//
					.authorizedClientService(this.authorizedClientService())//
					.userInfoEndpoint()//
						.userAuthoritiesMapper(authoritiesMapper());

	}
	
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
		return new InMemoryOAuth2AuthorizedClientService(this.clientRegistrationRepository);
	}
	
	@Bean
	public PUGrantedAuthoritiesMapper authoritiesMapper() {
		return new PUGrantedAuthoritiesMapper();
	}
	

	


}
