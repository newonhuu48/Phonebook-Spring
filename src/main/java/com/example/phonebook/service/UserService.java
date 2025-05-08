package com.example.phonebook.service;

import com.example.phonebook.data.entity.Role;
import com.example.phonebook.data.entity.User;
import com.example.phonebook.data.repository.RoleRepository;
import com.example.phonebook.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public boolean registerUser(User user) {
        // Check if user already exists
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            return false;
        }

        // Encode password with BCrypt hashing
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role - USER
        Role role = roleRepository.findByRoleName("ROLE_USER");
        if (role != null) {
            user.setRoles(List.of(role)); // Set the role for the user
        } else {
            // If ROLE_USER does not exist, create it and assign to the user
            role = new Role();
            role.setRoleName("ROLE_USER");
            roleRepository.save(role);
            user.setRoles(List.of(role));
        }

        userRepository.save(user); // Save user in the database
        return true;
    }
}
