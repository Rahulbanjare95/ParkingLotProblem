package exception;

public class ParkingLotException extends Exception {

    public enum ExceptionType {
        PARKING_LOT_FULL, WRONG_DETAILS
    }

    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}