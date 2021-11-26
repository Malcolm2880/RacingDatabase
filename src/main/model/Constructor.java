package main.model;

public class Constructor {




    private final String name;
    private final Integer position;
    private final Float points;

    public Constructor(String name, Integer position, Float points) {
        this.name = name;
        this.position = position;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Integer getPosition() {
        return position;
    }

    public Float getPoints() {
        return points;
    }

}
