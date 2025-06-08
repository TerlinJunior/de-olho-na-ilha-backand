package br.com.deolhonailha.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username; // O usuário poderá enviar o email ou o CPF aqui
    private String password;
}
