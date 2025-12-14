package org.example.minishop.controller;


import org.example.minishop.dto.LoginRequest;
import org.example.minishop.dto.SignUpRequest;
import org.example.minishop.model.User;
import org.example.minishop.service.UserService;
import org.example.minishop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String username = request.getUsername();
        User user = userService.getUser(username);

        String token = jwtUtil.generateToken(request.getUsername());

        // Create a JSON object: {"token": "eyJhb..."}
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("role", user.getRole().toString());

        return ResponseEntity.ok(response);
    }



    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody SignUpRequest request) {
        try {
            userService.registerUser(request);
            String token = jwtUtil.generateToken(request.getUsername());
            String username = request.getUsername();
            User user = userService.getUser(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("role", user.getRole().toString());
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }
    }

}
