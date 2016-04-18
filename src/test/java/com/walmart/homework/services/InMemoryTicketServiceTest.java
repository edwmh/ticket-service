package com.walmart.homework.services;

import com.walmart.homework.pojo.SeatPojo;
import junit.framework.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by E on 4/18/16.
 * unit tests for some of the main functionality of InMemoryTicketService
 */
public class InMemoryTicketServiceTest extends TestCase {

    /**
     *  test finding seats.
     *  pass in no tier should return all 6250 seats.
     *  pass in tier 1 should return 1250 seats
     */
    public void testNumSeatsAvailable(){
        InMemoryTicketService ticketService = new InMemoryTicketService();
        assertEquals(ticketService.numSeatsAvailable(Optional.empty()), 6250);
        assertEquals(ticketService.numSeatsAvailable(Optional.of(1)), 1250);
    }

    /**
     * test finding and holding available seats.
     * pass in searching for 3 seats on any tier should return a list of seats
     * after we confirm 3 seats are held, try to reserve them
     * then confirm in the full seat list, there are 3 seats reserved
     */
    public void testFindAndReserveSeats(){
        InMemoryTicketService ticketService = new InMemoryTicketService();
        List<SeatPojo> reservedSeatList = ticketService.findAndHoldSeats(3, Optional.of(0), Optional.of(0), "test@test.com");
        assertEquals(reservedSeatList.size(), 3);
        String response = ticketService.reserveSeats(reservedSeatList,"test@test.com");
        assertEquals(ticketService.getSeatList()
                .stream()
                .filter(s -> !s.seatReserved())
                .collect(Collectors.toList())
                .size(), 3);
    }

}
