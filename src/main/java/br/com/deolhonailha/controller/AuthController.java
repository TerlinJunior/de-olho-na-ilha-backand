package br.com.deolhonailha.controller;

import br.com.deolhonailha.dto.LoginRequestDto;
import br.com.deolhonailha.dto.UserRegistrationDto;
import br.com.deolhonailha.model.User;
import br.com.deolhonailha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marca a classe como um controlador REST
@RequestMapping("/api/auth") // Todas as URLs neste controlador começarão com /api/auth
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User newUser = userService.registerNewUser(registrationDto);
            // Retorna uma resposta de sucesso (201 Created)
            return ResponseEntity.status(201).body("Usuário registrado com sucesso! ID: " + newUser.getId());
        } catch (IllegalArgumentException e) {
            // Retorna uma resposta de erro (400 Bad Request) com a mensagem da exceção
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        // Cria um objeto de autenticação com o username (email/cpf) e a senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Se a autenticação for bem-sucedida, o Spring Security armazena o usuário autenticado
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Em uma API real e moderna, aqui você geraria um Token JWT (JSON Web Token)
        // e o retornaria para o cliente. Por simplicidade, vamos apenas retornar uma
        // mensagem de sucesso.
        return ResponseEntity.ok("Usuário logado com sucesso!");
    }
}
