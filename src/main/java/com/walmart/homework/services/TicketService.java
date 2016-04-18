package com.walmart.homework.services;

import com.walmart.homework.pojo.SeatPojo;

import java.util.List;
import java.util.Optional;

/**
 * Created by E on 4/17/2016.
 * TicketService implementation provided by the assignment
 */

public interface TicketService {
    /**
     * The number of seats in the requested level that are neither held nor reserved
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
    int numSeatsAvailable(Optional<Integer> venueLevel);
    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level to search
     * @param maxLevel the maximum venue level to search
     * @param customerEmail unique identifier for the customer
     * @return an array of SeatPojo objects identifying the specific seats and related
    information
     */
    List<SeatPojo> findAndHoldSeats(int numSeats, Optional<Integer> minLevel,
                                    Optional<Integer> maxLevel, String customerEmail);
    /**
     * Commit seats held for a specific customer
     *
     * @param seatsToReserve list of seats to reserve
     * @param customerEmail the email address of the customer to which the seat hold
    is assigned
     * @return a reservation confirmation message
     */
    String reserveSeats(List<SeatPojo> seatsToReserve, String customerEmail);
}