package com.team_linne.digimov.mapper;

import com.team_linne.digimov.dto.GenreResponse;
import com.team_linne.digimov.dto.MovieRequest;
import com.team_linne.digimov.dto.MovieResponse;
import com.team_linne.digimov.model.Movie;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {
    public Movie toEntity(MovieRequest movieRequest) {
        Movie movie = new Movie();

        BeanUtils.copyProperties(movieRequest, movie);

        return movie;
    }

    public MovieResponse toResponse(Movie movie, List<GenreResponse> genres) {
        MovieResponse movieResponse = new MovieResponse();

        BeanUtils.copyProperties(movie, movieResponse);

        movieResponse.setGenres(genres);

        return movieResponse;
    }
}
