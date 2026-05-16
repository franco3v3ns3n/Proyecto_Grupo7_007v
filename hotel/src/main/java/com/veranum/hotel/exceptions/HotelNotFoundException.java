package com.veranum.hotel.exceptions;

public class HotelNotFoundException extends RuntimeException {

    public HotelNotFoundException(String message) {
        super(message);
    }
}