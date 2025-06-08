package br.com.deolhonailha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fotos_feedback") // Tabela "fotos_feedback"
public class Foto { // Classe "Foto"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoConteudo;

    @Lob
    @Column(name = "dados", columnDefinition="BYTEA", nullable = false)
    private byte[] dados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    public Foto(String nomeArquivo, String tipoConteudo, byte[] dados, Feedback feedback) {
        this.nomeArquivo = nomeArquivo;
        this.tipoConteudo = tipoConteudo;
        this.dados = dados;
        this.feedback = feedback;
    }
}