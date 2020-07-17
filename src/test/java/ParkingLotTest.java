import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest {
    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        ParkingLot parkingLot = new ParkingLot();
        boolean isParked = parkingLot.park(new  Car());
        Assert.assertTrue(isParked);
    }
}
