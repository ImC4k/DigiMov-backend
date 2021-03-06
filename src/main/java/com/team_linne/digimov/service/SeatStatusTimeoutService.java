package com.team_linne.digimov.service;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

@Service
public class SeatStatusTimeoutService {
    private final ThreadPoolExecutor taskExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private Map<Integer, Future<?>> timeoutThreads = new HashMap<>();
    @Value("${timeout.value}")
    private Integer timeoutInSeconds;

    @Autowired
    private MovieSessionService movieSessionService;
    @Autowired
    private MovieSessionRepository movieSessionRepository;

    public SeatStatusTimeoutService(@Value("${timeout.value}") Integer timeoutValue) {
        this.timeoutInSeconds = timeoutValue;
    }

    public void startSeatStatusCountdown(final String movieSessionId, final List<Integer> seatIndices) {
        final int id = new Random().nextInt();
        AtomicBoolean shouldUpdateDb = new AtomicBoolean(false);
        timeoutThreads.put(id, taskExecutor.submit(() -> {
            try {
                sleep(timeoutInSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MovieSession movieSession = movieSessionService.getById(movieSessionId);
            seatIndices.forEach(seatIndex -> {
                if (movieSession.getOccupied().containsKey(seatIndex) && movieSession.getOccupied().get(seatIndex).getStatus().equals(MovieSessionService.IN_PROCESS)) {
                    movieSession.getOccupied().remove(seatIndex);
                    shouldUpdateDb.set(true);
                }
            });
            if (shouldUpdateDb.get()) {
                movieSessionService.update(movieSessionId, movieSession);
            }
            timeoutThreads.remove(id);
        }));
    }

    public void setTimeoutInSeconds(Integer timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
    }
}
