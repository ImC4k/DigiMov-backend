package com.team_linne.digimov.mapper;
import com.team_linne.digimov.dto.GenreRequest;
import com.team_linne.digimov.dto.GenreResponse;
import com.team_linne.digimov.model.Genre;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public Genre toEntity(GenreRequest genreRequest) {
        Genre genre = new Genre();

        BeanUtils.copyProperties(genreRequest, genre);

        return genre;
    }

    public GenreResponse toResponse(Genre genre) {
        GenreResponse genreResponse = new GenreResponse();

        BeanUtils.copyProperties(genre, genreResponse);

        return genreResponse;
    }
}
