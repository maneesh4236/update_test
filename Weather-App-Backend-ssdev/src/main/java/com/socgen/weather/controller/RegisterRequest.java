package com.socgen.weather.controller;  // Ensure the correct package

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;  // Email validation
import jakarta.validation.constraints.NotBlank;  // Non-nullable validation

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank  // Ensure this field is not empty
    private String name;  // Unified field for name

    @NotBlank  // Ensures email is provided
    @Email  // Ensures email has a valid format
    private String email;  // Unique and non-nullable

    @NotBlank  // Ensure this field is not empty
    private String password;  // Non-nullable

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
// Since the city field is removed from the entity, it's not needed here
}
