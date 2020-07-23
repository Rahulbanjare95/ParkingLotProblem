package service;

public class Driver implements  IObserver {

    @Override
    public boolean isParkedAt(boolean isParked) {
        return true;
    }

    public boolean isAvailable(boolean isParked) {
        return true;
    }
}
