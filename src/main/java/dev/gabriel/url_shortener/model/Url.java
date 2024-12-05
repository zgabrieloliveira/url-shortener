package dev.gabriel.url_shortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="url")
@Getter
@Setter
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT") // para aceitar urls extensas sem erros
    private String original;
    private String shortened;

    public Url(String original, String shortened) {
        this.original = original;
        this.shortened = shortened;
    }

}
