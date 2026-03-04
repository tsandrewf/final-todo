package ru.jabki.final_todo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class ExternalUserService {

    private final RestClient restClient;

    public ExternalUserService() {
        String baseUrl = null;

        {
            Properties properties = new Properties();
            InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream("application.properties");
            try {
                properties.load(inputStream);
                baseUrl = properties.getProperty("external.user.service.baseurl");
            } catch (IOException e) {

            }
        }
        if (baseUrl == null) {
            baseUrl = "http://localhost:8083/api/v1/user";
        }

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean isUserExists(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/exists/{id}", userId)
                .retrieve().body(String.class));
    }
}
