
package org.example.gotogether.mapper;

import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.entity.Reservation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "departurePoint", ignore = true)
    @Mapping(target = "arrivalPoint", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "reservationDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Reservation toEntity(CreateReservationRequestDto request);

    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "departurePointId", source = "departurePoint.id")
    @Mapping(target = "arrivalPointId", source = "arrivalPoint.id")
    ReservationResponseDto toResponseDto(Reservation reservation);

    List<ReservationResponseDto> toResponseDtoList(
            List<Reservation> reservations
    );
}

