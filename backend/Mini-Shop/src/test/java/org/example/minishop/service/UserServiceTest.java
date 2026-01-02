package org.example.minishop.service;


import org.example.minishop.dto.SignUpRequest;
import org.example.minishop.exception.BadRequestException;
import org.example.minishop.model.Order;
import org.example.minishop.model.Role;
import org.example.minishop.model.User;
import org.example.minishop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void registerUser_existedUser_throwsBadRequestException() {
        SignUpRequest signUpRequest = new SignUpRequest("testUser", "password", "testEmail@gamil.com");
        User user = new User();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.registerUser(signUpRequest));
    }
}
