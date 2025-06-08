package br.com.deolhonailha.repository;

import br.com.deolhonailha.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Boa prática para indicar ao Spring que esta é uma interface de repositório
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA criará automaticamente a query para este método
    // "SELECT u FROM User u WHERE u.email = ?1"
    Optional<User> findByEmail(String email);

    // "SELECT u FROM User u WHERE u.cpf = ?1"
    Optional<User> findByCpf(String cpf);

    // "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1"
    boolean existsByEmail(String email);

    // "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.cpf = ?1"
    boolean existsByCpf(String cpf);
}
