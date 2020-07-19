package service;

public enum ParkingLotControllers {
    AIRPORT_SECURITY(true), PARKING_LOT_OWNER(false);

    public boolean isParkingLotFull;

    ParkingLotControllers(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;
    }

}
