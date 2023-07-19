package com.meta.sports.user.domain;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class UserAuthoritySwitcher implements GrantedAuthority {

    private String role;

    private Authentication source;

    private List<SimpleGrantedAuthority> authorities;

    public UserAuthoritySwitcher(String role, List<SimpleGrantedAuthority> authorities) {
        this.role = role;
        this.authorities = authorities;
    }

    public void attachAuthority(SimpleGrantedAuthority authority) {
        this.authorities.add(authority);
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    public void setSource(Authentication source) {
        if (this.source == null && source != null && source.isAuthenticated() && source.getAuthorities().size() > 0
            && source.getAuthorities().stream().findFirst().get() instanceof UserAuthoritySwitcher
            && source.getAuthorities().stream().findFirst().get() == this
            && ((UserAuthoritySwitcher)source.getAuthorities().stream().findFirst().get()).authorities == authorities) {
            this.source = source;
        }
    }

    public void switchAuthority(String authority, Authentication authentication) {
        if (!source.equals(authentication)) {
            throw new AccessDeniedException("unauthorized action");
        }
        else if (!authority.equalsIgnoreCase(this.role) &&
                 !authorities.stream().anyMatch(item -> item.getAuthority().equalsIgnoreCase(authority))) {
            throw new AccessDeniedException("unauthorized action");
        }
        else {
            this.role = authority;
        }
    }
}
