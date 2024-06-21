package com.conner.assistant.models;

import jakarta.persistence.*;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long userId;
    @Setter
    @Column(unique = true)
    private String username;
    @Setter
    private String password;

    @Setter
    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(name = "user_role_junction")
    private Set<Role> authorities;

    public ApplicationUser(){
        super();
        this.authorities = new HashSet<Role>();
    }

    public ApplicationUser(Long userId, String username, String password, Set<Role> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
