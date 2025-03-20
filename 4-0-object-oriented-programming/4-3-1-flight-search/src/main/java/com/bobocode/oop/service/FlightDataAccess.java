package com.bobocode.oop.service;

import java.util.Set;

public interface FlightDataAccess {
    boolean register(String flightNumber);
    Set<String> findAll();
}
