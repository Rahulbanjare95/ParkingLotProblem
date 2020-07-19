package service;

import exception.ParkingLotException;
import model.Car;

import java.util.HashMap;
import java.util.Map;

public class ParkingLot {

    Car car = new Car();
    private ParkingLotControllers parkingLotControllers;

    Map<String, Car> carMap = new HashMap<>();

    public boolean park(Car car) {
        return true;
    }

    String registration;
    String model;
    int capacity = 3;

    public int parkWithDetails(String registration, String model) {
        carMap.put(registration, new Car(registration, model));
        return carMap.size();
    }

    public boolean unParkCar(String registration) throws ParkingLotException {
        if (carMap.containsKey(registration)) {
            carMap.remove(registration);
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    public int getParkingAllotmentDetails() throws ParkingLotException {
        int numberOfCars = this.parkWithDetails(registration, model);
        if (numberOfCars < capacity) {
            this.parkingLotControllers.PARKING_LOT_OWNER.isParkingLotFull = false;
            this.parkingLotControllers.AIRPORT_SECURITY.isParkingLotFull = false;
            return numberOfCars;
        }
        throw new ParkingLotException("Parking Lot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }
}