package org.example.pages.searchPages.records;

public record BusResult(
                Location departure,
                Location arrival,
                String departureTime,
                String arrivalTime,
                String busName,
                int passengers) {
}