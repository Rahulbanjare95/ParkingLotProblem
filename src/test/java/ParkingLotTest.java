import exception.ParkingLotException;
import model.Car;
import org.junit.Assert;
import org.junit.Test;
import service.ParkingLot;

public class ParkingLotTest {
    ParkingLot parkingLot = new ParkingLot();

    @Test
    public void givenCar_whenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.park(new Car());
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenOneCarWithDetails_whenParked_ShouldReturnOneCar() throws ParkingLotException {
        int i = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
        Assert.assertEquals(1, i);
    }

    @Test
    public void givenCarWithDetails_whenUnParked_ShouldReturnTrue() {
        try {
            int i = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
            boolean isUnPark = parkingLot.unParkCar("CG11M7393");
            Assert.assertTrue(isUnPark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCarDetailsIncorrect_WhenUnParked_ShouldThrowException() {
        try {
            int i = parkingLot.parkWithDetails("CG11M7393", "HyundaiVerna");
            boolean isUnPark = parkingLot.unParkCar("CG11M793");
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenMultipleCarDetails_whenTriedToExceedParkingLimit_ShouldThrowException() throws ParkingLotException {
        parkingLot.parkWithDetails("CG11M7393", "Hyundai Verna");
        parkingLot.parkWithDetails("KA01B1212", "BMW 720D");
        parkingLot.parkWithDetails("DL03C0003", "Mercedes-Benz S-Class");
        parkingLot.parkWithDetails("MH04CD1011", "Mercedes-Benz S-Class");
        try {
            int parkingAllotmentDetails = parkingLot.getParkingAllotmentDetails();
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }

    }
}
