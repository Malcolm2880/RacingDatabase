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
  Initial data is based on the first race of the season and the constant data of the
  season like teams, drivers, and cars.
 */

INSERT INTO Constructor VALUES ('Mercedes', 1, 41.0);
INSERT INTO Constructor VALUES ('Red Bull Racing Honda', 2, 28.0);
INSERT INTO Constructor VALUES ('McLaren Mercedes', 3, 18.0);
INSERT INTO Constructor VALUES ('Ferrari', 4, 12.0);
INSERT INTO Constructor VALUES ('AlphaTauri Honda', 5, 2.0);
INSERT INTO Constructor VALUES ('Aston Martin Mercedes', 6, 1.0);
INSERT INTO Constructor VALUES ('Alpine Renault', 7, 0.0);
INSERT INTO Constructor VALUES ('Alfa Romeo Racing Ferrari', 8, 0.0);
INSERT INTO Constructor VALUES ('Haas Ferrari', 9, 0.0);
INSERT INTO Constructor VALUES ('Williams Mercedes', 10, 0.0);

INSERT INTO Driver VALUES (44, 'Lewis Hamilton', 36, 25.0, 'Mercedes');
INSERT INTO Driver VALUES (77, 'Valtteri Bottas', 32, 16.0, 'Mercedes');
INSERT INTO Driver VALUES (33, 'Max Verstapen', 24, 18.0, 'Red Bull Racing Honda');
INSERT INTO Driver VALUES (11, 'Sergio Perez', 31, 10.0, 'Red Bull Racing Honda');
INSERT INTO Driver VALUES (4, 'Lando Norris', 22, 12.0, 'McLaren Mercedes');
INSERT INTO Driver VALUES (3, 'Daniel Ricciardo', 32, 6.0, 'McLaren Mercedes');
INSERT INTO Driver VALUES (16, 'Charles Leclerc', 24, 8.0, 'Ferrari');
INSERT INTO Driver VALUES (55, 'Carlos Sainz Jr.', 27, 4.0, 'Ferrari');
INSERT INTO Driver VALUES (22, 'Yuki Tsunoda', 21, 2.0, 'AlphaTauri Honda');
INSERT INTO Driver VALUES (10, 'Pierre Gasly', 25, 0.0, 'AlphaTauri Honda');
INSERT INTO Driver VALUES (14, 'Fernando Alonso', 40, 0.0, 'Alpine Renault');
INSERT INTO Driver VALUES (31, 'Esteban Ocon', 25, 0.0, 'Alpine Renault');
INSERT INTO Driver VALUES (5, 'Sebastian Vettel', 35, 0.0, 'Aston Martin Mercedes');
INSERT INTO Driver VALUES (18, 'Lance Stroll', 23, 0.0, 'Aston Martin Mercedes');
INSERT INTO Driver VALUES (99, 'Antonio Giovinazzi', 27, 0.0, 'Alfa Romeo Racing Ferrari');
INSERT INTO Driver VALUES (7, 'Kimi Raikkonen', 42, 0.0, 'Alfa Romeo Racing Ferrari');
INSERT INTO Driver VALUES (47,'Mick Schumacher',22, 0.0, 'Haas Ferrari');
INSERT INTO Driver VALUES (9,'Nikita Mazepin',22, 0.0, 'Haas Ferrari');
INSERT INTO Driver VALUES (63, 'George Russell', 23, 0.0, 'Williams Mercedes');
INSERT INTO Driver VALUES (6, 'Nicholas Latifi', 26, 0.0, 'Williams Mercedes');

