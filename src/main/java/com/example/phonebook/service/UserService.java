package com.example.phonebook.service;

import com.example.phonebook.data.entity.Role;
import com.example.phonebook.data.entity.User;
import com.example.phonebook.data.repository.RoleRepository;
import com.example.phonebook.data.repository.UserRepository;
import com.example.phonebook.dto.UserDTO;
import com.example.phonebook.exception.admin.UserNotFoundException;
import com.example.phonebook.exception.admin.UserNotFoundForEditException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;


    public boolean registerUser(User user) {
        // Check if user already exists
        Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());
        if (existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
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

        userRepository.save(user);
        return true;
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }



    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDTO.class);
    }



    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundForEditException("User not found with id " + id));

        existingUser.setUsername(userDTO.getUsername());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundForEditException("User not found with id " + id));

        userRepository.delete(existingUser);
    }
}
