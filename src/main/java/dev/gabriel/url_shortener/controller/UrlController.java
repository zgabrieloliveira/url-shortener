package dev.gabriel.url_shortener.controller;

import dev.gabriel.url_shortener.dto.RequestDTO;
import dev.gabriel.url_shortener.dto.ResponseDTO;
import dev.gabriel.url_shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("url")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/short")
    public ResponseEntity<ResponseDTO<String>> shortUrl(@RequestBody RequestDTO urlRequest) {
        try {
            String url = urlRequest.url();
            if (!urlService.isValidUrl(url)) {
                return ResponseEntity.badRequest().body(new ResponseDTO<>(false, null, "Invalid URL"));
            }
            String shortUrl = urlService.shortenUrl(url);
            return ResponseEntity.ok(new ResponseDTO<>(true, shortUrl, "URL successfully shortened"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(false, null, "An error occurred while processing the request"));
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        return originalUrl != null
                ? ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build()
                : ResponseEntity.notFound().build();
    }

}