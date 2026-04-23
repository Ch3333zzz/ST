package org.example.pages.searchPages.records;

import java.util.List;

public record RailwayResult(
                Location departure,
                Location arrival,
                String departureTime,
                String arrivalTime,
                String trainName,
                List<String> wagonTypes,
                int passengers,
                int price) {
}
