package org.example.pages.paymentPages.records;

public record BookingDetails(
        String hotelName,
        String city,
        String checkInDate,
        String checkOutDate,
        String guestsAndNightsInfo) {
}