package service;
import exception.ParkingLotException;
import model.Car;
import observer.IObserver;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLot {

    private List<IObserver> observers;
    Car car = new Car();
    int capacity;
    int carParked =0;
    Map<Integer, SlotDetails> carsParkingDetails;
    SlotDetails slotDetails;
    public ParkingLot(int capacity){
        this.observers = new ArrayList<>();
        this.capacity = capacity;
        slotDetails = new SlotDetails();
        carsParkingDetails = new LinkedHashMap<>();
        IntStream.rangeClosed(1, (capacity)).forEach(slotNumber -> carsParkingDetails.put(slotNumber, slotDetails));
    }
    public void initialiseMap(int capacity){
        for(int i =1; i<=capacity; i++ ){
            mapSlot.put(i," ");
        }
    }
    // first string is for give position as string type, Second is for Registration of car
    Map<String, String> parkingPositionMap;
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
        if( mapSlot.size() == capacity){
            for (IObserver observer: observers){
                observer.parkingLotFull(true);
            }throw new ParkingLotException("Parking Full",
                    ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        if(mapSlot.containsValue(registration))
            throw  new ParkingLotException("Already parked", ParkingLotException.ExceptionType.WRONG_DETAILS);
        if( mapSlot.size() < capacity) {

            mapSlot.put(position,registration);
        }


    }
    public void vehiclePresentPosition(String registration) throws ParkingLotException {
        this.parkWithDetails(position,registration);
        if( parking.contains(registration)){
            this.getKey(mapSlot, registration);
        }
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

    public int getSlotPositionToPark(int capacity) {
        this.initialiseMap(capacity);
        return getKey(mapSlot," ");
    }

    public LocalTime getParkingTime( String registration) throws ParkingLotException {
        this.vehiclePresentPosition(registration);
        return LocalTime.now().withNano(0);
    }

    public boolean unParkCar(String registration) throws ParkingLotException {
        if (mapSlot.containsKey(registration)) {
            mapSlot.put(position," ");
            for (IObserver observer: observers){
                observer.parkingLotAvailable(true);
            }
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    /// for uc 9 onwards

    public void parkVehicle(Car registration) throws  ParkingLotException{
        if(registration == null)
            throw  new ParkingLotException("Null entered", ParkingLotException.ExceptionType.WRONG_DETAILS);
        if (carsParkingDetails.containsValue(registration))
            throw new ParkingLotException("Already Preset",ParkingLotException.ExceptionType.ALREADY_PARKED);
        if (carParked > capacity)
            throw new ParkingLotException("Parking Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        carsParkingDetails.put(getPosition(),new SlotDetails(getPosition(), registration,LocalTime.now().withNano(0)));
        carParked++;
        if (carParked == capacity){
            observers.forEach(observer -> observer.parkingLotAvailable(true));
        }
    }
    public int getPosition() {
        return this.carsParkingDetails.keySet()
                .stream().filter(slotDetails -> carsParkingDetails.get(slotDetails).getCar()==null)
                .findFirst().orElse(0);
    }

    public int vehicleLocation(Car registration){
        return this.getSlotDetails(registration).getSlotNu();
    }

    public SlotDetails getSlotDetails(Car registration){
        return this.carsParkingDetails.values().stream().filter(slot -> registration.equals(slot.car))
                .findFirst().get();

    }

    public boolean isVehiclePresent(Car registration) {
        return carsParkingDetails.values().stream()
                .anyMatch(slotDetails -> slotDetails.getCar() == (registration));
    }
    public int getNumberOfParkedCars() {
        return carParked;
    }


    public List<Integer> findOnColor(String color){
        List<Integer> whiteColorList = new ArrayList<>();
         whiteColorList = this.carsParkingDetails.values().stream()
                .filter(slotDetails1 -> slotDetails1.getCar() != null)
                .filter(slotDetails2 -> slotDetails2.getCar().getColor().equals(color))
                .map(slotDetails1 -> slotDetails1.getSlotNu()).collect(Collectors.toList());
        return whiteColorList;
    }



}