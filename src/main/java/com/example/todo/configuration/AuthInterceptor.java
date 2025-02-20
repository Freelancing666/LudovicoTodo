package com.example.todo.configuration;

import com.example.todo.entity.User;
import com.example.todo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AuthInterceptor: preHandle called");
        String token = request.getHeader("Authorization");
        if (token == null) {
            System.out.println("token mancante");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante");
            return false;
        }
        token = token.replace("Bearer ","").trim();// Recuperiamo il token dall'header

        User user = authService.getUserFromToken(token); // Recuperiamo l'utente dalla sessione
        if (user == null) {
            System.out.println("Token non valido");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido o sessione scaduta");
            return false;
        }

        request.setAttribute("user", user); // Salviamo l'utente nella richiesta
        return true;
    }
}
