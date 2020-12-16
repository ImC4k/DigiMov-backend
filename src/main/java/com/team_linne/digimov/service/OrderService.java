package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.*;
import com.team_linne.digimov.model.CreditCardInfo;
import com.team_linne.digimov.model.MovieSession;
import com.team_linne.digimov.model.Order;
import com.team_linne.digimov.model.SeatStatus;
import com.team_linne.digimov.repository.MovieSessionRepository;
import com.team_linne.digimov.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class OrderService {
    public static final String SOLD = "sold";
    public static final String IN_PROCESS = "in process";
    public static final String CREDIT_CARD_NUMBER_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(String id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public void delete(String id) {
        Order order = this.getById(id);
        orderRepository.deleteById(order.getId());
    }

    public Order create(Order order, CreditCardInfo creditCardInfo, String clientSessionId) {
        MovieSession movieSession = movieSessionRepository.findById(order.getMovieSessionId()).orElseThrow(MovieSessionNotFoundException::new);
        Map<String, Double> prices = movieSession.getPrices();
        if (!isCreditCardNumberValid(creditCardInfo) || isCreditCardExpired(creditCardInfo)) {
            throw new InvalidCreditCardInfoException();
        }
        if (!isSeatAvailable(order.getBookedSeatIndices(), movieSession, clientSessionId)) {
            throw new UnavailableSeatException();
        }

        order.getCustomerGroupQuantityMap().keySet().forEach(key -> {
            if (!prices.containsKey(key)) {
                throw new InvalidCustomerGroupException();
            }
        });
        updateSeatStatus(order, movieSession);
        return orderRepository.save(order);
    }

    private void updateSeatStatus(Order order, MovieSession movieSession) {
        order.getBookedSeatIndices().forEach(seatId -> {
            SeatStatus seatStatus = movieSession.getOccupied().get(seatId);
            seatStatus.setStatus(SOLD);
            seatStatus.setClientSessionId(null);
            seatStatus.setProcessStartTime(null);
        });
    }

    public boolean isCreditCardNumberValid(CreditCardInfo creditCardInfo) {
        Pattern creditCardNumberPattern = Pattern.compile(CREDIT_CARD_NUMBER_REGEX);
        return creditCardNumberPattern.matcher(creditCardInfo.getNumber()).matches();
    }

    public boolean isCreditCardExpired(CreditCardInfo creditCardInfo) {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);
        Integer currentMonth = now.get(Calendar.MONTH);
        Integer expiryYear = Integer.valueOf(creditCardInfo.getExpiryDate().getYear());
        Integer expiryMonth = Integer.valueOf(creditCardInfo.getExpiryDate().getMonth());
        return (expiryYear < currentYear || (expiryYear.equals(currentYear) && expiryMonth < currentMonth));
    }

    public boolean isSeatAvailable(List<Integer> bookedSeatIndices, MovieSession movieSession, String clientSessionId) {
        for (Integer seatId : bookedSeatIndices) {
            SeatStatus seatStatus = movieSession.getOccupied().get(seatId);
            String status = seatStatus.getStatus();
            if (status.equals(SOLD) || (status.equals(IN_PROCESS) && !seatStatus.getClientSessionId().equals(clientSessionId))) {
                return false;
            }
        }
        return true;
    }
}