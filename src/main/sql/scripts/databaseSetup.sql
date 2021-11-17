CREATE TABLE Constructor
(
    ConstructorName CHAR(80),
    Position        INTEGER,
    Points          FLOAT,
    PRIMARY KEY (ConstructorName)
);

CREATE TABLE Driver
(
    DriverNumber    INTEGER,
    DriverName      CHAR(30),
    DriverAge       INTEGER,
    DriverPoints    FLOAT,
    ConstructorName CHAR(80) NOT NULL,
    PRIMARY KEY (DriverNumber),
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor (ConstructorName)
    -- ON UPDATE CASCADE
);

CREATE TABLE Car
(
    CarNumber    INTEGER,
    CarName      CHAR(80),
    DriverNumber INTEGER,
    PRIMARY KEY (CarNumber, DriverNumber),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver (DriverNumber)
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
    LapTime      CHAR(10),
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



/**
  Initial data is based on the first race of the season for Mercedes
 */

INSERT INTO Constructor VALUES ('Mercedes', 1, 41.0);
INSERT INTO Driver VALUES (44, 'Lewis Hamilton', 36, 25.0, 'Mercedes');
INSERT INTO Driver VALUES (77, 'Valtteri Bottas', 32, 16.0, 'Mercedes');
INSERT INTO Car VALUES (44, 'Mercedes-AMG F1 W12 E Performance', 44);
INSERT INTO Car VALUES (77, 'Mercedes-AMG F1 W12 E Performance', 77);
INSERT INTO CircuitLocation VALUES ('Sakhir', 'Bahrain');
INSERT INTO RaceDate VALUES ('2021-03-26', '2021-03-28');
INSERT INTO Circuit VALUES ('Bahrain International Circuit', 5412, 'Sakhir');
INSERT INTO FastestLap VALUES (211.566, '01:32:090', 'Bahrain Grand Prix', 77);
INSERT INTO Race VALUES ('Bahrain Grand Prix', 56, '2021-03-28', 211.566, 'Bahrain International Circuit');
INSERT INTO ConstructorCreatesCar VALUES ('Mercedes', 77, 77);
INSERT INTO ConstructorCreatesCar VALUES ('Mercedes', 44, 44);
INSERT INTO DriverRacesInCircuit VALUES (44, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (77, 'Bahrain International Circuit');
INSERT INTO DrivePlacesInRace VALUES (44, 'Bahrain Grand Prix', 1);
INSERT INTO DrivePlacesInRace VALUES (77, 'Bahrain Grand Prix', 2);


