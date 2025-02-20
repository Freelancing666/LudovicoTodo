package com.example.todo.controller;

import com.example.todo.dto.CredentialsDto;
import com.example.todo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//locahost:8093/auth/register
//localhost:8093/auth/login
//localhost:8093/auth/logout
//localhost:8093/auth/deleteUser
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CredentialsDto credentials) {
        try {
            String message = authService.register(credentials);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentials) {
        try {
            String token = authService.login(credentials);
            return ResponseEntity.ok(Map.of("token", token)); // 🔹 Ora il token è unico per ogni utente
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ","").trim();
            authService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Logout effettuato con successoo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ","").trim();
            authService.deleteUser(token);
            return ResponseEntity.ok(Map.of("message", "Utente eliminato con successo"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

}
