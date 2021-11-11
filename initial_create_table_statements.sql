CREATE TABLE Race
(
    RaceName               CHAR(75),
    Laps                   INTEGER,
    EndDate                DATE,
    FastestLapAverageSpeed FLOAT    NOT NULL,
    CircuitName            CHAR(75) NOT NULL,
    PRIMARY KEY (RaceName)
        FOREIGN KEY (EndDate)
        REFERENCES RaceDate
        ON UPDATE CASCADE
        FOREIGN KEY (FastestLapAverageSpeed)
        REFERENCES FastestLap
        ON DELETE CASCADE
        FOREIGN KEY (CircuitName)
        REFERENCES Circuit
        ON DELETE CASCADE
)

CREATE TABLE RaceDate
(
    StartDate DATE,
    EndDate   DATE,
    PRIMARY KEY (EndDate)
)

CREATE TABLE Circuit
(
    CircuitName CHAR(75),
    Length      INTEGER,
    City        CHAR(30) NOT NULL,
    PRIMARY KEY (CircuitName)
        FOREIGN KEY (City) REFERENCES CircuitLocation
        ON DELETE CASCADE
)
CREATE TABLE CircuitLocation
(
    City    CHAR(30),
    Country CHAR(30)
        PRIMARY key(City)
)

CREATE TABLE FastestLap
(
    AverageSpeed FLOAT,
    LapTime      TIME,
    RaceName     CHAR(75) NOT NULL,
    DriverNumber INTEGER  NOT NULL,
    PRIMARY KEY (AverageSpeed, RaceName),
    FOREIGN KEY (DriverNumber) REFERENCES Driver
        ON DELETE CASCADE
        ON UPDATE CASCADE
        FOREIGN KEY (RaceName)
        REFERENCES Race
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
CREATE TABLE Constructor
(
    ConstructorName CHAR(8),
    Position        INTEGER,
    Points          INTEGER,
    PRIMARY KEY (ConstructorName)
)

CREATE TABLE Engine
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    PRIMARY KEY (ModelLine, Manufacturer)
)

CREATE TABLE Chassis
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    PRIMARY KEY (ModelLine, Manufacturer)
)

CREATE TABLE ChassisCar
(
    ModelLine    CHAR(8),
    Manufacturer CHAR(10),
    CarNumber    INTEGER,
    DriverNumber INTEGER,
    PRIMARY KEY (ModelLine, Manufacturer, CarNumber, DriverNumber),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Chassis
            ON DELETE CASCADE,
    FOREIGN KEY (CarNumber, DriverNumber)
        REFERENCES Car
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
);


CREATE TABLE EngineConsDesigns
(
    ModelLine       CHAR(8),
    Manufacturer    CHAR(10),
    ConstructorName CHAR(8),
    PRIMARY KEY (ModelLine, Manufacturer, ConstructorName),
    FOREIGN KEY (ModelLine, Manufacturer)
        REFERENCES Engine
            ON DELETE CASCADE,
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor
            ON DELETE CASCADE
);

CREATE TABLE ConstructorCreatesCar
(
    ConstructorName CHAR(8),
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

CREATE TABLE Driver
(
    DriverNumber    INTEGER,
    DriverName      CHAR(10),
    DriverAge       INTEGER,
    DriverPoints    FLOAT,
    ConstructorName CHAR(8) NOT NULL
        PRIMARY KEY(DriverNumber),
    FOREIGN KEY (ConstructorName)
        REFERENCES Constructor
        ON UPDATE CASCADE
)


CREATE TABLE Car
(
    CarNumber    INTEGER,
    CarName      CHAR(8),
    DriverNumber INTEGER,
    PRIMARY KEY (CarNumber, DriverNumber),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver
            ON DELETE CASCADE
)

CREATE TABLE DriverRacesInCircuit
(
    DriverNumber INTEGER,
    CircuitName  CHAR(75),
    PRIMARY KEY (DriverNumber, CircuitName),
    FOREIGN KEY (DriverNumber)
        REFERENCES Driver
            ON DELETE CASCADE
        FOREIGN KEY (CircuitName)
        REFERENCES Circuit
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

