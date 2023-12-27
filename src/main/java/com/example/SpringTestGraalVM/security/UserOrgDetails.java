package com.example.SpringTestGraalVM.security;

import com.example.SpringTestGraalVM.model.UserOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

public class UserOrgDetails implements UserDetails {

    private final UserOrg userOrg;

    @Autowired
    public UserOrgDetails(UserOrg userOrg) {
        this.userOrg = userOrg;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userOrg.getRole()));
    }

    @Override
    public String getPassword() {
        return this.userOrg.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userOrg.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserOrg getPerson(){
        return this.userOrg;
    }
}
