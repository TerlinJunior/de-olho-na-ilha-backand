package br.com.deolhonailha.repository;

import br.com.deolhonailha.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    // Por enquanto, os métodos básicos do JpaRepository (save, findById, etc.) são suficientes.
    // Se precisarmos de buscas mais específicas no futuro, podemos adicioná-las aqui.
}