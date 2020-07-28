package service;

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
        for (int i = 1; i <= capacity; i++) {
            mapSlot.put(i, " ");
        }
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
        if (mapSlot.size() > capacity) {
            for (IObserver observer : observers) {
                observer.parkingLotFull(true);
            }
            throw new ParkingLotException("Parking Full",
                    ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        if (mapSlot.containsValue(registration))
            throw new ParkingLotException("Already parked", ParkingLotException.ExceptionType.WRONG_DETAILS);
        if (mapSlot.size() < capacity) {
            mapSlot.put(position, registration);
        }
    }

    public int vehiclePresentPosition(String registration) throws ParkingLotException {
        this.parkWithDetails(position, registration);
        if (parking.contains(registration)) {
            return this.getKey(mapSlot, registration);
        } else return 0;
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
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
            for (IObserver observer : observers) {
                observer.parkingLotAvailable(true);
            }
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
        List<Integer> colorslot = new ArrayList<>();
        for (Integer slot : carsParkingDetails.keySet()) {
            if (carsParkingDetails.get(slot).getCar().getColor().equals(color)) {
                colorslot.add(slot);
            }
        }
        return colorslot;
    }

    public List<String> findbyColorAndCompany(String color, String brand) {
        List<String> attendants = new ArrayList<>();
        for (Integer slot : carsParkingDetails.keySet()) {
            if (carsParkingDetails.get(slot).getCar().getColor().equals(color) && carsParkingDetails.get(slot).getCar().getBrand().equals(brand))
                attendants.add(slot + " " + carsParkingDetails.get(slot).getCar().getRegistration() + " " + carsParkingDetails.get(slot).getAttendantName());
        }
        return attendants;

    }

    public List<Integer> findCarsByBrand(String brand) {
        List<Integer> slots = new ArrayList<>();
        for (Integer slot : carsParkingDetails.keySet()) {
            if (carsParkingDetails.get(slot).getCar().getBrand().equals(brand)) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

    public List<Integer> findCarsParkedRecently(int minutes) {
        List<Integer> slots = new ArrayList<>();
        for (Integer slot : carsParkingDetails.keySet()) {
            if (carsParkingDetails.get(slot).getTime().getMinute() - currentTime().getMinute() <= minutes) {
                slots.add(slot);
            }
        }
        return slots;
    }

    public List<String> findCarsParkedByHandicap(DriverCategory type, String size) {
        return this.carsParkingDetails.values().stream().filter(slotDetails1 -> slotDetails1.getCar().getType() == (type))
                .filter(slotDetails1 -> slotDetails1.getCar().getSize().equals(size))
                .map(slotDetails1 -> (("  Slot: " + slotDetails1.getSlotNu() +
                        " Registration " + slotDetails1.getCar().getRegistration()
                        + " BRAND " + slotDetails1.getCar().getBrand() + " Size - " +
                        slotDetails1.getCar().getSize() + " AttendantName " + slotDetails1.getAttendantName())))
                .collect(Collectors.toList());
    }

    public List<String> findAllCarsParked() {
        return this.carsParkingDetails.values().stream().map(details -> (("Slot: " + details.getSlotNu() + " Registration " +
                details.getCar().getRegistration() + ""))).collect(Collectors.toList());
    }
}