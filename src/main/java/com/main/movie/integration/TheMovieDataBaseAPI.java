package com.main.movie.integration;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public interface TheMovieDataBaseAPI {
    WebClient.ResponseSpec call(HttpMethod httpMethod, String url);
}
