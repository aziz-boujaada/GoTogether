package org.example.gotogether.service.TripService;

import jakarta.transaction.Transactional;
import org.example.gotogether.dto.request.CreateTripRequestDto;
import org.example.gotogether.dto.response.TripResponseDto;
import org.example.gotogether.entity.Driver;
import org.example.gotogether.entity.StopPoint;
import org.example.gotogether.entity.Trip;
import org.example.gotogether.entity.Vehicle;
import org.example.gotogether.enums.TripStatus;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceAlreadyExistException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.example.gotogether.mapper.StopPointMapper;
import org.example.gotogether.mapper.TripMapper;
import org.example.gotogether.repository.TripRepository;
import org.example.gotogether.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TripServiceImpl implements TripService{

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final StopPointMapper stopPointMapper;
    private final VehicleRepository vehicleRepository ;
    public TripServiceImpl(
            TripRepository tripRepository,
            TripMapper tripMapper,
            VehicleRepository vehicleRepository,
            StopPointMapper stopPointMapper
    ){
     this.tripRepository = tripRepository ;
     this.tripMapper = tripMapper;
     this.vehicleRepository = vehicleRepository;
     this.stopPointMapper = stopPointMapper;
    }

    @Override
    @Transactional
    public TripResponseDto createTrip(CreateTripRequestDto request) {

        validDuplicatePublication(request);
        Vehicle vehicle = getVehicle(request.vehicleId());
        Driver driver = vehicle.getDriver();
        validateSeats(request.proposedSeats(), vehicle.getCapacite());
        Trip trip = tripMapper.toEntity(request);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setStatus(TripStatus.PUBLIE);


        List<StopPoint> stopPoints = request.pointsRequests()
                .stream()
                .map(stopPointMapper::toEntity)
                .toList();

        initializeStopPoints(stopPoints , trip);
        trip.getStopPoints().addAll(stopPoints);

        return tripMapper.toResponseDto(tripRepository.save(trip));
    }

    @Override
    public TripResponseDto updateTrip(CreateTripRequestDto request, Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        Vehicle vehicle = getVehicle(request.vehicleId());
        validateSeats(request.proposedSeats(), vehicle.getCapacite() );

        tripMapper.updateEntity(request , trip);
        trip.setVehicle(vehicle);
        trip.setDriver(vehicle.getDriver());

        return tripMapper.toResponseDto(tripRepository.save(trip));
    }

    @Override
    public List<TripResponseDto> allTrips() {
        return tripRepository.findAll()
                .stream()
                .map(tripMapper::toResponseDto)
                .toList();
    }

    @Override
    public TripResponseDto findTripById(Long id) {
        return tripRepository.findById(id)
                .map(tripMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }

    @Override
    public void delete(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripRepository.delete(trip);

    }

    private Vehicle getVehicle(Long id){
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    private void validateSeats(Integer proposedSeats , Integer capacity ){
        if(proposedSeats == null || proposedSeats < 1){
            throw new InvalidValueException("Proposed stats must be greater then 0");
        }
        if(proposedSeats > capacity){
            throw new InvalidValueException("Proposed stats must less or equals to vehicle capacity");
        }
    }

    private void initializeStopPoints(List<StopPoint>stopPoints , Trip trip){
        LocalDateTime previousTime = null ;
        for(int i = 0 ; i < stopPoints.size() ; i++){
            StopPoint stopPoint = stopPoints.get(i);

            if(
                    previousTime != null &&
                    !stopPoint.getPassageTime().isAfter(previousTime)){
                 throw  new InvalidValueException("stop points must be in chronology order");
            }

            if(stopPoint.getPassageTime().isBefore(trip.getDepartureDate())){
                throw new InvalidValueException("The passage time must be after the Departure Date of trip");
            }
            stopPoint.setStopOrder(i);
            stopPoint.setTrip(trip);
            previousTime = stopPoint.getPassageTime();
        }
    }
    private void validDuplicatePublication(CreateTripRequestDto requestDto){
       LocalDateTime departure = requestDto.departureDate();
       boolean exists = tripRepository.existsByVehicleIdAndDepartureDateBetween(
               requestDto.vehicleId(),
               requestDto.departureDate().minusHours(24),
               requestDto.departureDate().plusHours(24)
       );
       if(exists){
           throw new ResourceAlreadyExistException("A trip with this vehicle already exists within 24 hours");
       }
    }
}
