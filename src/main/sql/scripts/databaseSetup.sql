CREATE TABLE Constructor
(
    ConstructorName CHAR(80),
    Position        INTEGER,
    Points          INTEGER,
    PRIMARY KEY (ConstructorName)
);

CREATE TABLE Engine
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    PRIMARY KEY (ModelLine, Manufacturer)
);

CREATE TABLE Chassis
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    PRIMARY KEY (ModelLine, Manufacturer)
);

CREATE TABLE Driver
(
    DriverNumber    INTEGER,
    DriverName      CHAR(10),
    DriverAge       INTEGER,
    DriverPoints    FLOAT,
    ConstructorName CHAR(8) NOT NULL,
    PRIMARY KEY (DriverNumber),
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
    -- ON UPDATE CASCADE
);

CREATE TABLE Car
(
    CarNumber    INTEGER,
    CarName      CHAR(8),
    DriverNumber INTEGER,
    PRIMARY KEY (CarNumber, DriverNumber),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver (DriverNumber)
            ON DELETE CASCADE
);

CREATE TABLE ChassisConsDesigns
(
    ModelLine       CHAR(18),
    Manufacturer    CHAR(20),
    ConstructorName CHAR(80),
    PRIMARY KEY (ModelLine, Manufacturer, ConstructorName),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Chassis (ModelLine, Manufacturer)
            ON DELETE CASCADE,
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
            ON DELETE CASCADE
);

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
);

CREATE TABLE CircuitLocation
(
    City    CHAR(30),
    Country CHAR(30),
    PRIMARY KEY (City)
);

CREATE TABLE Circuit
(
    CircuitName CHAR(75),
    Length      INTEGER,
    City        CHAR(30) NOT NULL,
    PRIMARY KEY (CircuitName),
    FOREIGN KEY (City)
        REFERENCES CircuitLocation (City)
            ON DELETE CASCADE
);

CREATE TABLE RaceDate
(
    StartDate DATE,
    EndDate   DATE,
    PRIMARY KEY (EndDate)
);

CREATE TABLE Race
(
    RaceName               CHAR(75),
    Laps                   INTEGER,
    EndDate                DATE,
    FastestLapAverageSpeed FLOAT    NOT NULL,
    CircuitName            CHAR(75) NOT NULL,
    PRIMARY KEY (RaceName),
    FOREIGN KEY (EndDate)
        REFERENCES RaceDate (EndDate),
    /*FOREIGN KEY (FastestLapAverageSpeed, RaceName)
        REFERENCES FastestLap (AverageSpeed, RaceName)
            ON DELETE CASCADE,*/
    FOREIGN KEY (CircuitName)
        REFERENCES Circuit (CircuitName)
            ON DELETE CASCADE
);

CREATE TABLE FastestLap
(
    AverageSpeed FLOAT,
    LapTime      TIMESTAMP,
    RaceName     CHAR(75) NOT NULL,
    DriverNumber INTEGER  NOT NULL,
    PRIMARY KEY (AverageSpeed, RaceName),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver (DriverNumber)
            ON DELETE CASCADE
                -- ON UPDATE CASCADE
    /*FOREIGN KEY (RaceName)
        REFERENCES Race (RaceName)
            ON DELETE CASCADE
                --ON UPDATE CASCADE*/
);

-- Race<-> Fastestlap circular dependency, fix below
-- https://stackoverflow.com/questions/18832586/creating-tables-with-circular-reference
ALTER TABLE Race
    ADD CONSTRAINT FK_LAP
        FOREIGN KEY (FastestLapAverageSpeed, RaceName)
            REFERENCES FastestLap (AverageSpeed, RaceName)
                ON DELETE CASCADE
                DEFERRABLE;

ALTER TABLE FastestLap
    ADD CONSTRAINT FK_RACE
        FOREIGN KEY (RaceName)
            REFERENCES Race (RaceName)
                ON DELETE CASCADE
                DEFERRABLE;

CREATE TABLE ChassisCar
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    CarNumber    INTEGER,
    DriverNumber INTEGER,
    PRIMARY KEY (ModelLine, Manufacturer, CarNumber, DriverNumber),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Chassis (ModelLine, Manufacturer)
            ON DELETE CASCADE,
    FOREIGN KEY (CarNumber, DriverNumber)
        REFERENCES Car (CarNumber, DriverNumber)
            ON DELETE CASCADE
);

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
        REFERENCES Car (CarNumber, DriverNumber)
            ON DELETE CASCADE
);

CREATE TABLE ConstructorCreatesCar
(
    ConstructorName CHAR(80),
    CarNumber       INTEGER,
    DriverNumber    INTEGER,
    PRIMARY KEY (ConstructorName, CarNumber, DriverNumber),
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
            ON DELETE CASCADE,
    FOREIGN KEY (CarNumber, DriverNumber)
        REFERENCES Car (CarNumber, DriverNumber)
            ON DELETE CASCADE
);

CREATE TABLE DriverRacesInCircuit
(
    DriverNumber INTEGER,
    CircuitName  CHAR(75),
    PRIMARY KEY (DriverNumber, CircuitName),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver
            ON DELETE CASCADE,
    FOREIGN KEY (CircuitName)
        REFERENCES Circuit (CircuitName)
            ON DELETE CASCADE
);


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
);

ALTER SESSION SET CONSTRAINTS = DEFERRED;

/*
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

INSERT INTO DrivePlacesInRace
VALUES (2, 'Bahrain Grand Prix', 9);
INSERT INTO DrivePlacesInRace
VALUES (4, 'Emilia Romagna Grand Prix', 4);
INSERT INTO DrivePlacesInRace
VALUES (55, 'Portugese Grand Prix', 5);
INSERT INTO DrivePlacesInRace
VALUES (69, 'Spanish Grand Prix', 1);
INSERT INTO DrivePlacesInRace
VALUES (42, 'Monaco Grand Prix', 99);*/

