package org.example.minishop.service;


import jakarta.transaction.Transactional;
import org.example.minishop.dto.SignUpRequest;
import org.example.minishop.model.Role;
import org.example.minishop.model.User;
import org.example.minishop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpRequest registerUser(SignUpRequest user) {
        try {
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            newUser.setPassword(encodedPassword);
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            return user;

        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Username is already in use");
        }
    }


    public User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }


    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));


        userRepository.delete(user);
    }
}
