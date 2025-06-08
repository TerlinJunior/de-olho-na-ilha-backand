package br.com.deolhonailha.service;

import br.com.deolhonailha.model.Feedback;
import br.com.deolhonailha.model.Foto;
import br.com.deolhonailha.model.StatusFeedback;
import br.com.deolhonailha.model.User;
import br.com.deolhonailha.repository.FeedbackRepository;
import br.com.deolhonailha.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Feedback criarFeedback(Feedback feedback, Long userId, List<MultipartFile> arquivosFotos) throws IOException {
        // 1. Busca o usuário que está criando o feedback
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));
        feedback.setUser(user);

        // 2. Salva o feedback (ainda sem as fotos) para gerar o ID
        Feedback feedbackSalvo = feedbackRepository.save(feedback);

        // 3. Processa e associa as fotos, se houver
        if (arquivosFotos != null && !arquivosFotos.isEmpty()) {
            for (MultipartFile arquivo : arquivosFotos) {
                if (!arquivo.isEmpty()) {
                    String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
                    Foto foto = new Foto(
                            nomeArquivo,
                            arquivo.getContentType(),
                            arquivo.getBytes(),
                            feedbackSalvo // Associa a foto ao feedback já salvo
                    );
                    // Adiciona a foto à lista de fotos do feedback
                    feedbackSalvo.addFoto(foto);
                }
            }
            // 4. Salva o feedback novamente, agora com as fotos associadas
            return feedbackRepository.save(feedbackSalvo);
        }

        return feedbackSalvo;
    }

    public List<Feedback> listarTodosFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback buscarFeedbackPorId(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado com ID: " + id));
    }

    @Transactional
    public Feedback atualizarStatus(Long feedbackId, StatusFeedback novoStatus) {
        Feedback feedback = buscarFeedbackPorId(feedbackId);
        feedback.setStatus(novoStatus);
        // O @PreUpdate na entidade Feedback cuidará de atualizar a data
        return feedbackRepository.save(feedback);
    }
}
