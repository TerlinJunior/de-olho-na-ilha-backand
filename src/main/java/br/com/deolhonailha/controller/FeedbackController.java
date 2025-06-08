package br.com.deolhonailha.controller;

import br.com.deolhonailha.model.Feedback;
import br.com.deolhonailha.model.StatusFeedback;
import br.com.deolhonailha.model.TipoFeedback;
import br.com.deolhonailha.model.User;
import br.com.deolhonailha.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Endpoint para criar um novo feedback.
    // Consome 'multipart/form-data' por causa do upload de arquivos.
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> criarFeedback(
            // @AuthenticationPrincipal injeta o usuário atualmente autenticado
            @AuthenticationPrincipal User userDetails,
            // @RequestParam para os campos de texto do formulário
            @RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("tipo") TipoFeedback tipo,
            // @RequestPart para os arquivos
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        try {
            Feedback feedback = new Feedback();
            feedback.setTitulo(titulo);
            feedback.setDescricao(descricao);
            feedback.setLatitude(latitude);
            feedback.setLongitude(longitude);
            feedback.setTipo(tipo);

            Feedback novoFeedback = feedbackService.criarFeedback(feedback, userDetails.getId(), fotos);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFeedback);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar as fotos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint PÚBLICO para listar todos os feedbacks
    @GetMapping
    public ResponseEntity<List<Feedback>> listarTodosFeedbacks() {
        List<Feedback> feedbacks = feedbackService.listarTodosFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    // Endpoint PÚBLICO para buscar um feedback por ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> buscarFeedbackPorId(@PathVariable Long id) {
        try {
            Feedback feedback = feedbackService.buscarFeedbackPorId(id);
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para um administrador atualizar o status de um feedback
    @PutMapping("/{id}/status")
    // A anotação @PreAuthorize exige que o usuário tenha o papel 'ADMIN' para acessar este método.
    // Para funcionar, a anotação @EnableMethodSecurity deve estar na classe SecurityConfig.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam StatusFeedback novoStatus) {
        try {
            Feedback feedbackAtualizado = feedbackService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(feedbackAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
