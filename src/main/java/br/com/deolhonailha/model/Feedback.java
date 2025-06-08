package br.com.deolhonailha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks") // Define o nome da tabela no banco de dados como "feedbacks"
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob // Marca o campo para objetos grandes, adequado para textos longos.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 255)
    private String enderecoAproximado;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto (ex: "POSITIVO")
    @Column(nullable = false)
    private TipoFeedback tipo;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto (ex: "PENDENTE")
    @Column(nullable = false)
    private StatusFeedback status;

    @Column(nullable = false, updatable = false) // Define que a data de criação não pode ser atualizada
    private LocalDateTime dataCriacao;

    private LocalDateTime dataUltimaAtualizacao;

    // --- Relacionamentos ---

    // Muitos Feedbacks podem ser de um Usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Define a chave estrangeira "user_id"
    private User user;

    // Um Feedback pode ter muitas Fotos
    @OneToMany(
            mappedBy = "feedback", // Indica que o lado "Foto" da relação gerencia a chave estrangeira
            cascade = CascadeType.ALL, // Operações no Feedback (salvar, deletar) se propagam para as Fotos
            orphanRemoval = true, // Remove fotos que não estão mais associadas a este feedback
            fetch = FetchType.LAZY
    )
    private List<Foto> fotos = new ArrayList<>();

    // --- Métodos de Callback do JPA ---

    @PrePersist // Executa este método antes de salvar a entidade pela primeira vez
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataUltimaAtualizacao = LocalDateTime.now();
        if (status == null) {
            status = StatusFeedback.PENDENTE; // Define um status padrão
        }
    }

    @PreUpdate // Executa este método antes de atualizar uma entidade existente
    protected void onUpdate() {
        dataUltimaAtualizacao = LocalDateTime.now();
    }

    // --- Métodos Utilitários (para gerenciar a relação bidirecional com Foto) ---

    public void addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setFeedback(this); // Mantém a consistência nos dois lados da relação
    }

    public void removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setFeedback(null); // Remove a referência para manter a consistência
    }
}