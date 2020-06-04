# WFH Movie Challenge
The idea of this challenge is to improve interdisciplinary skills by developing a simple project in multiple stages. Each stage is designed to develop some specific skills. This challenge is not tied to any specific technology or language. We want to allow participants to explore and choose what best suits the problem and their capabilities.
## Stage 1
Data extraction – organization and simple visualization

# [Swagger API Documentation](https://secret-atoll-56548.herokuapp.com/swagger-ui.html#!/movie-controller)

### Endpoints
1. Shows movies from data base
  https://secret-atoll-56548.herokuapp.com/api/moviesDB
  
    | QueryParam | What is  | Option | Example |
    | :-----: | :- | :-: | :-: |
    | sort | Sort movies according to title | title | title |
    | genres | Filtered by gender, if there are several, they must be separated by commas | any string | comedy,drama |
    | limit | Amounts of movies displayed per page | any number | 10 |
    | page | Pagination | any number | 1 |
    | title | Filter by specific search on this parameter | any string | Toy |

2. Shows movies in detail using the TMDB API query
  https://secret-atoll-56548.herokuapp.com/api/movies
    
    | QueryParam | What is  | Option | Example |
    | :-----: | :- | :-: | :-: |
    | sort | Sort movies according to title | title | title |
    | genres | Filtered by gender, if there are several, they must be separated by commas | any string | comedy,drama |
    | limit | Amounts of movies displayed per page | any number | 10 |
    | page | Pagination | any number | 1 |
    | title | Filter by specific search on this parameter | any string | Toy |
    
3. Shows the specific movie from data base
  https://secret-atoll-56548.herokuapp.com/api/movie/{movie_id}

4. Shows the sécific movie in detail using TMDB API query
  https://secret-atoll-56548.herokuapp.com/api/movie/detail/{movie_id}
  
5. Shows the actors of the movie using TMDB API query
  https://secret-atoll-56548.herokuapp.com/api/movie/casts/{movie_id} 
 
6. Shows movie rating per years
  https://secret-atoll-56548.herokuapp.com/api/movie/rating/{movie_id}
  
7. Shows all genres
  https://secret-atoll-56548.herokuapp.com/api/movies/genres
  
### Note
For front-end, we will use endpoints:
- https://secret-atoll-56548.herokuapp.com/api/movies
- https://secret-atoll-56548.herokuapp.com/api/movie/detail/{movie_id}
- https://secret-atoll-56548.herokuapp.com/api/movie/casts/{movie_id}
- https://secret-atoll-56548.herokuapp.com/api/movie/rating/{movie_id}
- https://secret-atoll-56548.herokuapp.com/api/movies/genres
