public class ParkingLotException extends Exception {

    public enum ExceptionType {
        WRONG_DETAILS
    }

    public ExceptionType type;

    ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
