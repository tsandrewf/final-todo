package ru.jabki.final_todo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalUserService {

    private final RestClient restClient;

    public ExternalUserService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8083/api/v1")
                .build();
    }

    public boolean isUserExists(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/user/exists/{id}", userId)
                .retrieve().body(String.class));
    }
}
