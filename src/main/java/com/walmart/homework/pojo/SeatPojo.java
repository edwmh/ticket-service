package com.walmart.homework.pojo;

import com.walmart.homework.Constants;
import java.util.Date;

/**
 * Created by E on 4/17/2016.
 * SeatPojo - Define seat object and methods
 */
public class SeatPojo {
    private Integer tierLevel;
    private String tierName;
    private Boolean seatReserved;
    private Date holdTimestamp;
    private Integer row;
    private Integer seatNum;
    private Double price;
    private String customerEmail;
    private Integer seatHoldId;

    // no values
    public SeatPojo(){}

    // constructor for the hashmap i will use when creating a List of SeatPojo containing every possible seat
    public SeatPojo(String tierName,Double price) {
        this.tierName = tierName;
        this.price = price;
    }

    // getters
    public Date getHoldTimestamp() { return holdTimestamp; }
    public Integer getTierLevel() { return tierLevel; }
    public String getTierName() { return tierName; }
    public Boolean getSeatReserved() { return seatReserved; }
    public Integer getRow() { return row; }
    public Integer getSeatNum() { return seatNum; }
    public Double getPrice() { return price; }
    public String getCustomerEmail() { return customerEmail; }
    public Integer getSeatHoldId() { return seatHoldId; }

    // setters
    public void setHoldTimestamp(Date holdTimestamp) { this.holdTimestamp = holdTimestamp; }
    public void setTierLevel(Integer tierLevel) { this.tierLevel = tierLevel; }
    public void setTierName(String tierName) { this.tierName = tierName; }
    public void setSeatReserved(Boolean seatReserved) { this.seatReserved = seatReserved; }
    public void setRow(Integer row) { this.row = row; }
    public void setSeatNum(Integer seatNum) { this.seatNum = seatNum; }
    public void setPrice(Double price) { this.price = price; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setSeatHoldId(Integer seatHoldId) { this.seatHoldId = seatHoldId; }

    /**
     * checks if a seat is reserved or has a hold on it
     * @return  Boolean
     */
    public Boolean seatReserved(){
        return !this.seatReserved && ((new Date().getTime() - Constants.RESERVATIONTIMEOUT) > this.holdTimestamp.getTime());
    }

}
