package main.database;

import main.model.Constructor;
import main.model.Driver;
import main.model.Race;
import main.util.PrintablePreparedStatement;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";

    private static DatabaseConnectionHandler instance;
    private Connection connection = null;

    private DatabaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            login("ora_kbarutcu", "a25056623");
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public static DatabaseConnectionHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionHandler();
        }
        return instance;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public List<Race> getRaceResults() {
        List<Race> races = new ArrayList<>();

        try {
            String query = "SELECT * " +
                           "FROM RACE, DRIVER, DRIVEPLACESINRACE " +
                           "WHERE RACE.RACENAME = DRIVEPLACESINRACE.RACENAME AND DRIVEPLACESINRACE.RANK = 1 " +
                           "AND DRIVER.DRIVERNUMBER = DRIVEPLACESINRACE.DRIVERNUMBER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Race model = new Race(rs.getString("RaceName"), rs.getInt("Laps"),
                                      rs.getDate("EndDate"), rs.getFloat("FastestLapAverageSpeed"),
                                      rs.getString("CircuitName"), rs.getString("DriverName"),
                                      rs.getString("ConstructorName"));
                races.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return races;
    }

    public List<Constructor> getConstructorResults() {
        List<Constructor> constructors = new ArrayList<>();

        try {
            String query = "SELECT * " +
                    "FROM CONSTRUCTOR ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Constructor model = new Constructor(rs.getString("ConstructorName"), rs.getInt("Position"), rs.getFloat("Points"));

                constructors.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return constructors;
    }


    public List<Driver> getRaceResult(String name) {
        List<Driver> result = new ArrayList<>();

        try {
            String fastestLapDriverName = getFastestLapDriver(name);
            String query = "SELECT DRIVER.DRIVERNAME, DRIVER.DRIVERNUMBER, DRIVER.DRIVERAGE, DRIVER.CONSTRUCTORNAME,  " +
                            "DRIVEPLACESINRACE.RANK "+
                            "FROM DRIVER, DRIVEPLACESINRACE " +
                            "WHERE DRIVEPLACESINRACE.RACENAME = ? " +
                            "AND DRIVEPLACESINRACE.DRIVERNUMBER = DRIVER.DRIVERNUMBER " +
                            "ORDER BY DRIVEPLACESINRACE.RANK ASC ";

                    PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Boolean isFastestLap = rs.getString("DriverName").equals(fastestLapDriverName);
                Double racePoints = calculateRacePoints(rs.getInt("Rank"), isFastestLap);

                Driver model = new Driver(rs.getString("DriverName"), rs.getInt("DriverNumber"),
                                          rs.getInt("DriverAge"), racePoints,rs.getInt("Rank"),
                                          rs.getString("ConstructorName"), isFastestLap);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    private Double calculateRacePoints(Integer rank, Boolean isFastestLap) {
        if (rank > 10) {
            return isFastestLap ? 1.0 : 0.0;
        }

        if (rank == 0) {
            return 0.0;
        }

        Double points;
        switch (rank) {
            case 1:
                points = 25.0;
                break;
            case 2:
                points = 18.0;
                break;
            case 3:
                points = 15.0;
                break;
            case 10:
                points = 1.0;
                break;
            default:
                points = 12.0 - ((rank - 4.0) * 2.0);
                break;
        }

        return isFastestLap ? points + 1.0 : points;
    }

    private String getFastestLapDriver(String raceName) {
        String driverName = "";

        try {
            String query = "SELECT DRIVER.DRIVERNAME " +
                           "FROM DRIVER, FASTESTLAP "+
                           "WHERE FASTESTLAP.RACENAME = ?" + " AND DRIVER.DRIVERNUMBER = FASTESTLAP.DRIVERNUMBER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, raceName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                driverName = rs.getString("DriverName");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return driverName;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    public List<String> getRaceCities() {
        List<String> cities = new ArrayList<>();

        try {
            String query = "SELECT CITY FROM CIRCUITLOCATION";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                cities.add(rs.getString("City"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return cities;
    }

    public List<String> getDriverNames() {
        List<String> names = new ArrayList<>();

        try {
            String query = "SELECT DRIVERNAME FROM DRIVER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                names.add(rs.getString("DriverName"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return names;
    }

    public void insertRace(String raceName, String practiceDate, String raceDate, String city, String circuitName,
                           Integer numOfLaps, Integer circuitLength, Float fastestLapAverageSpeed, String fastestLapTime,
                           Integer fastestLapDriverNo) throws ParseException {

        try {
            insertRaceDate(practiceDate, raceDate);
            insertCircuit(circuitName, circuitLength, city);

            String query = "INSERT INTO RACE VALUES (?,?,?,?,?)";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, raceName);
            ps.setInt(2, numOfLaps);
            ps.setDate(3, Date.valueOf(raceDate));
            ps.setNull(4, Types.FLOAT);
            ps.setString(5, circuitName);

            ps.executeUpdate();
            connection.commit();

            ps.close();
            insertFastestLap(fastestLapAverageSpeed, fastestLapTime, fastestLapDriverNo, raceName);
            updateFastestLap(raceName, fastestLapAverageSpeed);

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void updateFastestLap(String raceName, Float fastestLapAverageSpeed) {
        try {
            String query = "UPDATE RACE SET FASTESTLAPAVERAGESPEED = ? WHERE RACENAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setFloat(1, fastestLapAverageSpeed);
            ps.setString(2, raceName);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    private void insertFastestLap(Float averageSpeed, String lapTime, Integer driverNo, String raceName) {
        try{
            String query = "INSERT INTO FASTESTLAP VALUES (?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ps.setFloat(1, averageSpeed);
            ps.setString(2, lapTime);
            ps.setString(3, raceName);
            ps.setInt(4, driverNo);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertCircuit(String circuitName, Integer circuitLength, String city) {
        try {
            String query = "INSERT INTO CIRCUIT VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, circuitName);
            ps.setInt(2, circuitLength);
            ps.setString(3, city);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertRaceDate(String practiceDate, String raceDate) {
        try {
            String query = "INSERT INTO RACEDATE VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDate(1, Date.valueOf(practiceDate));
            ps.setDate(2, Date.valueOf(raceDate));

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertRaceResult(String raceName, String driverName, Integer rank) {
        try {
            Integer driverNo = getDriverNumber(driverName);
            inserDriverToRace(driverNo, raceName);

            String query = "INSERT INTO DRIVEPLACESINRACE VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, driverNo);
            ps.setString(2, raceName);
            ps.setInt(3, rank);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void inserDriverToRace(Integer driverNo, String raceName) {
        try {
            String query = "INSERT INTO DRIVERRACESINCIRCUIT VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, driverNo);
            ps.setString(2, raceName);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private Integer getDriverNumber(String driverName) {
        Integer driverNo = 0;

        try {
            String query = "SELECT DRIVER.DRIVERNUMBER FROM DRIVER WHERE DRIVERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, driverName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                driverNo = rs.getInt("DriverNumber");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return driverNo;
    }
}
