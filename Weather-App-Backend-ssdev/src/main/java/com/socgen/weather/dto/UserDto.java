package com.socgen.weather.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    private String name;  // Replaced firstName and lastName with a single field

    @NotBlank
    @Email  // Ensures email format validation
    private String email;  // Email should be unique and non-nullable

    @NotBlank
    private String password;  // Non-nullable

    // If 'city' is no longer required, you can remove it from the DTO
    // private String city;
}
