package com.erp.universitycoursemanagement.service;

import com.erp.universitycoursemanagement.dto.*;
import com.erp.universitycoursemanagement.model.*;
import com.erp.universitycoursemanagement.repository.UserRepository;
import com.erp.universitycoursemanagement.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((User) authentication.getPrincipal());

        User userDetails = (User) authentication.getPrincipal();
        return new AuthResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getRole().name());
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsernameAndStatus(signupRequest.getUsername(), Status.ACTIVE)) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmailAndStatus(signupRequest.getEmail(), Status.ACTIVE)) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(signupRequest.getRole())
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully!")
                .build();
    }
}
