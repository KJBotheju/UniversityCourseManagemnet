package com.erp.universitycoursemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_username", columnNames = "username"))
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @Email @NotBlank @Size(max = 100)
    private String email;

    @JsonIgnore
    @NotBlank @Size(min = 6, max = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    // Constructors
    public User() {}

    public User(String username, String email, String password, Role role, Status status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.STUDENT;
        this.status = status != null ? status : Status.ACTIVE;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == Status.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == Status.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == Status.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }

    // Additional getters for fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Builder pattern
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String username;
        private String email;
        private String password;
        private Role role = Role.STUDENT;
        private Status status = Status.ACTIVE;

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public User build() {
            return new User(username, email, password, role, status);
        }
    }
}
