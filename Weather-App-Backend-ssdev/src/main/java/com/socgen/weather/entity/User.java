package com.socgen.weather.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Renamed to a more conventional "id"

    @Column(nullable = false)  // New non-nullable constraint
    private String name;  // Combines firstName and lastName into a single field

    @Column(unique = true, nullable = false)  // Unique and non-nullable
    private String email;  // Ensures email is unique

    @Column(nullable = false)  // Password must not be null
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)  // Role must not be null
    private Role role;

    // Default constructor (used by JPA)
    public User() {
    }

    // Parameterized constructor (with the required fields)
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Methods from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;  // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can customize this logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // You can customize this logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // You can customize this logic
    }

    @Override
    public boolean isEnabled() {
        return true;  // You can customize this logic
    }
}
