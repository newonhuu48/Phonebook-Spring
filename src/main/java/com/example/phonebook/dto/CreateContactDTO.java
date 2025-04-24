package com.example.phonebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactDTO {

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be a valid number")
    private String phoneNumber;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Email must be valid")
    private String email;


    private boolean isFavorite;
}
