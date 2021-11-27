package main.model;

import java.util.Date;

public class ConstructorRace {




    private final String grandPrix;
    private final Date date;
    private final Double points;

    public Date getDate() {
        return date;
    }

    public Double getPoints() {
        return points;
    }

    public String getGrandPrix() {
        return grandPrix;
    }

    public ConstructorRace(String grandPrix, Date date, Double points) {
        this.grandPrix = grandPrix;
        this.date = date;
        this.points = points;
    }
}