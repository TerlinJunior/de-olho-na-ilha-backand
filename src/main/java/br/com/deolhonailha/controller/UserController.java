// src/main/java/br/com/deolhonailha/controller/UserController.java
package br.com.deolhonailha.controller;

import br.com.deolhonailha.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal injeta o usuário logado diretamente aqui.
        // Se não houver ninguém logado, currentUser será nulo.
        return ResponseEntity.ok(currentUser);
    }
}
