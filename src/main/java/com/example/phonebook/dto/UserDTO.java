package com.example.phonebook.dto;

import com.example.phonebook.data.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    private List<Role> roles;
}
