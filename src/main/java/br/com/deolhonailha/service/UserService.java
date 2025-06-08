package br.com.deolhonailha.service;

import br.com.deolhonailha.dto.UserRegistrationDto;
import br.com.deolhonailha.model.User;
import br.com.deolhonailha.repository.UserRepository;
import br.com.deolhonailha.utils.CpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy; // IMPORTANTE
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    // A anotação @Lazy quebra o ciclo de dependência
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Erro: Já existe uma conta com este e-mail.");
        }
        if (userRepository.existsByCpf(registrationDto.getCpf())) {
            throw new IllegalArgumentException("Erro: Já existe uma conta com este CPF.");
        }
        if (!CpfValidator.isValid(registrationDto.getCpf())) {
            throw new IllegalArgumentException("Erro: CPF inválido.");
        }

        User newUser = new User();
        newUser.setNomeCompleto(registrationDto.getNomeCompleto());
        newUser.setCpf(registrationDto.getCpf());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setSenha(passwordEncoder.encode(registrationDto.getSenha()));
        newUser.setRoles(Collections.singleton("ROLE_USER"));

        User savedUser = userRepository.save(newUser);

        // Linhas de Debug para o nosso teste
        System.out.println("--- DADOS DO USUÁRIO SALVO ---");
        System.out.println("ID: " + savedUser.getId());
        System.out.println("Email: " + savedUser.getEmail());
        System.out.println("Senha Criptografada: " + savedUser.getSenha());
        System.out.println("---------------------------------");

        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByCpf(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail ou CPF: " + username));
    }
}