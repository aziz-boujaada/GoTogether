package org.example.gotogether.mapper;

import org.example.gotogether.dto.request.CreateVehicleRequest;
import org.example.gotogether.dto.response.VehicleResponseDto;
import org.example.gotogether.entity.Vehicle;

import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class}
)
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    Vehicle toEntity(CreateVehicleRequest request);

    @Mapping(target = "driver", source = "driver")
    VehicleResponseDto toResponse(Vehicle vehicle);

    List<VehicleResponseDto> toResponseList(List<Vehicle> vehicles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    void updateVehicle(
            CreateVehicleRequest request,
            @MappingTarget Vehicle vehicle
    );
}