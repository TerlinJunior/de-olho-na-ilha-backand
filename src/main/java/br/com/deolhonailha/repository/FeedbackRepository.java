package br.com.deolhonailha.repository;

import br.com.deolhonailha.model.Feedback;
import br.com.deolhonailha.model.StatusFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Busca todos os feedbacks de um usuário específico pelo ID do usuário
    List<Feedback> findByUser_Id(Long userId);

    // Busca todos os feedbacks com um determinado status
    List<Feedback> findByStatus(StatusFeedback status);

    // Você pode combinar campos:
    // Ex: List<Feedback> findByStatusAndTipo(StatusFeedback status, TipoFeedback tipo);
}
