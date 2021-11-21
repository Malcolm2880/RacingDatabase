package main.model;

import java.util.Date;

public class Constructor {
    private final String name;



    private final Integer position;
    private final float points;


    public Constructor(String name, Integer position, float points) {
        this.name = name;
        this.position = position;
        this.points = points;
    }

    public Integer getPosition() {
        return position;
    }

    public float getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

}
