package exception;

public class ParkingLotException extends Exception {

    public enum ExceptionType {
        PARKING_LOT_FULL, ALREADY_PARKED, WRONG_DETAILS, NO_SUCH_COLOR ;
    }

    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
