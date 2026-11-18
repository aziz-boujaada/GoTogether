package org.example.gotogether.service.VehicleService;

import org.example.gotogether.dto.request.CreateVehicleRequest;
import org.example.gotogether.dto.response.VehicleResponseDto;
import org.example.gotogether.entity.Driver;
import org.example.gotogether.entity.User;
import org.example.gotogether.entity.Vehicle;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.example.gotogether.mapper.VehicleMapper;
import org.example.gotogether.repository.UserRepository;
import org.example.gotogether.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService{


    private final VehicleRepository vehicleRepository ;
    private final VehicleMapper vehicleMapper ;
    private final UserRepository userRepository;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository ,
            VehicleMapper vehicleMapper,
            UserRepository userRepository
    ){
        this.vehicleRepository = vehicleRepository ;
        this.vehicleMapper = vehicleMapper ;
        this.userRepository = userRepository;
    }

    @Override
    public VehicleResponseDto createVehicle(CreateVehicleRequest request) {
        Driver driver = getDriver(request.driverId());
        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setDriver(driver);
        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponseDto updateVehicle(CreateVehicleRequest request, Long id) {

         Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not fount"));;

        vehicleMapper.updateVehicle(request , vehicle);
        return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public List<VehicleResponseDto> allVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    public VehicleResponseDto findVehicleById(Long id) {
        return getVehicle(id);
    }

    @Override
    public void delete(Long id) {
       Vehicle vehicle = vehicleRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Vehicle not fount"));
       vehicleRepository.delete(vehicle);
    }

    private VehicleResponseDto getVehicle(Long id){
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toResponse)
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle not found"));
    }

    private Driver getDriver(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if(!(user instanceof Driver driver)){
            throw new InvalidValueException("User must be a driver");
        }
        return driver;
    }
}
