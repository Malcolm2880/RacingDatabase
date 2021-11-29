package main.ui;

import main.model.Constructor;
import main.model.Driver;
import main.model.FastestLap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class OverviewScreen extends Screen {
    private TablePanel podiumDriverTable; //pos, driver, constructor, pts for top 3
    private TablePanel podiumConstructorTable; //pos, name, drivers, pts for top 3
    private TablePanel fastestSeasonLapTable;
    private JLabel minFastestLap; // fastest fastest lap
    private JButton filterDriversByPoints; //which drivers got over >x points every race? - first divide drivers/races, then condition points

    private HeaderPanel headerPanel;

    public OverviewScreen() {
        super("Overview of the Season 2021");
        List<Driver> driverStandings = dbHandler.getDriverStandings().subList(0, 3);
        List<Constructor> constructorStandings = dbHandler.getConstructorResults().subList(0, 3);
        FastestLap fastestLapOfSeason = dbHandler.getFastestLapOfSeason();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        headerPanel = new HeaderPanel("Overview of the Season 2021", true);
        setUpdateAction();
        add(headerPanel);

        JLabel driverPodiumLabel = new JLabel("Driver Podium");
        driverPodiumLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        add(driverPodiumLabel);
        setPodiumDriverTable(driverStandings);

        JLabel constructorPodiumLabel = new JLabel("Constructor Podium");
        constructorPodiumLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        add(constructorPodiumLabel);
        setPodiumConstructorTable(constructorStandings);

        JLabel fastestLapLabel = new JLabel("Fastest Lap of the Season");
        fastestLapLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        add(fastestLapLabel);

        setFastestSeasonLapTable(fastestLapOfSeason);

        filterDriversByPoints = new JButton("Find Drivers With Minimum Score In All Races");
        filterDriversByPoints.addActionListener(this);
        filterDriversByPoints.setActionCommand("filterDrivers");
        filterDriversByPoints.setPreferredSize(new Dimension(80, 120));
        add(filterDriversByPoints);
    }

    private Object[][] getDriverDataForTable(List<Driver> driverStandings) {
        Object[][] data = new Object[driverStandings.size()][];

        int i = 0;
        for (Driver next : driverStandings) {
            Object[] obj = new Object[5];
            obj[0] = next.getName();
            obj[1] = next.getNumber();
            obj[2] = next.getAge();
            obj[3] = next.getPoints();
            obj[4] = next.getConstructorName();
            data[i++] = obj;
        }
        return data;
    };

    private Object[][] getConstructorDataForTable(List<Constructor> constructorStandings) {
        Object[][] data = new Object[constructorStandings.size()][];

        int i = 0;
        for (Constructor next : constructorStandings) {
            Object[] obj = new Object[3];
            obj[0] = next.getName();
            obj[1] = next.getPosition();
            obj[2] = next.getPoints();
            data[i++] = obj;
        }
        return data;
    }

    private void setPodiumDriverTable(List<Driver> driverStandings) {
        Object[][] data = getDriverDataForTable(driverStandings);
        podiumDriverTable = new DriversTablePanel(data);
        add(podiumDriverTable);
    }

    private void setPodiumConstructorTable(List<Constructor> constructorStandings) {
        Object[][] data = getConstructorDataForTable(constructorStandings);
        podiumConstructorTable = new ConstructorsTablePanel(data);
        add(podiumConstructorTable);
    }

    private void setFastestSeasonLapTable(FastestLap fastestLap) {
        Object[][] data = new Object[1][];
        Object[] obj = new Object[4];
        obj[0] = fastestLap.getAverageSpeed();
        obj[1] = fastestLap.getLapTime();
        obj[2] = fastestLap.getRaceName();
        obj[3] = fastestLap.getDriverNumber();
        data[0] = obj;

        fastestSeasonLapTable = new FastestLapTablePanel(data);
        add(fastestSeasonLapTable);
    }


    private void setUpdateAction() {
        headerPanel.updateButton.addActionListener(this);
        headerPanel.updateButton.setActionCommand("update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "update":
                List<Driver> driverStandings = dbHandler.getDriverStandings();
                List<Constructor> constructorStandings = dbHandler.getConstructorResults();
                remove(podiumDriverTable);
                remove(podiumConstructorTable);
                this.revalidate();
                this.repaint();
                setPodiumDriverTable(driverStandings);
                setPodiumConstructorTable(constructorStandings);
                this.revalidate();
                this.repaint();
                break;
            case "filterDrivers":
                FindDriversWithMinimumRank findDrivers = new FindDriversWithMinimumRank();
                findDrivers.setVisible(true);
                break;
        }
    }
}
