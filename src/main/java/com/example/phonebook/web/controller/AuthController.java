package com.example.phonebook.web.controller;

import com.example.phonebook.data.entity.User;
import com.example.phonebook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;



    //LOGIN
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        } catch (BadCredentialsException e) {

            System.out.println("Debug: BadCredentialsException caught");
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/auth/login?error=true";  // Return to login page
        } catch (Exception e) {

            System.out.println("Debug: General Exception caught");
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/auth/login?error=true";  // Redirect to the login page with error flag on failure
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }



    //REGISTER
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid User user, Model model) {
        try {
            boolean isRegistered = userService.registerUser(user);
            if (isRegistered) {
                model.addAttribute("success", "Registration successful!");
                return "redirect:/auth/login"; // Redirect to login if registration is successful
            } else {
                model.addAttribute("error", "Username already exists"); // Show error message
                return "auth/register"; // Stay on registration page if user already exists
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // Display error message if username exists
            return "auth/register";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }



    //LOGOUT
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the session and clear authentication
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/auth/login"; // Redirect to login page
    }
}
