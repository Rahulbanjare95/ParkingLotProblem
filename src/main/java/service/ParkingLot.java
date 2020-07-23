package service;

import enums.ParkingLotControllers;
import exception.ParkingLotException;
import model.Car;

import java.time.LocalTime;
import java.util.*;

public class ParkingLot implements IParkingLot {

    Car car = new Car();
    private ParkingLotControllers parkingLotControllers;
    Owner owner = new Owner();

    Map<String, Car> carMap = new HashMap<>();
    HashMap<Integer,Car> mapSlot=new HashMap<Integer,Car>();

    public boolean park(Car car) {
        return true;
    }

    String registration;
    String model;
    Integer position;
    int capacity = 3;
    List<Integer> slot = new ArrayList<>();
    List<Double> parkingTime = new ArrayList<>();
    public int parkWithDetails(String registration) {
        carMap.put(registration, new Car(registration));

        return carMap.size();
    }

    public boolean unParkCar(String registration)  {
        if (carMap.containsKey(registration)) {
            carMap.remove(registration);
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    public void getParkingAllotmentDetails() throws ParkingLotException {
        int numberOfCars = this.parkWithDetails(registration);

        if (numberOfCars <= capacity) {
            ParkingLotControllers.PARKING_LOT_OWNER.isParkingLotFull = false;
            ParkingLotControllers.AIRPORT_SECURITY.isParkingLotFull = false;
            return;
        }
        throw new ParkingLotException("Parking Lot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }

    public void parkWithSlot(Integer position, String registration) {
            mapSlot.put(position, new Car(registration));
            slot.add(position);
        }

    public void parkCarWithTiming(Integer position, String registration, double timing){
            mapSlot.put(position, new Car(registration));
            parkingTime.add(timing);

        }

        @Override
        public boolean isParkedAt(Integer position) {
            this.parkWithSlot(position, registration);
            if(position > slot.size())
                throw new ArrayIndexOutOfBoundsException();
            if (position.equals(slot.get(position))) {
                return true;
            }
            throw new ParkingLotException("No such position", ParkingLotException.ExceptionType.WRONG_DETAILS);
        }


    public LocalTime getParkingTime(int position, String registration) {
            this.parkWithSlot(position, registration);
            LocalTime parkedTime = LocalTime.now().withNano(0);
            return parkedTime;
    }
}