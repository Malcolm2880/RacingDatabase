package main.model;

import java.util.Date;

public class FastestLap {


    private final Float averageSpeed;
    private final String lapTime;
    private final String raceName;
    private final Integer driverNumber;


    public FastestLap(Float averageSpeed, String lapTime, String raceName, Integer driverNumber) {
        this.averageSpeed = averageSpeed;
        this.lapTime = lapTime;
        this.raceName = raceName;
        this.driverNumber = driverNumber;
    }
    public Float getAverageSpeed() {
        return averageSpeed;
    }

    public String getLapTime() {
        return lapTime;
    }

    public String getRaceName() {
        return raceName;
    }

    public Integer getDriverNumber() {
        return driverNumber;
    }
}