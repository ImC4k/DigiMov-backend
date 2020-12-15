package com.team_linne.digimov.service;

import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.repository.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Thread.sleep;

@Service
public class SeatStatusTimeoutService {
    private final ThreadPoolExecutor taskExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private Map<Integer, Future<?>> timeoutThreads = new HashMap<>();
    @Value("${timeout.value}")
    private Integer TIMEOUT_IN_SECONDS;

    @Autowired
    private MovieSessionService movieSessionService;
    @Autowired
    private MovieSessionRepository movieSessionRepository;

    public SeatStatusTimeoutService(@Value("${timeout.value}") Integer timeoutValue) {
        this.TIMEOUT_IN_SECONDS = timeoutValue;
        System.out.println("timeout is " + TIMEOUT_IN_SECONDS);
    }

    public void startSeatStatusCountdown(final String movieSessionId, final Integer seatIndex) {
        final int id = new Random().nextInt();
        timeoutThreads.put(id, taskExecutor.submit(() -> {
            try {
                sleep(TIMEOUT_IN_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MovieSession movieSession = movieSessionService.getById(movieSessionId);
            if (movieSession.getOccupied().containsKey(seatIndex) && movieSession.getOccupied().get(seatIndex).getStatus().equals("in process")) {
                // delete the entry
                movieSession.getOccupied().remove(seatIndex);
                movieSessionService.update(movieSessionId, movieSession);
            }
            timeoutThreads.remove(id);
        }));
    }

    public void setTimeoutInSeconds(Integer timeoutInSeconds) {
        this.TIMEOUT_IN_SECONDS = timeoutInSeconds;
    }
}
