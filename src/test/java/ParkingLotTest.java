import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotTest {

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        ParkingLot parkingLot = new ParkingLot();
        boolean isParked = parkingLot.park(new  Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenCarWithDetails_whenParked_ShouldReturnTrue() {
        ParkingLot parkingLot = new ParkingLot();
        boolean b = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
        Assert.assertTrue(b);
    }


}
