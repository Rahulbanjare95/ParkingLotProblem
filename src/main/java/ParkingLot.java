import java.util.*;

public class ParkingLot {
    Car car = new Car();

    Map<String, Car> carMap= new HashMap<>();

    public boolean park(Car car) {
        return true;
    }
    String registration;
    String model;
    int capacity  = 3;
    public int parkWithDetails(String registration, String model) throws ParkingLotException {

        if (carMap.size() <= capacity){
            carMap.put(registration, new Car(registration, model));
            return carMap.size();
        }
        throw new ParkingLotException("ParkingLot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }
    public boolean unParkCar (String registration) throws ParkingLotException {
        if (carMap.containsKey(registration)) {
            carMap.remove(registration);
            return true;
        }
         throw  new ParkingLotException("No such Car exist",ParkingLotException.ExceptionType.WRONG_DETAILS);
    }

    public int getParkingAllotmentDetails() throws ParkingLotException {
        int i = this.parkWithDetails(registration, model);
        if(i<capacity){
            return i;
        }
        if (i == capacity){
            return capacity;
        }
        throw new ParkingLotException("ParkingLot Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
    }
}