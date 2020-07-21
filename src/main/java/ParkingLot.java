
public class ParkingLot {
    private Object vehicle;

    public ParkingLot() {
    }

    public boolean park(Object vehicle) {
        if(this.vehicle == null) return false;
        if( this.vehicle != null )
            return false;
        this.vehicle = vehicle;
        return true;
    }


    public boolean unPark(Object vehicle) throws ParkingLotException {
        if(this.vehicle == null) return  false;
        if (this.vehicle.equals(vehicle)) {
            this.vehicle = null;
            return true;
        }
        throw new ParkingLotException("No such model exist", ParkingLotException.ExceptionType.WRONG_DETAILS);
    }
}
