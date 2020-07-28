package service;

import enums.DriverCategory;
import exception.ParkingLotException;
import model.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem<whiteVehicleDetails> {

    int parkingLot;
    int parkingSlotInLot;
    public ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int parkingLot, int parkingSlotInLot) {

        this.parkingLot = parkingLot;
        this.parkingSlotInLot = parkingSlotInLot;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, parkingLot).forEachOrdered(lot -> parkingLotList.add(new ParkingLot(parkingSlotInLot)));
    }

    public void parkVehicle(Car carDetails, DriverCategory type, String attendant) throws ParkingLotException {

        for (ParkingLot lot : parkingLotList) {
            boolean isVehiclePresent = lot.isVehiclePresent(carDetails);
            if (isVehiclePresent) {
                throw new ParkingLotException("Already parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
            }
        }
        if (type.equals(DriverCategory.HANDICAP)) {
            if (carDetails.getSize().equals("SMALL"))
                IntStream.range(0, parkingLot).filter(lot -> parkingLotList.get(lot)
                        .getNumberOfParkedCars() != parkingSlotInLot)
                        .findFirst().ifPresent(lot -> {
                    try {
                        parkingLotList.get(lot).parkVehicle(carDetails, attendant);
                    } catch (ParkingLotException e) {
                        e.printStackTrace();
                    }
                });
            if (carDetails.getSize().equals("LARGE")) {
                ParkingLot lots = getPositionOnLot();
                lots.parkVehicle(carDetails, attendant);

            }
        }
        if (type.equals(DriverCategory.NORMAL)) {
            ParkingLot lots = getPositionOnLot();
            lots.parkVehicle(carDetails, attendant);
        }
    }


    private ParkingLot getPositionOnLot() throws ParkingLotException {
        int totalParked = IntStream.range(0, parkingLot).map(slot -> this.parkingLotList.get(slot).getNumberOfParkedCars()).sum();
        if (totalParked > (parkingLot * parkingSlotInLot)) {
            throw new ParkingLotException("Parking Full", ParkingLotException.ExceptionType.PARKING_LOT_FULL);
        }
        List<ParkingLot> lotList = new ArrayList<>(this.parkingLotList);
        lotList.sort(Comparator.comparing(ParkingLot::getNumberOfParkedCars));
        ParkingLot lotTobeParked = lotList.get(0);
        return lotTobeParked;
    }

    public boolean isVehiclePresent(Car carDetails) {
        return this.parkingLotList.stream().anyMatch(lot -> lot.isVehiclePresent(carDetails));
    }


    public String vehicleLocation(Car carDetails) {
        ParkingLot parked = this.parkingLotList.stream()
                .filter(lot -> lot.isVehiclePresent(carDetails)).findFirst().get();
        String vehicleLocation = String.format("lot %d slot %d", parkingLotList.indexOf(parked) + 1, parked.
                vehicleLocation(carDetails));
        System.out.println(vehicleLocation);
        return vehicleLocation;
    }

    public List findWhiteVehiclePosition(String color) throws ParkingLotException {
        List<String> whiteVehicleDetails = new ArrayList<>();
        int lot = 0;
        for (ParkingLot parkingLot : parkingLotList) {
            List<Integer> onColor = parkingLot.findOnColor(color);
            if (onColor.size() == 0)
                throw new ParkingLotException("No such color", ParkingLotException.ExceptionType.NO_SUCH_COLOR);
            lot++;
            whiteVehicleDetails.add("lot " + lot + " slot " + onColor);
        }

//        if (whiteVehicleDetails.contains(color)==0){
//            throw  new ParkingLotException("No such color",ParkingLotException.ExceptionType.NO_SUCH_COLOR);
//        }
        return whiteVehicleDetails;
    }

    public List<String> findCarByBrandsAndColor(String color, String brand) throws ParkingLotException {
        List<String> listofCarsColorAttendants = new ArrayList<>();
        int lot = 0;
        for (ParkingLot parkingLot : parkingLotList) {
            List<String> attendants = parkingLot.findbyColorAndCompany(color, brand);
            lot++;
            listofCarsColorAttendants.add("lot " + lot + " slot " + attendants);
        }
        if (listofCarsColorAttendants.size() == 0)
            throw new ParkingLotException("No such color", ParkingLotException.ExceptionType.NO_SUCH_COLOR);
        return listofCarsColorAttendants;
    }


    public List<String> findBMWCars(String brand) throws ParkingLotException {
        List<String> lisofBMWCars = new ArrayList<>();
        int lot = 0;
        for (ParkingLot parkingLot : parkingLotList) {
            List<Integer> bmwSlots = parkingLot.findCarsByBrand(brand);
            lot++;
            lisofBMWCars.add("lot " + lot + " slot " + bmwSlots);
        }
        if (lisofBMWCars.size() == 0) {
            throw new ParkingLotException("No such brand Parked", ParkingLotException.ExceptionType.NO_SUCH_BRAND);
        }
        return lisofBMWCars;
    }

    public List<String> findCarsParkedByTime(int minutes) throws ParkingLotException {
        List<String> parkingTimeList = new ArrayList<>();
        int lot =0;
        for (ParkingLot parkingLot: parkingLotList){
            List<Integer> slotsParkedRecently = parkingLot.findCarsParkedRecently(minutes);
            lot++;
            parkingTimeList.add("lot"+ lot+ " slot "+slotsParkedRecently);
        }
        if (parkingTimeList.size() == 0){
            throw new ParkingLotException("No Cars Parked in given time",ParkingLotException.ExceptionType.NOT_PARKED_IN_GIVEN_TIME);
        }
        return parkingTimeList;

    }
}