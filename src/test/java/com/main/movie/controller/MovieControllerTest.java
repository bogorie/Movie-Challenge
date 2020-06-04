package com.main.movie.controller;

import com.main.movie.MockGenerator;
import com.main.movie.model.*;
import com.main.movie.service.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieServiceImpl movieService;

    private int mockMovieId;

    @BeforeEach
    void setMovieService(){
        this.mockMovieId = 650;
    }

    @Test
    void getMovie() throws Exception {
        Mono<MovieDTO> movie = Mono.just(MockGenerator.getMockMovie().get());

        BDDMockito.given(movieService.getMovie(mockMovieId))
                .willReturn(movie);
        MvcResult response = mockMvc.perform(get("/api/movie/"+mockMovieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getAsyncResult().toString();
        String result = movie.block().toString();
        assertEquals(content, result);
    }

    @Test
    void getMovieDetails() throws Exception {
        Mono<MovieDetail> movieDetail = Mono.just(MockGenerator.getMockMovieDetail().get());

        BDDMockito.given(movieService.getApiDetails(mockMovieId))
                .willReturn(movieDetail);
        MvcResult response = mockMvc.perform(get("/api/movie/detail/"+mockMovieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getAsyncResult().toString();
        String result = movieDetail.block().toString();
        assertEquals(content, result);
    }

    @Test
    void getApiCast() throws Exception {
        Mono<CreditDTO> credit = Mono.just(MockGenerator.getMockCast().get());

        BDDMockito.given(movieService.getApiCast(mockMovieId))
                .willReturn(credit);
        MvcResult response = mockMvc.perform(get("/api/movie/casts/"+mockMovieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getAsyncResult().toString();
        String result = credit.block().toString();
        assertEquals(content, result);
    }

    @Test
    void getMovies() throws Exception {
        Mono<List<MovieDBResponse>> mono = Mono.just(MockGenerator.getMockMovies());
        Flux<MovieDBResponse> movies = mono.flatMapMany(Flux::fromIterable);

        BDDMockito.given(movieService.getMoviesFromDB(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .willReturn(movies);
        MvcResult response = mockMvc.perform(get("/api/moviesDB")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getAsyncResult().toString();
        String result = mono.block().toString();
        assertEquals(content, result);
    }

    @Test
    void getMovieData() throws Exception {
        Mono<List<MovieResponse>> mono = Mono.just(MockGenerator.getMockMoviesDetail());
        Flux<MovieResponse> movies = mono.flatMapMany(Flux::fromIterable);

        BDDMockito.given(movieService.getMoviesData(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .willReturn(movies);
        MvcResult response = mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = response.getAsyncResult().toString();
        String result = mono.block().toString();
        assertEquals(content, result);
    }
}