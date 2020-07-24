package service;
import exception.ParkingLotException;
import model.Car;
import observer.IObserver;

import java.time.LocalTime;
import java.util.*;

public class ParkingLot {

    private List<IObserver> observers;
    Car car = new Car();
    int capacity;
    public ParkingLot(int capacity){
        this.observers = new ArrayList<>();
        this.capacity = capacity;

    }
    public void initialiseMap(int capacity){
        for(int i =1; i<=capacity; i++ ){
            mapSlot.put(i," ");
        }
    }

    HashMap<Integer,String> mapSlot=new HashMap<Integer, String>();
    public void registerObserver(IObserver observer) {
        this.observers.add(observer);
    }

    public boolean park(Car car) {
        return true;
    }

    String registration;
    Integer position;
    List<Map<String, Car>> parking = new ArrayList<>();


    public void parkWithDetails(Integer position ,String registration) throws  ParkingLotException {
        if(mapSlot.containsValue(registration))
            throw  new ParkingLotException("Already parked", ParkingLotException.ExceptionType.WRONG_DETAILS);
        if( mapSlot.size() > capacity){
            for (IObserver observer: observers){
                observer.parkingLotFull(true);
            }throw new ParkingLotException("Parking Full",
                    ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        if( mapSlot.size() < capacity) {

            mapSlot.put(position,registration);
        }

    }
    public int vehiclePresentPosition(String registration) throws ParkingLotException {
        this.parkWithDetails(position,registration);
        if( parking.contains(registration)){
            return this.getKey(mapSlot,registration);
        }
        else return 0;

    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public int getPositionOfVehicle(String registration){
        return getKey(mapSlot, registration);
    }

    public int getSlotPositionToPark() throws ParkingLotException {
        this.initialiseMap(capacity);
        return getKey(mapSlot," ");
    }

    public LocalTime getParkingTime( String registration) throws ParkingLotException {
        this.vehiclePresentPosition(registration);
        return LocalTime.now().withNano(0);

    }

    public boolean unParkCar(String registration) throws ParkingLotException {
        if (mapSlot.containsKey(registration)) {
            mapSlot.remove(registration);
            for (IObserver observer: observers){
                observer.parkingLotAvailable(true);
            }
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    public int getVacantPosition() throws ParkingLotException {
        this.parkWithDetails(position,registration);
        int size = mapSlot.size();

        if (size < capacity) {
            return (capacity - size);
        }
        throw new ParkingLotException("Parking Lot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }

}