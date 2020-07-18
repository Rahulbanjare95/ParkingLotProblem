import java.util.*;

public class ParkingLot {
    Car car = new Car();

    Map<String, Car> carMap= new HashMap<>();
    int capacity  = 3;

    public boolean park(Car car) {
        return true;
    }


    public boolean parkWithDetails(String registration, String model) {
        if (carMap.size() < capacity){
            carMap.put(registration, new Car(registration, model));
            return true;
        }
        return false;
    }
    public boolean unParkCar (String registration){

        if (carMap.containsKey(registration)) {
            carMap.remove(registration);
            return true;
        }
            return false;
        }
}