package br.com.deolhonailha.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data // Lombok: Gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Gera um construtor sem argumentos
@AllArgsConstructor // Lombok: Gera um construtor com todos os argumentos
@Entity // JPA: Marca esta classe como uma entidade JPA (mapeada para uma tabela)
@Table(name = "users", uniqueConstraints = { // JPA: Especifica o nome da tabela e constraints
        @UniqueConstraint(columnNames = "email"), // Email deve ser único
        @UniqueConstraint(columnNames = "cpf")    // CPF deve ser único
})
public class User implements UserDetails { // Implementa UserDetails para integração com Spring Security

    @Id // JPA: Marca este campo como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Configura a geração automática do ID
    private Long id;

    @Column(nullable = false) // JPA: Mapeia para uma coluna, não pode ser nula
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email; // Será usado como username para login

    @Column(nullable = false)
    private String senha; // Senha armazenada com hash

    // Relacionamento: Um Usuário pode ter muitos Feedbacks
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    // Coleção de papéis (roles) para o Spring Security
    @ElementCollection(fetch = FetchType.EAGER) // EAGER para carregar os papéis junto com o usuário
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>(); // Ex: "ROLE_USER", "ROLE_ADMIN"

    // --- Métodos da interface UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // Usaremos o email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Por padrão, a conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Por padrão, a conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Por padrão, as credenciais não expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Por padrão, a conta está habilitada
    }
}