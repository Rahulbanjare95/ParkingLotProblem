package service;

public enum ParkingLotControllers {
    AIRPORT_SECURITY(false), PARKING_LOT_OWNER(false);

    public boolean isParkingLotFull;
    public boolean isSpaceAvailableInParking;

    ParkingLotControllers(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;
    }

}
