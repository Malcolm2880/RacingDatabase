package main.database;

import main.model.*;
import main.model.Driver;
import main.util.PrintablePreparedStatement;

import java.sql.*;
import java.text.ParseException;
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

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public List<Race> getRaceResults() {
        List<Race> races = new ArrayList<>();

        try {
            String query = "SELECT * FROM RACE";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Driver winnerDriver = getHighestRankInRace(rs.getString("RaceName"));

                String winnerDriverName = winnerDriver == null ? "" : winnerDriver.getName();
                String winnerConstructor = winnerDriver == null ? "" : winnerDriver.getConstructorName();

                Race model = new Race(rs.getString("RaceName"), rs.getInt("Laps"),
                        rs.getDate("EndDate"), rs.getFloat("FastestLapAverageSpeed"),
                        rs.getString("CircuitName"), winnerDriverName, winnerConstructor);
                races.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return races;
    }

    public List<Driver> getDriverStandings() {
        List<Driver> drivers = new ArrayList<>();

        try {
            String query = "SELECT * FROM DRIVER ORDER BY DRIVERPOINTS DESC";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Driver model = new Driver(rs.getString("DriverName"), rs.getInt("DriverNumber"),
                        rs.getInt("DriverAge"), rs.getDouble("DriverPoints"), 0,
                        rs.getString("ConstructorName"), false);
                // TODO: write a query to check if a driver drove a fastest lap - present in fastest lap table?
                drivers.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return drivers;
    }

    public List<ConstructorRace> getConstructorRaceResults(String name) {
        List<ConstructorRace> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM DRIVEPLACESINRACE, RACE, DRIVER WHERE DRIVEPLACESINRACE.RACENAME = RACE.RACENAME AND DRIVER.DRIVERNUMBER = DRIVEPLACESINRACE.DRIVERNUMBER AND DRIVER.CONSTRUCTORNAME = ? ";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                String fastestLapDriverName = getFastestLapDriver(rs.getString("DriverName"));

                Boolean isFastestLap = rs.getString("DriverName").equals(fastestLapDriverName);


                double points = calculateRacePoints(rs.getInt("Rank"),isFastestLap);
              ConstructorRace model = new ConstructorRace(rs.getString("RaceName"),rs.getDate("EndDate"),points);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }




    public List<Constructor> getConstructorResults() {
        List<Constructor> constructors = new ArrayList<>();

        try {
            String query = "SELECT * " +
                    "FROM CONSTRUCTOR ORDER BY POINTS DESC ";
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

    public List<FastestLap> getFastestLapResults() {
        List<FastestLap> laps = new ArrayList<>();

        try {
            String query = "SELECT * " +
                    "FROM FASTESTLAP ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                FastestLap model = new FastestLap(rs.getFloat("AverageSpeed"), rs.getString("LapTime"), rs.getString("RaceName"), rs.getInt("DriverNumber"));

                laps.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return laps;
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

    private Driver getHighestRankInRace(String raceName) {
        Driver result = null;
        createTempViewForHighestRankDrivers();

        try {
            @SuppressWarnings("SqlResolve")
            String query = "SELECT DRIVEPLACESINRACE.RACENAME, DRIVER.DRIVERNAME, DRIVER.DRIVERNUMBER, DRIVER.DRIVERAGE, " +
                           "DRIVER.CONSTRUCTORNAME, DRIVEPLACESINRACE.RANK "+
                           "FROM DRIVEPLACESINRACE, TEMP, DRIVER "+
                           "WHERE TEMP.minRank = DRIVEPLACESINRACE.RANK AND TEMP.raceName = DRIVEPLACESINRACE.RACENAME " +
                           "AND DRIVEPLACESINRACE.DRIVERNUMBER = DRIVER.DRIVERNUMBER";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if (rs.getString("RaceName").equals(raceName)) {
                    String fastestLapDriverName = getFastestLapDriver(rs.getString("RaceName"));
                    Boolean isFastestLap = rs.getString("DriverName").equals(fastestLapDriverName);
                    Double racePoints = calculateRacePoints(rs.getInt("Rank"), isFastestLap);

                    Driver model = new Driver(rs.getString("DriverName"), rs.getInt("DriverNumber"),
                            rs.getInt("DriverAge"), racePoints,rs.getInt("Rank"),
                            rs.getString("ConstructorName"), isFastestLap);
                    result = model;
                }
            }

            rs.close();
            ps.close();
            dropTempView();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    private void dropTempView() {
        try {
            @SuppressWarnings("SqlResolve")
            String query = "DROP VIEW TEMP";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createTempViewForHighestRankDrivers() {
        try {
            String query = "CREATE VIEW TEMP(raceName, minRank) AS " +
                           "SELECT RACENAME, MIN(RANK) as minRank " +
                           "FROM DRIVEPLACESINRACE " +
                           "WHERE RANK != 0 " +
                           "GROUP BY RACENAME HAVING COUNT(*) > 1";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
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

    private Integer getDriverRankForRace(String raceName, String driverName) {
        Integer rank = 0;
        Integer driverNo = getDriverNumber(driverName);

        try {
            String query = "SELECT RANK " +
                           "FROM DRIVEPLACESINRACE "+
                           "WHERE RACENAME = ?" + " AND DRIVERNUMBER = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, raceName);
            ps.setInt(2, driverNo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                rank = rs.getInt("Rank");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return rank;
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
                           String fastestLapDriverName) throws ParseException {

        try {
            insertRaceDate(practiceDate, raceDate);
            insertCircuit(circuitName, circuitLength, city);
            Integer fastestLapDriverNo = getDriverNumber(fastestLapDriverName);

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
            boolean isFastestLap = driverName.equals(getFastestLapDriver(raceName));
            updateConstructorScore(driverName, rank, isFastestLap);

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

    private String getConstructorForDriver(String driverName) {
        String constructorName = "";

        try {
            String query = "SELECT DRIVER.CONSTRUCTORNAME FROM DRIVER WHERE DRIVERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, driverName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                constructorName = rs.getString("ConstructorName");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return constructorName;
    }

    private Double getConstructorPoints(String constructor) {
        Double points = 0.0;

        try {
            String query = "SELECT POINTS FROM CONSTRUCTOR WHERE CONSTRUCTORNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, constructor);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                points = rs.getDouble("Points");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return points;
    }

    private void updateConstructorScore(String driverName, Integer rank, boolean isFastestLap) {
        String constructor = getConstructorForDriver(driverName);
        Double points = calculateRacePoints(rank, isFastestLap) + getConstructorPoints(constructor);

        try {
            String query = "UPDATE CONSTRUCTOR SET POINTS = ? WHERE CONSTRUCTORNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDouble(1, points);
            ps.setString(2, constructor);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void updateConstructorScoreForUpdate(String driverName, String raceName, Integer rank, boolean isFastestLap) {
        String constructor = getConstructorForDriver(driverName);
        Integer driverExistingRank = getDriverRankForRace(raceName, driverName);

        Double driverExistingPoints = calculateRacePoints(driverExistingRank, isFastestLap);
        Double newPoints = calculateRacePoints(rank, isFastestLap);

        Double pointDifference = newPoints - driverExistingPoints;
        Double constructorPoints = getConstructorPoints(constructor);

        try {
            String query = "UPDATE CONSTRUCTOR SET POINTS = ? WHERE CONSTRUCTORNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDouble(1, constructorPoints + pointDifference);
            ps.setString(2, constructor);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    private void updateConstructorScoreForDeletion(String driverName, String raceName, boolean isFastestLap) {
        String constructor = getConstructorForDriver(driverName);
        Integer driverExistingRank = getDriverRankForRace(raceName, driverName);

        Double driverExistingPoints = calculateRacePoints(driverExistingRank, isFastestLap);
        Double constructorPoints = getConstructorPoints(constructor);

        try {
            String query = "UPDATE CONSTRUCTOR SET POINTS = ? WHERE CONSTRUCTORNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDouble(1, constructorPoints - driverExistingPoints);
            ps.setString(2, constructor);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateRaceResult(String raceName, String driverName, Integer rank) {
        boolean isFastestLap = getFastestLapDriver(raceName).equals(driverName);
        updateConstructorScoreForUpdate(driverName, raceName, rank, isFastestLap);

        try {
            Integer driverNo = getDriverNumber(driverName);

            String query = "UPDATE DRIVEPLACESINRACE SET RANK = ? WHERE RACENAME = ? AND DRIVERNUMBER = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, rank);
            ps.setString(2, raceName);
            ps.setInt(3, driverNo);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteResult(String raceName, String driverName) {
        boolean isFastestLap = getFastestLapDriver(raceName).equals(driverName);
        updateConstructorScoreForDeletion(driverName, raceName, isFastestLap);
        try {
            Integer driverNo = getDriverNumber(driverName);
            String query = "DELETE FROM DRIVEPLACESINRACE WHERE RACENAME = ? AND DRIVERNUMBER = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, raceName);
            ps.setInt(2, driverNo);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }
}
