package dev.gabriel.url_shortener.dto;

public record ResponseDTO<T>(boolean success, T data, String message) {}
