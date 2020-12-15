package com.team_linne.digimov.service;

import com.team_linne.digimov.dto.MovieSessionPatchRequest;
import com.team_linne.digimov.exception.InvalidSeatUpdateOperationException;
import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MovieSessionService {
    @Autowired
    private MovieSessionRepository movieSessionRepository;

    public List<MovieSession> getAll() {
        return this.movieSessionRepository.findAll();
    }

    public MovieSession getById(String id) {
        return this.movieSessionRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public MovieSession create(MovieSession movieSession) {
        return this.movieSessionRepository.insert(movieSession);
    }

    public MovieSession update(String id, MovieSession movieSessionUpdate) {
        MovieSession movieSession = this.getById(id);
        movieSessionUpdate.setId(movieSession.getId());
        return this.movieSessionRepository.save(movieSessionUpdate);
    }

    public MovieSession patch(String id, MovieSessionPatchRequest movieSessionPatchRequest) {
        MovieSession movieSession = this.getById(id);
        Map<Integer, SeatStatus> occupied = movieSession.getOccupied();
        movieSessionPatchRequest.getBookedSeatIndices().forEach(seatIndex -> {
            if (occupied.containsKey(seatIndex)) {
                throw new InvalidSeatUpdateOperationException();
            }
        });

        movieSessionPatchRequest.getBookedSeatIndices().forEach(seatIndex -> {
            SeatStatus seatStatus = new SeatStatus("in process", System.currentTimeMillis(), movieSessionPatchRequest.getClientSessionId());
            occupied.put(seatIndex, seatStatus);
        });
        movieSession.setOccupied(occupied);
        return this.update(movieSession.getId(), movieSession);
    }

    public void delete(String id) {
        MovieSession movieSession = this.getById(id);
        this.movieSessionRepository.deleteById(movieSession.getId());
    }
}
