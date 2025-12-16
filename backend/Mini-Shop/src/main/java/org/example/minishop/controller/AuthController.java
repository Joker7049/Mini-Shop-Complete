package org.example.minishop.controller;


import jakarta.validation.Valid;
import org.example.minishop.dto.LoginRequest;
import org.example.minishop.dto.SignUpRequest;
import org.example.minishop.model.User;
import org.example.minishop.service.UserService;
import org.example.minishop.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        // 1. Attempt authentication
        // If password is wrong, this throws BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Fetch User Details
        String username = request.getUsername();
        User user = userService.getUser(username);

        // 3. Generate Token
        String token = jwtUtil.generateToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("role", user.getRole().toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody SignUpRequest request) {
        // 1. Register User
        // If username exists, Service throws BadRequestException -> Global Handler catches it.
        userService.registerUser(request);

        // 2. Auto-Login Logic (Generate token immediately)
        // We fetch the user again to ensure we have the correct Role/ID from DB
        User user = userService.getUser(request.getUsername());
        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());

        return ResponseEntity.ok(response);
    }
}
