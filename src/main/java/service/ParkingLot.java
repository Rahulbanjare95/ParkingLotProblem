package service;

import enums.ParkingLotControllers;
import exception.ParkingLotException;
import model.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot<capacity> {

    Car car = new Car();
    private ParkingLotControllers parkingLotControllers;

    Map<String, Car> carMap = new HashMap<>();
    HashMap<Integer,Car> mapSlot=new HashMap<Integer,Car>();

    public boolean park(Car car) {
        return true;
    }

    String registration;
    String model;
    int position;
    int capacity = 3;
    List<Integer> slot = new ArrayList<>();
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
        if (numberOfCars <= capacity) {
            this.parkingLotControllers.PARKING_LOT_OWNER.isParkingLotFull = false;
            this.parkingLotControllers.AIRPORT_SECURITY.isParkingLotFull = false;
            return numberOfCars;
        }
        throw new ParkingLotException("Parking Lot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }

        public HashMap<Integer, Car> parkWithSlot(Integer position, String registration, String model) {
            mapSlot.put(position, new Car(registration,model));
            slot.add(position);
            return mapSlot;
        }

        public boolean isParkedAt(Integer position) throws ParkingLotException {
                this.parkWithSlot(position, registration, model);

                if(position.equals(slot.get(position))){
                    return  true;
                }
                throw new ParkingLotException("Car not at provided position", ParkingLotException.ExceptionType.WRONG_DETAILS);

        }

}