package com.team_linne.digimov.service;

import com.team_linne.digimov.dto.MovieSessionPatchRequest;
import com.team_linne.digimov.exception.InvalidSeatUpdateOperationException;
import com.team_linne.digimov.exception.MovieNotFoundException;
import com.team_linne.digimov.exception.UnauthorizedSeatUpdateOperationException;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieSessionService {
    private static final String IN_PROCESS = "in process";
    private static final String SOLD = "sold";

    @Autowired
    private MovieSessionRepository movieSessionRepository;
    @Autowired
    private SeatStatusTimeoutService seatStatusTimeoutService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private HouseService houseService;

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
            if (occupied.containsKey(seatIndex) && occupied.get(seatIndex).getStatus().equals(SOLD)) {
                throw new InvalidSeatUpdateOperationException();
            }
        });

        Integer firstSeatIndex = movieSessionPatchRequest.getBookedSeatIndices().get(0);
        String statusOfFirstSeatIndex = movieSession.getOccupied().containsKey(firstSeatIndex)? movieSession.getOccupied().get(firstSeatIndex).getStatus() : null;
        movieSessionPatchRequest.getBookedSeatIndices().forEach(index -> {
            if ((movieSession.getOccupied().containsKey(index) && !movieSession.getOccupied().get(index).getStatus().equals(statusOfFirstSeatIndex))) {
                throw new InvalidSeatUpdateOperationException();
            }
            if (!movieSession.getOccupied().containsKey(index) && statusOfFirstSeatIndex != null) {
                throw new InvalidSeatUpdateOperationException();
            }
        });

        if (statusOfFirstSeatIndex == null) {
            movieSessionPatchRequest.getBookedSeatIndices().forEach(seatIndex -> {
                SeatStatus seatStatus = new SeatStatus(IN_PROCESS, System.currentTimeMillis(), movieSessionPatchRequest.getClientSessionId());
                occupied.put(seatIndex, seatStatus);
                seatStatusTimeoutService.startSeatStatusCountdown(id, seatIndex);
            });
        }
        else {
            movieSessionPatchRequest.getBookedSeatIndices().forEach(index -> {
                if (!movieSession.getOccupied().get(index).getClientSessionId().equals(movieSessionPatchRequest.getClientSessionId())) {
                    throw new UnauthorizedSeatUpdateOperationException();
                }
            });
            movieSessionPatchRequest.getBookedSeatIndices().forEach(occupied::remove);
        }
        movieSession.setOccupied(occupied);
        return this.update(movieSession.getId(), movieSession);
    }

    public void delete(String id) {
        MovieSession movieSession = this.getById(id);
        this.movieSessionRepository.deleteById(movieSession.getId());
    }

    public List<MovieSession> getUpcomingMovieSessionsByCinemaId(String cinemaId) {
        List<House> housesFromRequiredCinema = houseService.getByCinemaId(cinemaId);
        return movieSessionRepository.findAllByHouseIdInAndStartTimeGreaterThan(housesFromRequiredCinema.stream().map(House::getId).collect(Collectors.toList()), System.currentTimeMillis());
    }

    public List<MovieSession> getAllByCinemaId(String cinemaId) {
        List<House> housesFromRequiredCinema = houseService.getByCinemaId(cinemaId);
        return movieSessionRepository.findAllByHouseIdIn(housesFromRequiredCinema.stream().map(House::getId).collect(Collectors.toList()));
    }

    public List<MovieSession> getUpcomingMovieSessionsByMovieId(String movieId) {
        movieService.getById(movieId);
        return movieSessionRepository.findAllByMovieIdAndStartTimeGreaterThan(movieId, System.currentTimeMillis());
    }

    public List<MovieSession> getAllByMovieId(String movieId) {
        movieService.getById(movieId);
        return movieSessionRepository.findAllByMovieId(movieId);
    }

    public void deleteMovieSessionWithHouseId(String houseId) {
        List<MovieSession> movieSessionWithHouseId = movieSessionRepository.findAllByHouseIdIn(Collections.singletonList(houseId));
        movieSessionWithHouseId.forEach(movieSession -> this.delete(movieSession.getId()));
    }
}
