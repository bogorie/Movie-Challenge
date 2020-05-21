package com.main.movie.config;

import com.main.movie.integration.TheMovieDataBaseAPI;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;

@TestConfiguration
public class TestConfigurationSuite{
    @Bean
    @Primary
    public TheMovieDataBaseAPI createTheMovieDataBaseAPIMock(){
        return (HttpMethod httpMethod, String url) -> {
            String path = "";
            if(url.contains("/credits"))
                path = "src/test/java/com/main/movie/resource/MockCreditsAPI.json";
            else
                path = "src/test/java/com/main/movie/resource/MockMovieAPI.json";

            StringBuilder contentProcessor = new StringBuilder();
            String line;

            try (FileReader fileReader = new FileReader(path);
                 BufferedReader br = new BufferedReader(fileReader) ) {
                while ((line = br.readLine()) != null) {
                    contentProcessor.append(line);
                    contentProcessor.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            WebClient webClient;

            webClient = WebClient.builder()
                    .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("content-type", "application/json")
                            .body(contentProcessor.toString())
                            .build())
                    ).build();

            return webClient.method(httpMethod)
                    .uri(url)
                    .retrieve();
        };
    }
}
