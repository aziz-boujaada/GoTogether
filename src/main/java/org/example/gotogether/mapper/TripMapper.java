package org.example.gotogether.mapper;

import org.example.gotogether.dto.request.CreateTripRequestDto;
import org.example.gotogether.dto.response.TripResponseDto;
import org.example.gotogether.entity.Trip;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "stopPoints", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Trip toEntity(CreateTripRequestDto dto);

    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "stopPoints", ignore = true)
    TripResponseDto toResponseDto(Trip trip);

    List<TripResponseDto> toResponseDtoList(List<Trip> trips);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "stopPoints", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    void updateEntity(
            CreateTripRequestDto dto,
            @MappingTarget Trip trip
    );
}
