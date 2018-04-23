package com.mwiesner.employee.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

public class PUGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {


	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

		extractIdTokenClaims(authorities, mappedAuthorities);

		return mappedAuthorities;
	}


	private static void extractIdTokenClaims(Collection<? extends GrantedAuthority> authorities,
			Set<GrantedAuthority> mappedAuthorities) {
		authorities.forEach(authority -> {
			if (OidcUserAuthority.class.isInstance(authority)) {
				OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
				OidcIdToken idToken = oidcUserAuthority.getIdToken();

				List<String> rolesAsStringList = idToken.getClaimAsStringList("roles");
				rolesAsStringList.forEach( role -> {
					mappedAuthorities.add(new SimpleGrantedAuthority(role));
				});
			}
		});
	}

}
