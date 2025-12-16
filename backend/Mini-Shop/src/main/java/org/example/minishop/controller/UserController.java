package org.example.minishop.controller;


import jakarta.validation.constraints.NotBlank;
import org.example.minishop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(
            @NotBlank(message = "username cannot be empty")
            @PathVariable String username) {


        userService.delete(username);

        Map<String, String> response = new HashMap<>();
        response.put("message", "The user has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
