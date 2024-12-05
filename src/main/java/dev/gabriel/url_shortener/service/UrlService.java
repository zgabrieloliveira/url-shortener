package dev.gabriel.url_shortener.service;

import dev.gabriel.url_shortener.model.Url;
import dev.gabriel.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public String shortenUrl(String url) {
        String existingShortUrl = urlAlreadyShortened(url);
        String shortUrl = existingShortUrl != null ? existingShortUrl : generateShortenedUrl();
        Url urlObj = new Url(url, shortUrl);
        urlRepository.save(urlObj);
        return shortUrl;
    }

    public String urlAlreadyShortened(String url) {
        Optional<Url> urlFound = urlRepository.getUrlByOriginal(url);
        return urlFound.map(Url::getShortened).orElse(null);
    }

    public String getOriginalUrl(String shortenedUrl) {
        Optional<Url> url = urlRepository.getUrlByShortened(shortenedUrl);
        return url.map(Url::getOriginal).orElse(null);
    }

    // gerado com uuid para unicidade, evitando colisões entre as urls processadas
    private String generateShortenedUrl() {
        return Base64.getUrlEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
    }

}
