package com.walmart.homework;

import com.walmart.homework.pojo.SeatPojo;
import com.walmart.homework.services.InMemoryTicketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by E on 4/17/2016.
 * Main class for the WISARD application
 */

public class TicketApplication{


    public static void main(String[] args) {

        List<SeatPojo> holdSeatList = new ArrayList<>();
        Boolean keepRunning = true;
        Optional inputSeats;
        String inputTierMin;
        String inputTierMax = "";
        String userEmail = "";

        // welcome future overlords to my application
        System.out.println("\nWelcome to WISARD - Walmart Interview Seating and Reservation Details\n");

        // initialize ticket service
        InMemoryTicketService ticketService = new InMemoryTicketService();

        // keep the application running until user exits
        while(keepRunning) {
            // print out overall seats available and seat count by section. show price to be more user friendly
            System.out.println("\nThere are " + ticketService.numSeatsAvailable(Optional.empty()) + " overall seats left");
            for (int i = 0; i < ticketService.getTierCount(); i++) {
                int tierLevel = i + 1; // don't use 0 in tier level
                System.out.println("There are " + ticketService.numSeatsAvailable(Optional.of(tierLevel))
                    + " seats left in the " + ticketService.getSeatMapper().get(tierLevel).getTierName()
                    + " (tier " + tierLevel + ") section ($"
                    + ticketService.getSeatMapper().get(tierLevel).getPrice() + " per ticket)");
            }

            System.out.println("\nEnter 'q' at any time to exit\n");

            // get # of tickets the user would like to hold
            try {
                System.out.println("Enter the number of tickets you would like to reserve:");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                inputSeats = Optional.of(userInput.readLine());
            } catch (IOException e) {
                System.out.println("error reading user input, defaulting your number of tickets to '1'");
                inputSeats = Optional.of(1);
                // e.printStackTrace(); // dev output and debugging
            }

            // exit if user types 'q'
            if(inputSeats.get().toString().toLowerCase().equals("q")) { keepRunning = false; break; }

            // get the tier(s) the user would like to find seats in
            try {
                System.out.println("Enter the minimum tier (1-4) you would like to search, or '0' to pick best available:");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                inputTierMin = userInput.readLine();
                if(!inputTierMin.equals("0")){
                    System.out.println("Enter the maximum tier (1-4) you would like to search:");
                    inputTierMax = userInput.readLine();
                }
            } catch (IOException e) {
                System.out.println("error reading user input, defaulting to all tiers");
                inputTierMin = "0";
                // e.printStackTrace(); // dev output and debugging
            }

            // exit if the user types 'q'
            if(inputTierMin.toLowerCase().equals("q") || inputTierMax.toLowerCase().equals("q"))
            {
                keepRunning = false; break;
            }

            // get user e-mail address and place the seats on hold
            try {
                System.out.println("Enter your e-mail address");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                userEmail = userInput.readLine();

                // actually find the seats to hold
                holdSeatList = ticketService.findAndHoldSeats(Integer.parseInt(inputSeats.get().toString()),
                        Optional.of(Integer.parseInt(inputTierMin)),
                        Optional.of(Integer.parseInt(inputTierMax)), userEmail);
            } catch (Exception e) {
                // e.printStackTrace(); // dev output and debugging
            } finally {
                // report what seats were held for the user
                if(holdSeatList.size() > 0) {
                    System.out.println("\nYou have successfully put the following seats on hold:");
                    for (SeatPojo seat : holdSeatList) {
                        System.out.println(seat.getTierName() + " level, row " + seat.getRow() + ", seat " + seat.getSeatNum());
                    }
                } else {
                    System.out.println("There was an error while trying to place your seats on hold, please try again\n");
                }
            }

            // exit if the user types 'q'
            if(userEmail.toLowerCase().equals("q")) { keepRunning = false; break; }


            // don't bother trying to reserve seats if the hold list is empty for whatever reason
            if(holdSeatList.size() > 0) {
                try {
                    System.out.println("\nYour seats are on hold for " + Constants.TIMEOUTSTRING
                            + ". Please enter 'confirm' to place your reservation."
                            + "\nEnter anything else to cancel your hold and start over");
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                    String userConfirmation = userInput.readLine();

                    // if user confirms, place reservation
                    if (userConfirmation.toLowerCase().equals("confirm")) {
                        System.out.println(ticketService.reserveSeats(holdSeatList, userEmail));
                        // exit if the user types 'q'
                    } else if (userConfirmation.toLowerCase().equals("q")) {
                        keepRunning = false;
                        break;
                        // if they typed anything else, clear their hold and go start over
                    } else {
                        // reset the hold timestamp so the seats do not show as reserved
                        for (SeatPojo aHoldSeatList : holdSeatList) {
                            ticketService.getSeatList().get(aHoldSeatList.getSeatNum() - 1).setHoldTimestamp(new Date(1));
                        }

                        // clear hold list
                        holdSeatList.clear();
                    }
                } catch (Exception e) {
                    System.out.println("There was an error while trying to place your reservation, please try again\n");
                }

                /**
                 * report on all the seats currently reserved or on hold, grouped by e-mail address, just for fun.
                 * incongruous to what the actual user experience would be (admin functionality)
                 */
                // create a hash map to group the seat reservations together
                HashMap<String, List<Integer>> seatsGroupedByEmail = new HashMap<>();

                // populate hash map, grouping seat reservations by user email
                ticketService.getSeatList().stream()
                        .filter(s -> !s.seatReserved()) // find reserved seats
                        .collect(Collectors.toList()) // back in to a list
                        .forEach(seat -> { // iterate over the filtered list
                            String key = seat.getCustomerEmail();
                            seatsGroupedByEmail.putIfAbsent(key, new ArrayList<>());
                            seatsGroupedByEmail.get(key).add(seat.getSeatNum());
                        });

                // print out report of seats reserved grouped by e mail
                System.out.println("List of seats reserved/held by e-mail address:");
                seatsGroupedByEmail.forEach((k,v)-> System.out.println("* Email: " + k + ", Seats: " + v));

            }
        }
    }
}
