package service;

import enums.ParkingLotControllers;
import exception.ParkingLotException;
import model.Car;

import java.util.*;

public class ParkingLotSystem implements IParkingLot {

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
    public int parkWithDetails(String registration, String model) {
        carMap.put(registration, new Car(registration, model));

        return carMap.size();
    }

    public boolean unParkCar(String registration)  {
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

        public void initializeMap(Integer position, String registration, String model){

            for (int i = 1; i <= capacity ; i++) {
                mapSlot.put(i,null);
            }
        
        }

        public void parkWithSlot(Integer position, String registration, String model) {
            mapSlot.put(position, new Car(registration,model));
            slot.add(position);
        }
        public void initaliseparkingtime(Integer position){
            for(int i =0 ; i<= mapSlot.size(); i++ )
            {
                parkingTime.add(i,null);
            }
    }
        public void parkCarWithTiming(Integer position, String registration, double timing){
            mapSlot.put(position, new Car(registration));

           parkingTime.add(position,timing);
        }

        double timing;
        public boolean getTime(Integer position){
            this.parkCarWithTiming(position, registration, timing);
            return parkingTime.contains(timing);
        }

        @Override
        public boolean isParkedAt(Integer position) {
            this.parkWithSlot(position, registration, model);
            if(position > slot.size())
                throw new ArrayIndexOutOfBoundsException();
            if (position.equals(slot.get(position))) {
                return true;
            }
            throw new ParkingLotException("No such position", ParkingLotException.ExceptionType.WRONG_DETAILS);
        }

}