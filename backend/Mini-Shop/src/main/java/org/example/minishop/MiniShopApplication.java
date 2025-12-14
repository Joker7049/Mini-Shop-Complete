package org.example.minishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }

}