INSERT INTO Car VALUES (44, 'Mercedes-AMG F1 W12 E Performance', 44);
INSERT INTO Car VALUES (77, 'Mercedes-AMG F1 W12 E Performance', 77);
INSERT INTO Car VALUES (33,'Red Bull Racing RB16B', 33);
INSERT INTO Car VALUES (11,'Red Bull Racing RB16B', 11);
INSERT INTO Car VALUES (16, 'Ferrari SF21', 16);
INSERT INTO Car VALUES (55, 'Ferrari SF21', 55);
INSERT INTO Car VALUES (4, 'McLaren MCL35M', 4);
INSERT INTO Car VALUES (3, 'McLaren MCL35M', 3);
INSERT INTO Car VALUES (22, 'AlphaTauri AT02', 22);
INSERT INTO Car VALUES (10, 'AlphaTauri AT02', 10);
INSERT INTO Car VALUES (14, 'Alpine A521', 14);
INSERT INTO Car VALUES (31, 'Alpine A521', 31);
INSERT INTO Car VALUES (5, 'Aston Martin AMR21', 5);
INSERT INTO Car VALUES (18, 'Aston Martin AMR21', 18);
INSERT INTO Car VALUES (63, 'Williams FW43B', 63);
INSERT INTO Car VALUES (6, 'Williams FW43B', 6);
INSERT INTO Car VALUES (99, 'Alfa Romeo C41', 99);
INSERT INTO Car VALUES (7, 'Alfa Romeo C41', 7);
INSERT INTO Car VALUES (47, 'Haas VF-21', 47);
INSERT INTO Car VALUES (9, 'Haas VF-21', 9);

INSERT INTO CircuitLocation VALUES ('Sakhir', 'Bahrain');
INSERT INTO CircuitLocation VALUES ('Emilia-Romagna', 'Italy');
INSERT INTO CircuitLocation VALUES  ('Portimao', 'Portugal');
INSERT INTO CircuitLocation VALUES ('Catalonia', 'Spain');
INSERT INTO CircuitLocation VALUES ('Monte Carlo', 'Monaco');
INSERT INTO CircuitLocation VALUES ('Baku','Azerbaijan');
INSERT INTO CircuitLocation VALUES ('Marseille','France');
INSERT INTO CircuitLocation VALUES ('Spielberg', 'Austria');
INSERT INTO CircuitLocation VALUES ('Silverstone', 'Great Britain');
INSERT INTO CircuitLocation VALUES ('Mogyorod', 'Hungary');
INSERT INTO CircuitLocation VALUES ('Stavelot', 'Belgium');
INSERT INTO CircuitLocation VALUES ('Zandvoort', 'Netherlands');
INSERT INTO CircuitLocation VALUES ('Monza', 'Italy');
INSERT INTO CircuitLocation VALUES ('Sochi', 'Rusia');
INSERT INTO CircuitLocation VALUES ('Istanbul', 'Turkey');
INSERT INTO CircuitLocation VALUES ('Austin', 'United States of America');
INSERT INTO CircuitLocation VALUES ('Mexico City', 'Mexico');
INSERT INTO CircuitLocation VALUES ('Sao Paulo', 'Brazil');
INSERT INTO CircuitLocation VALUES ('Lusail', 'Qatar');
INSERT INTO CircuitLocation VALUES ('Jeddah', 'Saudi Arabia');
INSERT INTO CircuitLocation VALUES ('Abu Dhabi', 'United Arab Emirates');

INSERT INTO RaceDate VALUES ('2021-03-26', '2021-03-28');

INSERT INTO Circuit VALUES ('Bahrain International Circuit', 5412, 'Sakhir');

INSERT INTO FastestLap VALUES (211.566, '01:32:090', 'Bahrain Grand Prix', 77);

INSERT INTO Race VALUES ('Bahrain Grand Prix', 56, '2021-03-28', 211.566, 'Bahrain International Circuit');

