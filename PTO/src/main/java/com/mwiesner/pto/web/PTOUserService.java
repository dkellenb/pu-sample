package com.mwiesner.pto.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import com.mwiesner.pto.domain.Employee;
import com.mwiesner.pto.domain.EmployeeQueryInPort;
import com.mwiesner.pto.domain.PTOUser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_={@Autowired}) 
public class PTOUserService extends OidcUserService {
	
	@NonNull
	private EmployeeQueryInPort employeeQueryInPort;
	
	
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser baseUser = super.loadUser(userRequest);
		OidcIdToken idToken = baseUser.getIdToken();
		OidcUserAuthority newAuthority = new OidcUserAuthority("ROLE_TEMP",idToken, baseUser.getUserInfo());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(newAuthority);
		Employee employee = employeeQueryInPort.getEmployeeByEmail(baseUser.getEmail());
		return new PTOUser(authorities, userRequest.getIdToken(), baseUser.getUserInfo(), employee);
	}

}
