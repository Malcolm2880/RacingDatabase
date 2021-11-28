package main.ui;

import main.model.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DriversScreen extends Screen {
    private TablePanel driverStandingsTable;
    private JButton addRaceButton; //drivers can have points added/removed for penalty
    private HeaderPanel headerPanel;

    public DriversScreen() {
        super("Driver Standings");
        List<Driver> driverStandings = dbHandler.getDriverStandings();
        setLayout(new BorderLayout());
        setStandingsTable(driverStandings);
        //setAddRaceButton();
        headerPanel = new HeaderPanel("Driver Standings", true);
        setUpdateAction();
        add(headerPanel, BorderLayout.PAGE_START);
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

    private void setStandingsTable(List<Driver> driverStandings) {
        Object[][] data = getDriverDataForTable(driverStandings);
        driverStandingsTable = new DriversTablePanel(data); //TODO: make this class
        add(driverStandingsTable, BorderLayout.CENTER);
    }

    /*
    private void setAddRaceButton() {
        addRaceButton = new JButton("Add Race");
        addRaceButton.setPreferredSize(new Dimension(60, 100));
        addRaceButton.setActionCommand("addRace");
        addRaceButton.addActionListener(this);
        add(addRaceButton, BorderLayout.PAGE_END);
    }*/

    private void setUpdateAction() {
        headerPanel.updateButton.addActionListener(this);
        headerPanel.updateButton.setActionCommand("update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "update":
                List<Driver> driverStandings = dbHandler.getDriverStandings();
                remove(driverStandingsTable);
                this.revalidate();
                this.repaint();
                setStandingsTable(driverStandings);
                this.revalidate();
                this.repaint();
                break;
        }
    }
}
