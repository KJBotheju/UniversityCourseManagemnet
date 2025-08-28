package com.erp.universitycoursemanagement.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private String message;

    // Default constructor
    public AuthResponse() {}

    // Constructor for login response
    public AuthResponse(String token, String username, String email, String role) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.type = "Bearer";
    }

    // Full constructor
    public AuthResponse(String token, String type, String username, String email, String role, String message) {
        this.token = token;
        this.type = type != null ? type : "Bearer";
        this.username = username;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    // Builder pattern methods
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // Builder class
    public static class AuthResponseBuilder {
        private String token;
        private String type = "Bearer";
        private String username;
        private String email;
        private String role;
        private String message;

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder type(String type) {
            this.type = type;
            return this;
        }

        public AuthResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AuthResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, type, username, email, role, message);
        }
    }
}
