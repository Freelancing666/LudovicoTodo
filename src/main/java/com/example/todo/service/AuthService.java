package com.example.todo.service;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private Map<String, User> sessions = new HashMap<>(); // Mappa sessioni (token -> utente)
    private Map<String, String> userTokens = new HashMap<>(); // Mappa username -> token (per verificare se è già autenticato)

    public String register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username già in uso!");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 🔹 Qui potresti aggiungere hashing della password!
        userRepository.save(newUser);

        return "Utente registrato con successo!";
    }

    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            // 🔹 Se l'utente è già autenticato, restituiamo il token esistente
            if (userTokens.containsKey(user.getUsername())) {
                return userTokens.get(user.getUsername());
            }

            // 🔹 Altrimenti, generiamo un nuovo token e lo registriamo nella sessione
            String token = UUID.randomUUID().toString();
            sessions.put(token, user);
            userTokens.put(user.getUsername(), token);

            return token;
        }
        throw new RuntimeException("Credenziali errate");
    }

    public User getUserFromToken(String token) {
        return sessions.get(token);
    }

    public void logout(String token) {

        User user = sessions.get(token);
        if (user != null) {
            userTokens.remove(user.getUsername()); // Rimuoviamo l'utente dalla mappa username-token
            sessions.remove(token); // Rimuoviamo il token dalla sessione
        } else {
            throw new RuntimeException("Nessun utente loggato");
        }
    }

    public void deleteUser(String token) {
        User user = sessions.get(token);
        if (user == null) {
            throw new RuntimeException("Utente non autenticato");
        }

        // Rimuove l'utente dal database
        userRepository.delete(user);

        // Rimuove la sessione dell'utente
        userTokens.remove(user.getUsername());
        sessions.remove(token);
    }
}

