package main.model;

import java.util.Date;

public class Race {
    private final String name;
    private final Integer numberOfLaps;
    private final Date date;
    private final Float fastestLapAverageSpeed;
    private final String circuitName;
    private final String winnerDriver;
    private final String winnerConstructor;

    public Race(String name, Integer numberOfLaps, Date date, Float fastestLapAverageSpeed, String circuitName, String winnerDriver, String winnerConstructor) {
        this.name = name;
        this.numberOfLaps = numberOfLaps;
        this.date = date;
        this.fastestLapAverageSpeed = fastestLapAverageSpeed;
        this.circuitName = circuitName;
        this.winnerDriver = winnerDriver;
        this.winnerConstructor = winnerConstructor;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfLaps() {
        return numberOfLaps;
    }

    public Date getDate() {
        return date;
    }

    public Float getFastestLapAverageSpeed() {
        return fastestLapAverageSpeed;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public String getWinnerDriver() {
        return winnerDriver;
    }

    public String getWinnerConstructor() {
        return winnerConstructor;
    }
}
