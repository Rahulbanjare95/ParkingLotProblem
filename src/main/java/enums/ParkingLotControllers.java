package enums;

public enum ParkingLotControllers {
    AIRPORT_SECURITY(true), PARKING_LOT_OWNER(true);

    public boolean isParkingLotFull;

    ParkingLotControllers(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;
    }
}
