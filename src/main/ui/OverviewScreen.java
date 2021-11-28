package main.ui;

import main.model.Constructor;
import main.model.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class OverviewScreen extends Screen {
    private TablePanel podiumDriverTable; //pos, driver, constructor, pts for top 3
    private TablePanel podiumConstructorTable; //pos, name, drivers, pts for top 3
    private JLabel minFastestLap; // fastest fastest lap
    private JButton filterDriversByPoints; //which drivers got over >x points every race? - first divide drivers/races, then condition points

    private HeaderPanel headerPanel;

    public OverviewScreen() {
        super("Overview of the Season 2021");
        List<Driver> driverStandings = dbHandler.getDriverStandings().subList(0, 3);
        List<Constructor> constructorStandings = dbHandler.getConstructorResults().subList(0, 3);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        headerPanel = new HeaderPanel("Overview of the Season 2021", true);
        setUpdateAction();
        add(headerPanel);

        add(new JLabel("Driver Podium"), BorderLayout.WEST);
        setPodiumDriverTable(driverStandings);
        add(new JLabel("Constructor Podium"), BorderLayout.WEST);
        setPodiumConstructorTable(constructorStandings);

        add(new JLabel("Fastest Lap of the Season"), BorderLayout.WEST);
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
    };

    private void setPodiumDriverTable(List<Driver> driverStandings) {
        Object[][] data = getDriverDataForTable(driverStandings);
        podiumDriverTable = new DriversTablePanel(data);
        podiumDriverTable.setPreferredSize(new Dimension(MAXIMIZED_HORIZ, 140));
        add(podiumDriverTable);
    }

    private void setPodiumConstructorTable(List<Constructor> constructorStandings) {
        Object[][] data = getConstructorDataForTable(constructorStandings);
        podiumConstructorTable = new ConstructorsTablePanel(data);
        add(podiumConstructorTable);
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
        }
    }
}
