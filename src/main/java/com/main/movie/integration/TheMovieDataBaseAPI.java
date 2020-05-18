package com.main.movie.integration;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@NoArgsConstructor
public class TheMovieDataBaseAPI {

    @Autowired
    private Environment env;
    @Autowired
    private WebClient.Builder webClientBuider ;
    private WebClient webClient;

    private WebClient buildWebClient(WebClient.Builder webClientBuilder, String baseUrl){
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    public WebClient.ResponseSpec call(HttpMethod httpMethod, String url) {
        webClient = buildWebClient(webClientBuider,env.getProperty("api.host"));
        return webClient.method(httpMethod)
                .uri(url)
                .retrieve();
    }
}
