package main.model;

import java.util.Date;

public class DriverPlacementInRace {
    private final Integer driverNumber;
    private final String raceName;
    private final Integer rank;

    public DriverPlacementInRace(Integer driverNumber, String raceName, Integer rank) {
        this.driverNumber = driverNumber;
        this.raceName = raceName;
        this.rank = rank;
    }

    public Integer getDriverNumber() {
        return driverNumber;
    }

    public String getRaceName() {
        return raceName;
    }

    public Integer getRank() {
        return rank;
    }
}
