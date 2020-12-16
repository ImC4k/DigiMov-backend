package com.team_linne.digimov.mapper;
import com.team_linne.digimov.dto.MovieSessionRequest;
import com.team_linne.digimov.dto.MovieSessionResponse;
import com.team_linne.digimov.model.MovieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
public class MovieSessionMapper {

    public MovieSession toEntity(MovieSessionRequest movieSessionRequest) {
        MovieSession movieSession = new MovieSession();

        BeanUtils.copyProperties(movieSessionRequest, movieSession);

        return movieSession;
    }

    public MovieSessionResponse toResponse(MovieSession movieSession) {
        MovieSessionResponse movieSessionResponse = new MovieSessionResponse();

        BeanUtils.copyProperties(movieSession, movieSessionResponse);

        return movieSessionResponse;
    }
}
