package dev.gabriel.url_shortener.repository;

import dev.gabriel.url_shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> getUrlByShortened(String shortenedUrl);
    Optional<Url> getUrlByOriginal(String originalUrl);
}
