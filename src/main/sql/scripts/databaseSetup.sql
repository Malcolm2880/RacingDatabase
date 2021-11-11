CREATE TABLE ConstructorCreatesCar
(
    ConstructorName CHAR(8),
    CarNumber       INTEGER,
    DriverNumber    INTEGER,
    PRIMARY KEY (ConstructorName, CarNumber, DriverNumber),
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
            ON DELETE CASCADE,
        FOREIGN KEY (CarNumber,DriverNumber)
        REFERENCES Car(CarNumber, DriverNumber)
        ON DELETE CASCADE
)

CREATE TABLE EngineCar
(
    ModelLine    CHAR(18),
    Manufacturer CHAR(20),
    CarNumber    INTEGER,
    DriverNumber INTEGER,
    PRIMARY KEY (ModelLine, Manufacturer, CarNumber, DriverNumber),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Engine (ModelLine, Manufacturer)
            ON DELETE CASCADE,
        FOREIGN KEY (CarNumber, DriverNumber)
        REFERENCES Car(CarNumber, DriverNumber)
        ON DELETE CASCADE
)

CREATE TABLE ChassisConsDesigns
(
    ModelLine       CHAR(18),
    Manufacturer    CHAR(20),
    ConstructorName CHAR(8),
    PRIMARY KEY (ModelLine, Manufacturer, ConstructorName),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Chassis (ModelLine, Manufacturer)
            ON DELETE CASCADE,
        FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
        ON DELETE CASCADE
)
CREATE TABLE EngineConsDesigns
(
    ModelLine       CHAR(18),
    Manufacturer    CHAR(20),
    ConstructorName CHAR(80),
    PRIMARY KEY (ModelLine, Manufacturer, ConstructorName),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Engine (ModelLine, Manufacturer)
            ON DELETE CASCADE,
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
            ON DELETE CASCADE
)

CREATE TABLE DrivePlacesInRace
(
    DriverNumber INTEGER,
    RaceName     CHAR(75),
    Rank         INTEGER,
    PRIMARY KEY (DriverNumber, RaceName),
    FOREIGN KEY (RaceName)
        REFERENCES Race (RaceName)
            ON DELETE CASCADE,
        FOREIGN KEY (DriverNumber)
        REFERENCES Driver(DriverNumber)
        ON DELETE CASCADE
)

INSERT INTO ConstructorCreatesCar
VALUES ('Mercedes', 2, 1);
INSERT INTO ConstructorCreatesCar
VALUES ('Ferrari', '4', '2');
INSERT INTO ConstructorCreatesCar
VALUES ('RedBullRacingHonda', '55', '3');
INSERT INTO ConstructorCreatesCar
VALUES ('AlpineRenault', '69', '4');
INSERT INTO ConstructorCreatesCar
VALUES ('McLarenMercedes', '42', '5');

INSERT INTO EngineCar
VALUES ('Spirits', 'Ghost Productions', 1, 2);
INSERT INTO EngineCar
VALUES ('Computational', 'Queen Manufacturing', 2, 4);
INSERT INTO EngineCar
VALUES ('Chad', 'Chad', 3, 55);
INSERT INTO EngineCar
VALUES ('Test', 'Bankruptcy Racing', 4, 69);
INSERT INTO EngineCar
VALUES ('Weapons', 'Winchester', 5, 42);


INSERT INTO ChassisConsDesigns
VALUES ('Spirits', 'Ghost Productions', 'Mercedes');
INSERT INTO ChassisConsDesigns
VALUES ('Computational', 'Queen Manufacturing', 'Ferrari');
INSERT INTO ChassisConsDesigns
VALUES ('Chad', 'Chad', 'RedBullRacingHonda');
INSERT INTO ChassisConsDesigns
VALUES ('Test', 'Bankruptcy Racing', 'AlpineRenault');
INSERT INTO ChassisConsDesigns
VALUES ('Weapons', 'Winchester', 'McLarenMercedes');

INSERT INTO EngineConsDesigns
VALUES ('Spirits', 'Ghost Productions', 'Mercedes');
INSERT INTO EngineConsDesigns
VALUES ('Computational', 'Queen Manufacturing', 'Ferrari');
INSERT INTO EngineConsDesigns
VALUES ('Chad', 'Chad', 'RedBullRacingHonda');
INSERT INTO EngineConsDesigns
VALUES ('Test', 'Bankruptcy Racing', 'AlpineRenault');
INSERT INTO EngineConsDesigns
VALUES ('Weapons', 'Winchester', 'McLarenMercedes');

INSERT INTO DriverRacesInRace
VALUES (2, 'Bahrain Grand Prix', 9);
INSERT INTO DriverRacesInRace
VALUES (4, 'Emilia Romagna Grand Prix', 4);
INSERT INTO DriverRacesInRace
VALUES (55, 'Portugese Grand Prix', 5);
INSERT INTO DriverRacesInRace
VALUES (69, 'Spanish Grand Prix', 1);
INSERT INTO DriverRacesInRace
VALUES (42, 'Monaco Grand Prix', 99);

