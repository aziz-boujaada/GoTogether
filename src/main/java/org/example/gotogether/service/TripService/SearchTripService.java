package org.example.gotogether.service.TripService;

import lombok.RequiredArgsConstructor;
import org.example.gotogether.dto.request.SearchTripRequest;
import org.example.gotogether.dto.response.TripResponseDto;
import org.example.gotogether.enums.TripStatus;
import org.example.gotogether.mapper.TripMapper;
import org.example.gotogether.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchTripService {
   private final TripRepository tripRepository;
   private final TripMapper tripMapper ;


   public List<TripResponseDto> searchTrips(SearchTripRequest searchRequest){


       LocalDateTime startDate = searchRequest.departureDate() != null
               ? searchRequest.departureDate()
               : null ;



       return tripRepository.searchForTrips(
               searchRequest.departureCity(),
               searchRequest.arrivalCity(),
               startDate,
               searchRequest.requiredSeats(),
               searchRequest.maxPrice(),
               TripStatus.PUBLIE
       ).stream().map(tripMapper::toResponseDto).toList();

   }

}
