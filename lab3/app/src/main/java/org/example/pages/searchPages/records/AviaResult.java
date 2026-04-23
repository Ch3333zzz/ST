package org.example.pages.searchPages.records;

import java.util.List;

public record AviaResult(
        List<FlightSegment> segments,
        int passengers,
        boolean withBaggage,
        boolean isBookable,
        List<String> airlines,
        int price) {
}