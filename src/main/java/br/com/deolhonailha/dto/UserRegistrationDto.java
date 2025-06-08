package br.com.deolhonailha.dto;

import lombok.Data;

@Data // Lombok: gera getters, setters, etc.
public class UserRegistrationDto {
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String senha;
}