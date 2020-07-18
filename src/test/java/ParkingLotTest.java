import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParkingLotTest {
    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new  Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenCarWithDetails_whenParked_ShouldReturnTrue() {
        boolean b = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
        Assert.assertTrue(b);
    }

    @Test
    public void givenCarWithDetails_whenUnparked_ShouldReturnTrue() {
        boolean b = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
        boolean isUnpark = parkingLot.unParkCar("CG11M7393");
        Assert.assertTrue(isUnpark);

    }
}
