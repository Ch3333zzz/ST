package org.example.pages.searchPages.records;

public record HotelResult(
        String name,
        AccommodationType type,
        String location,
        int nights,
        int guests,
        boolean hasFreeCancellation,
        boolean isBreakfastIncluded,
        int starts,
        int price) {
}
