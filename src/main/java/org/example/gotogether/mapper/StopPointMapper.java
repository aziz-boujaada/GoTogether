package org.example.gotogether.mapper;

import org.example.gotogether.dto.request.CreateStopPointsRequest;
import org.example.gotogether.dto.response.StopPointsResponse;
import org.example.gotogether.entity.StopPoint;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StopPointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "stopOrder", ignore = true)
    @Mapping(target = "departureReservations", ignore = true)
    @Mapping(target = "arrivalReservations", ignore = true)
    StopPoint toEntity(CreateStopPointsRequest request);

    StopPointsResponse toResponseDto(StopPoint stopPoint);
}
