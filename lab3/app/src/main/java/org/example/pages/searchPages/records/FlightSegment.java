package org.example.pages.searchPages.records;

public record FlightSegment(
                Location departure,
                Location arrival,
                String departureTime,
                String arrivalTime,
                boolean isDirect) {
}