package com.bobocode.oop.service;

import com.bobocode.oop.data.FlightDao;
import com.bobocode.util.ExerciseNotCompletedException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link FlightService} provides an API that allows to manage flight numbers
 * <p>
 * todo: 1. Using {@link com.bobocode.oop.data.FlightDao} implement method {@link FlightService#registerFlight(String)}
 * todo: 2. Using {@link com.bobocode.oop.data.FlightDao} implement method {@link FlightService#searchFlights(String)}
 */
public class FlightService {
    private FlightDataAccess flightDataAccess;

    public FlightService(FlightDataAccess flightDataAccess) {
        this.flightDataAccess = flightDataAccess;
    }

    /**
     * Adds a new flight number
     *
     * @param flightNumber a flight number to add
     * @return {@code true} if a flight number was added, {@code false} otherwise
     */
    public boolean registerFlight(String flightNumber) {
        return flightDataAccess.register(flightNumber);
    }

    /**
     * Returns all flight numbers that contains a provided key.
     *
     * @param query a search query
     * @return a list of found flight numbers
     */
    public List<String> searchFlights(String query) {
        return flightDataAccess.findAll().stream()
                .filter(flightNumber -> flightNumber.contains(query))
                .collect(Collectors.toList());
    }
}
