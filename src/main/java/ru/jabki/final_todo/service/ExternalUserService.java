package ru.jabki.final_todo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalUserService {

    private final RestClient restClient;

    public ExternalUserService(@Value("${external.user.service.baseurl:http://localhost:8083}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean isUserExists(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/api/v1/user/exists/{id}", userId)
                .retrieve().body(String.class));
    }
}
