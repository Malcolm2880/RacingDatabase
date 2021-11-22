package main.model;

public class Driver {
    private final String name;
    private final Integer number;
    private final Integer age;
    private final Double points;
    private final Integer position;
    private final String constructorName;
    private final Boolean didFastestLap;

    public Driver(String name, Integer number, Integer age, Double points, Integer position, String constructorName, Boolean didFastestLap) {
        this.name = name;
        this.number = number;
        this.age = age;
        this.points = points;
        this.position = position;
        this.constructorName = constructorName;
        this.didFastestLap = didFastestLap;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getAge() {
        return age;
    }

    public Double getPoints() {
        return points;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public Boolean getDidFastestLap() {
        return didFastestLap;
    }

    public Integer getPosition() {
        return position;
    }
}
