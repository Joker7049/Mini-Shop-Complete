package org.example.minishop.service;


import jakarta.transaction.Transactional;
import org.example.minishop.dto.SignUpRequest;
import org.example.minishop.exception.BadRequestException;
import org.example.minishop.exception.ResourceNotFoundException;
import org.example.minishop.model.Role;
import org.example.minishop.model.User;
import org.example.minishop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpRequest registerUser(SignUpRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username '" + request.getUsername() + "' is already in use");
        }

        try {
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            return request;
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Username is already in use");
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        userRepository.delete(user);
    }
}
