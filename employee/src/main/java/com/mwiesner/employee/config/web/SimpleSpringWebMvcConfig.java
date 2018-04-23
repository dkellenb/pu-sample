package com.mwiesner.employee.config.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mwiesner.employee.web.PTOGateway;

@EnableWebMvc
@SpringBootApplication(scanBasePackages={"com.mwiesner.employee.web","com.mwiesner.employee.domain"})
@EnableMongoRepositories(basePackages="com.mwiesner.employee.domain")
@Import(SecurityConfig.class)
public class SimpleSpringWebMvcConfig implements WebMvcConfigurer {
	
	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringWebMvcConfig.class, args);
	}
	
    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilter(){
        FilterRegistrationBean hiddenHttpMethodFilter = new FilterRegistrationBean(new HiddenHttpMethodFilter());
        hiddenHttpMethodFilter.addUrlPatterns("/*");
        return hiddenHttpMethodFilter;
    }
    
	@Bean
	@RequestScope
	public PTOGateway ptoGateway(OAuth2AuthorizedClientService clientService) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String accessToken = null;
		if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
			OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId,
					oauthToken.getName());
			accessToken = client.getAccessToken().getTokenValue();
		}
		return new PTOGateway(accessToken);
	}
    


	

}
