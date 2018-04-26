package com.mwiesner.employee.config.web;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.mwiesner.employee.web.PUGrantedAuthoritiesMapper;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	
	@Bean
	public UserDetailsService userDetailsService() {
		return (username) -> {
			HashMap<String, UserDetails> users = new HashMap<String, UserDetails>() {
				{
					put("mike", User.withUsername("mike")//
							.password("{bcrypt}$2a$10$j.X09oYOYg.o7EhIOR/mYO3YpiM/bgqyHvDFKbGfDjONzNfCUN8pq")//
							.authorities("ROLE_HR")//
							.build());
					put("bill", User.withUsername("bill")//
							.password("{bcrypt}$2a$10$j.X09oYOYg.o7EhIOR/mYO3YpiM/bgqyHvDFKbGfDjONzNfCUN8pq")//
							.authorities("ROLE_employee")//
							.build());
				}
			};
			
			return users.get(username);
		};
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
	
	public static void main(String[] args) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encodePW = encoder.encode("test");
		System.out.println(encodePW);
	}
	

	


}
