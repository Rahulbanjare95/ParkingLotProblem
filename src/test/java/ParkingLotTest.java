import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {

    Object vehicle = null;
    ParkingLot parkingLot = null;

    @Before
    public void setUp() throws Exception {
        vehicle = new Object();
        parkingLot = new ParkingLot();
    }

    @Test
    public void givenAVehicle_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Object());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenAVehicle_whenAlreadyParked_ShouldReturnFalse() {
        parkingLot.park(vehicle);
        boolean isParked = parkingLot.park(vehicle);
        Assert.assertFalse(isParked);
    }

    @Test
    public void givenNull_whenParked_ShouldReturnFalse() {
        parkingLot.park(vehicle);
        boolean isParked = parkingLot.park(null);
        Assert.assertFalse(isParked);

    }

    @Test
    public void givenAVehicle_whenUnParked_ShouldReturnTrue() {
        try {
            parkingLot.park(vehicle);
            boolean isUnParked = parkingLot.unPark(vehicle);
            Assert.assertTrue(isUnParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicle_whenUnParkedAlready_ShouldReturnException() {
        try {
            parkingLot.park(vehicle);
            parkingLot.unPark(vehicle);
            boolean isUnParked = parkingLot.unPark(vehicle);
            Assert.assertFalse(isUnParked);
        } catch (ParkingLotException | NullPointerException e) {
            e.printStackTrace();

        }

    }

    @Test
    public void givenNull_whenUnParked_ShouldReturnFalse() {
        try {
            parkingLot.park(null);
            boolean isUnParked = parkingLot.unPark(null);
            Assert.assertFalse(isUnParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}
