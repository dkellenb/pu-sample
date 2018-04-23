package com.mwiesner.pto.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import com.mwiesner.pto.domain.UserRole;
import com.mwiesner.pto.domain.UserRoleQueryInPort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
public class PTOGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {

	@NonNull
	private UserRoleQueryInPort userRoleQueryInPort;

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

		extractIdTokenClaims(authorities, mappedAuthorities);

		mapRolesToRights(this.userRoleQueryInPort, mappedAuthorities);

		return mappedAuthorities;
	}

	private static void mapRolesToRights(UserRoleQueryInPort userRoleQueryInPort,
			Set<GrantedAuthority> mappedAuthorities) {
		mappedAuthorities.forEach(authority -> {
			try {
				String authorityWithoutRolePrefix = authority.getAuthority().substring(5);
				UserRole userRole = userRoleQueryInPort.findUserRoleByName(authorityWithoutRolePrefix);
				mappedAuthorities.addAll(
						AuthorityUtils.commaSeparatedStringToAuthorityList(userRole.toCommaSeperatedRightsList()));
			} catch (NoSuchElementException ex) {
				log.info("Role {} not found", authority.getAuthority());
			}
		});
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
