package com.walmart.homework.services;

import com.walmart.homework.Constants;
import com.walmart.homework.pojo.SeatPojo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by E on 4/17/16.
 * InMemoryTicketService which implements TicketService.  Data/state is not stored anywhere
 */
public class InMemoryTicketService implements TicketService {

    private static final Integer tiers[] = {25,20,15,15}; // number of rows in each tier
    private static HashMap<Integer, SeatPojo> seatMapper = new HashMap<>();
    private static final Integer rows[] = {50,100,100,100}; // number of seats in each row
    private static List<SeatPojo> seatList = new ArrayList<>(); // hold the full list of seats

    // get # of tiers in venue
    public Integer getTierCount(){ return tiers.length; }

    // get the full seat list
    public List<SeatPojo> getSeatList(){ return seatList; }

    // get the seat mapper
    public HashMap<Integer, SeatPojo> getSeatMapper() { return seatMapper; }

    public InMemoryTicketService(){
        // populate seat mapper, so when i build the seat list, i know the tier name and price to store in the SeatPojo
        seatMapper.put(1, new SeatPojo("Orchestra",100.00));
        seatMapper.put(2, new SeatPojo("Main",75.00));
        seatMapper.put(3, new SeatPojo("Balcony 1",50.00));
        seatMapper.put(4, new SeatPojo("Balcony 2",40.00));

        Integer seatCounter = 0;

        // loop through the tiers, rows, and seats to create the full concert hall seat list
        for(int i = 0; i < tiers.length; i++) {
            int tierLevel = i + 1; // don't use 0 in tier level
            for(int j = 0; j < tiers[i]; j++) {
                for(int k = 0; k < rows[i]; k++) {
                    seatCounter++; // increment seat counter
                    SeatPojo seat = new SeatPojo();// create a seat

                    // set necessary values for each seat
                    seat.setSeatReserved(false); // seat starts out available
                    seat.setTierName(seatMapper.get(tierLevel).getTierName()); // set tier name using hashmap
                    seat.setPrice(seatMapper.get(tierLevel).getPrice()); // set price using hashmap
                    seat.setTierLevel(tierLevel); // set the tier level
                    seat.setRow(j + 1); // set the row number, don't use 0
                    seat.setSeatNum(seatCounter); // set arbitrary seat number from counter
                    seat.setHoldTimestamp(new Date(1));

                    seatList.add(seat); // add seat to the seatList array
                }
            }
        }
    }

    // check for available seating
    public static int numSeatsAvailable(Optional<Integer> venueLevel) {
        return seatList.stream()
                .filter(seat -> seat.getTierLevel().equals(venueLevel.orElse(seat.getTierLevel())))
                .filter(SeatPojo::seatReserved)
                .collect(Collectors.toList()).size();
    }

    // find best available seats given the search parameters and hold them
    public List<SeatPojo> findAndHoldSeats(int numSeats, Optional<Integer> minLevel,
                                           Optional<Integer> maxLevel, String customerEmail) {
        Integer seatHoldId = 0;
        // if user entered '0' to search all tiers, empty the optional so the filters aren't tier specific
        Optional minTierLevel = minLevel.get().equals(0) ? Optional.empty() : minLevel;
        Optional maxTierLevel = maxLevel.get().equals(0) ? Optional.empty() : maxLevel;
        List<SeatPojo> holdSeats = new ArrayList<>();

        // build a list of available seats, checking for reservation status and tier level
        List<SeatPojo> availableSeats = seatList.stream()
                .filter(seat -> seat.getTierLevel() >= Integer.parseInt(minTierLevel.orElse(seat.getTierLevel()).toString()))
                .filter(seat -> seat.getTierLevel() <= Integer.parseInt(maxTierLevel.orElse(seat.getTierLevel()).toString()))
                .filter(SeatPojo::seatReserved)
                .collect(Collectors.toList());

        // place desired number of seats on hold
        for(int i=0; i < numSeats; i++){
            if(i==0){ seatHoldId = availableSeats.get(i).getSeatNum()-1; }
            seatList.get(availableSeats.get(i).getSeatNum()-1).setCustomerEmail(customerEmail);
            seatList.get(availableSeats.get(i).getSeatNum()-1).setHoldTimestamp(new Date());
            seatList.get(availableSeats.get(i).getSeatNum()-1).setSeatHoldId(seatHoldId);
            holdSeats.add(availableSeats.get(i));
        }
        return holdSeats;
    }

    // reserve the seats
    public String reserveSeats(List<SeatPojo> seatsToReserve, String customerEmail){
        String response;
        // if more than [x] time (set in Constants.java) has elapsed, don't place the reservation
        if(new Date().getTime() - Constants.RESERVATIONTIMEOUT > seatsToReserve.get(0).getHoldTimestamp().getTime()) {
            response = "\nMore than " + Constants.TIMEOUTSTRING + " has passed and your hold has expired, please try again\n";
        } else {
            // reserve the seats
            for (SeatPojo aSeatsToReserve : seatsToReserve) {
                seatList.get(aSeatsToReserve.getSeatNum() - 1).setSeatReserved(true);
            }
            // successful reservation -  sequence/confirmation number for would be generated by the database
            response = "\nThank you for your purchase. Your reservation confirmation number is #WIN"
                    + " and a confirmation e-mail has been sent to " + customerEmail + "\n";
        }

        seatsToReserve.clear();

        return response;

    }

}