INSERT INTO ConstructorCreatesCar VALUES ('Mercedes', 77, 77);
INSERT INTO ConstructorCreatesCar VALUES ('Mercedes', 44, 44);
INSERT INTO ConstructorCreatesCar VALUES ('Red Bull Racing Honda', 33, 33);
INSERT INTO ConstructorCreatesCar VALUES ('Red Bull Racing Honda', 11, 11);
INSERT INTO ConstructorCreatesCar VALUES ('McLaren Mercedes', 3, 3);
INSERT INTO ConstructorCreatesCar VALUES ('McLaren Mercedes', 4, 4);
INSERT INTO ConstructorCreatesCar VALUES ('Ferrari', 16, 16);
INSERT INTO ConstructorCreatesCar VALUES ('Ferrari', 55, 55);
INSERT INTO ConstructorCreatesCar VALUES ('AlphaTauri Honda', 22, 22);
INSERT INTO ConstructorCreatesCar VALUES ('AlphaTauri Honda', 10, 10);
INSERT INTO ConstructorCreatesCar VALUES ('Alpine Renault', 14, 14);
INSERT INTO ConstructorCreatesCar VALUES ('Alpine Renault', 31, 31);
INSERT INTO ConstructorCreatesCar VALUES ('Aston Martin Mercedes', 5, 5);
INSERT INTO ConstructorCreatesCar VALUES ('Aston Martin Mercedes', 18, 18);
INSERT INTO ConstructorCreatesCar VALUES ('Alfa Romeo Racing Ferrari', 99, 99);
INSERT INTO ConstructorCreatesCar VALUES ('Alfa Romeo Racing Ferrari', 7, 7);
INSERT INTO ConstructorCreatesCar VALUES ('Haas Ferrari', 47, 47);
INSERT INTO ConstructorCreatesCar VALUES ('Haas Ferrari', 9, 9);
INSERT INTO ConstructorCreatesCar VALUES ('Williams Mercedes', 6, 6);
INSERT INTO ConstructorCreatesCar VALUES ('Williams Mercedes', 63, 63);

INSERT INTO DriverRacesInCircuit VALUES (44, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (77, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (33, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (11, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (3, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (4, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (16, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (55, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (22, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (10, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (14, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (31, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (5, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (18, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (99, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (7, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (47, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (9, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (6, 'Bahrain International Circuit');
INSERT INTO DriverRacesInCircuit VALUES (63, 'Bahrain International Circuit');

INSERT INTO DrivePlacesInRace VALUES (44, 'Bahrain Grand Prix', 1);
INSERT INTO DrivePlacesInRace VALUES (33, 'Bahrain Grand Prix', 2);
INSERT INTO DrivePlacesInRace VALUES (77, 'Bahrain Grand Prix', 3);
INSERT INTO DrivePlacesInRace VALUES (4, 'Bahrain Grand Prix', 4);
INSERT INTO DrivePlacesInRace VALUES (11, 'Bahrain Grand Prix', 5);
INSERT INTO DrivePlacesInRace VALUES (16, 'Bahrain Grand Prix', 6);
INSERT INTO DrivePlacesInRace VALUES (3, 'Bahrain Grand Prix', 7);
INSERT INTO DrivePlacesInRace VALUES (55, 'Bahrain Grand Prix', 8);
INSERT INTO DrivePlacesInRace VALUES (22, 'Bahrain Grand Prix', 9);
INSERT INTO DrivePlacesInRace VALUES (18, 'Bahrain Grand Prix', 10);
INSERT INTO DrivePlacesInRace VALUES (7, 'Bahrain Grand Prix', 11);
INSERT INTO DrivePlacesInRace VALUES (99, 'Bahrain Grand Prix', 12);
INSERT INTO DrivePlacesInRace VALUES (31, 'Bahrain Grand Prix', 13);
INSERT INTO DrivePlacesInRace VALUES (63, 'Bahrain Grand Prix', 14);
INSERT INTO DrivePlacesInRace VALUES (5, 'Bahrain Grand Prix', 15);
INSERT INTO DrivePlacesInRace VALUES (47, 'Bahrain Grand Prix', 16);
INSERT INTO DrivePlacesInRace VALUES (10, 'Bahrain Grand Prix', 17);
INSERT INTO DrivePlacesInRace VALUES (6, 'Bahrain Grand Prix', 18);
INSERT INTO DrivePlacesInRace VALUES (14, 'Bahrain Grand Prix', 0);
INSERT INTO DrivePlacesInRace VALUES (9, 'Bahrain Grand Prix', 0);



