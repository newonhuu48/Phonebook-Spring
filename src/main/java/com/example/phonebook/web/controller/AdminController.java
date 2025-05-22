package com.example.phonebook.web.controller;

import com.example.phonebook.dto.UserDTO;
import com.example.phonebook.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "admin/dashboard";
    }


    //EDIT USER
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("user", userDTO);

        return "admin/edit-user";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute("user") @Valid UserDTO userDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "admin/edit-user";
        }

        userService.updateUser(id, userDTO);
        return "redirect:/admin/dashboard";
    }



    //DELETE USER
    @GetMapping("/delete/{id}")
    public String showDeleteUserForm(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("user", userDTO);

        return "admin/delete-user";
    }

    @PostMapping("/delete/{id}")
    public String softDeleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}