package com.example.rental.infrastructure.controller;

import com.example.rental.application.dto.AuthenticationRequest;
import com.example.rental.application.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserUseCase userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Usuario registrado con éxito");
    }
}
