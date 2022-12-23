package dev.gollund.gitrepoparser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${security.token}")
    private String accessToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .build();
    }

}
