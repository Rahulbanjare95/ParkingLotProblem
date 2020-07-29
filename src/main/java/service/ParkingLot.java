package service;

import enums.CarSize;
import enums.DriverCategory;
import exception.ParkingLotException;
import model.Car;
import model.SlotDetails;
import observer.IObserver;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ParkingLot {

    private List<IObserver> observers;
    Car car = new Car();
    int capacity;
    int carParked = 0;
    Map<Integer, SlotDetails> carsParkingDetails;
    SlotDetails slotDetails;

    public ParkingLot(int capacity) {
        this.observers = new ArrayList<>();
        this.capacity = capacity;
        slotDetails = new SlotDetails();
        carsParkingDetails = new LinkedHashMap<>();
        IntStream.rangeClosed(1, (capacity)).forEach(slotNumber -> carsParkingDetails.put(slotNumber, slotDetails));
    }

    public void initialiseMap(int capacity) {
        IntStream.rangeClosed(1, capacity).forEachOrdered(i -> mapSlot.put(i, " "));
    }

    HashMap<Integer, String> mapSlot = new HashMap<Integer, String>();

    public void registerObserver(IObserver observer) {
        this.observers.add(observer);
    }

    public boolean park(Car car) {
        return true;
    }

    String registration;
    Integer position;
    List<Map<String, Car>> parking = new ArrayList<>();

    public void parkWithDetails(Integer position, String registration) throws ParkingLotException {
        System.out.println(mapSlot.toString());
        if (mapSlot.size() > capacity) {
            observers.forEach(observer -> observer.parkingLotFull(true));
            throw new ParkingLotException("Parking Full",
                    ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        if (mapSlot.containsValue(registration)) throw new AssertionError("Already parked");
        if (mapSlot.size() < capacity) mapSlot.put(position, registration);
    }

    public int vehiclePresentPosition(String registration) throws ParkingLotException {
        this.parkWithDetails(position, registration);
        return parking.contains(registration) ? this.getKey(mapSlot, registration) : Integer.valueOf(0);
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value))
                .findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public int getPositionOfVehicle(String registration) {
        return getKey(mapSlot, registration);
    }

    public int getSlotPositionToPark(int capacity) {
        this.initialiseMap(capacity);
        return getKey(mapSlot, " ");
    }

    public LocalTime getParkingTime(String registration) throws ParkingLotException {
        this.vehiclePresentPosition(registration);
        return slotDetails.getTime();
    }

    public boolean unParkCar(String registration) throws ParkingLotException {
        if (mapSlot.containsKey(registration)) {
            mapSlot.remove(registration);
            observers.stream().forEachOrdered(observer -> observer.parkingLotAvailable(true));
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    public void parkVehicle(Car registration, String attendant) throws ParkingLotException {
        if (registration == null)
            throw new ParkingLotException("Null entered", ParkingLotException.ExceptionType.WRONG_DETAILS);
        if (carsParkingDetails.containsValue(registration))
            throw new ParkingLotException("Already Preset", ParkingLotException.ExceptionType.ALREADY_PARKED);
        if (carParked > capacity)
            throw new ParkingLotException("Parking Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        carsParkingDetails.put(getPosition(), new SlotDetails(getPosition(), registration,
                LocalTime.now().withNano(0), attendant));
        carParked++;
        if (carParked == capacity) {
            observers.forEach(observer -> observer.parkingLotAvailable(true));
        }
    }

    public void unParkVehicle(Car registration) {
        if (carsParkingDetails.containsValue(registration)) {
            carsParkingDetails.put(getPosition(), null);
        }
        carParked--;
    }

    public int getPosition() {
        return this.carsParkingDetails.keySet()
                .stream().filter(slotDetails -> carsParkingDetails.get(slotDetails).getCar() == null)
                .findFirst().orElse(0);
    }

    public int vehicleLocation(Car registration) {
        return this.getSlotDetails(registration).getSlotNu();
    }

    private SlotDetails getSlotDetails(Car registration) {
        return this.carsParkingDetails.values().stream().filter(slot -> registration.equals(slot.getCar()))
                .findFirst().get();
    }

    public boolean isVehiclePresent(Car registration) {
        return carsParkingDetails.values().stream()
                .anyMatch(slotDetails -> slotDetails.getCar() == (registration));
    }

    public int getNumberOfParkedCars() {
        return carParked;
    }

    public List<Integer> findOnColor(String color) {
        List<Integer> colorslot = carsParkingDetails.keySet().stream()
                .filter(slot -> carsParkingDetails.get(slot).getCar().getColor().equals(color))
                .collect(Collectors.toList());
        return colorslot;
    }

    public List<String> findbyColorAndCompany(String color, String brand) {
        List<String> attendants = carsParkingDetails.keySet().stream()
                .filter(slot -> carsParkingDetails.get(slot).getCar().getColor().equals(color) &&
                        carsParkingDetails.get(slot).getCar().getBrand().equals(brand))
                .map(slot -> slot + " " + carsParkingDetails.get(slot).getCar().getRegistration() + " " +
                        carsParkingDetails.get(slot).getAttendantName()).collect(Collectors.toList());
        return attendants;

    }

    public List<Integer> findCarsByBrand(String brand) {
        List<Integer> slots = carsParkingDetails.keySet()
                .stream().filter(slot -> carsParkingDetails.get(slot).getCar().getBrand().equals(brand))
                .collect(Collectors.toList());
        return slots;
    }

    private LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

    public List<Integer> findCarsParkedRecently(int minutes) {
        List<Integer> slots = carsParkingDetails.keySet().stream()
                .filter(slot -> carsParkingDetails.get(slot).getTime().getMinute() - currentTime().getMinute() <= minutes)
                .collect(Collectors.toList());
        return slots;
    }

    public List<String> findCarsParkedByHandicap(DriverCategory type, CarSize size) {
        return this.carsParkingDetails.values().stream().filter(slotDetails1 -> slotDetails1.getCar().getType() == (type))
                .filter(slotDetails1 -> slotDetails1.getCar().getSize().equals(size))
                .map(slotDetails1 -> (("  Slot: " + slotDetails1.getSlotNu() +
                        " Registration " + slotDetails1.getCar().getRegistration()
                        + " BRAND " + slotDetails1.getCar().getBrand() + " Size - " +
                        slotDetails1.getCar().getSize() + " AttendantName " + slotDetails1.getAttendantName())))
                .collect(Collectors.toList());
    }

    public List<String> findAllCarsParked() {
        return this.carsParkingDetails.values().stream()
                .map(details -> (("Slot: " + details.getSlotNu() + " Registration " +
                details.getCar().getRegistration() + ""))).collect(Collectors.toList());
    }
}