package org.example.gotogether.service.VehicleService;


import org.example.gotogether.dto.request.CreateVehicleRequest;
import org.example.gotogether.dto.response.VehicleResponseDto;
import org.example.gotogether.entity.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VehicleService {
    VehicleResponseDto createVehicle(CreateVehicleRequest request);
    VehicleResponseDto updateVehicle(CreateVehicleRequest request , Long id);
    List<VehicleResponseDto> allVehicles();
    VehicleResponseDto findVehicleById(Long id);
    void delete(Long id);
}
