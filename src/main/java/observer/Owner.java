package observer;

public class Owner implements IObserver {


    private boolean isLotFull;
    private boolean isLotAvailable;

    public void parkingLotFull(boolean b) {

        this.isLotFull = true;
    }

    @Override
    public void parkingLotAvailable(boolean isLotAvailable) {
        this.isLotAvailable = isLotAvailable;

    }

    public boolean isParkingLotAvailable() {
        this.parkingLotAvailable(isLotAvailable);
        return isLotAvailable;
    }

    public boolean isParkingLotFull() {
        this.parkingLotFull(isLotAvailable);
        return isLotFull;


    }
}


