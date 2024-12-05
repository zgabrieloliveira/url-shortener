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

    /**
     * recebe uma URL e valida ela, verificando seu formato com java.net.URL
     *
     * @return true se conseguir criar um objeto URL, false caso contrário
     */
    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    /**
     * recebe uma URL, verifica se já não foi processada através de consulta no BD, caso não tenha sido, gera uma
     * @param url, url a ser processada
     * @return String da URL encurtada
     */
    public String shortenUrl(String url) {
        String existingShortUrl = urlAlreadyShortened(url);
        String shortUrl = existingShortUrl != null ? existingShortUrl : generateShortenedUrl();
        Url urlObj = new Url(url, shortUrl);
        urlRepository.save(urlObj);
        return shortUrl;
    }

    // verifica se URL já existe no banco, se tiver sido, retorna a correspondência, senão null
    public String urlAlreadyShortened(String url) {
        Optional<Url> urlFound = urlRepository.getUrlByOriginal(url);
        return urlFound.map(Url::getShortened).orElse(null);
    }

    // recebe uma URL encurtada, busca no BD e retorna correspondência ou null
    public String getOriginalUrl(String shortenedUrl) {
        Optional<Url> url = urlRepository.getUrlByShortened(shortenedUrl);
        return url.map(Url::getOriginal).orElse(null);
    }

    // gerado com uuid para unicidade, evitando colisões entre as urls processadas
    private String generateShortenedUrl() {
        return Base64.getUrlEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
    }

}
