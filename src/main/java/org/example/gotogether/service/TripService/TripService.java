package org.example.gotogether.service.TripService;



import org.example.gotogether.dto.request.CreateTripRequestDto;
import org.example.gotogether.dto.response.TripResponseDto;

import java.util.List;

public interface TripService {
    TripResponseDto createTrip(CreateTripRequestDto request);
    TripResponseDto updateTrip(CreateTripRequestDto request , Long id);
    List<TripResponseDto> allTrips();
    TripResponseDto findTripById(Long id);
    void delete(Long id);
}
