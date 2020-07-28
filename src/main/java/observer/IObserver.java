package observer;

public interface IObserver {

    void parkingLotFull(boolean isLotFull);

    void parkingLotAvailable(boolean isLotAvailable);
}
