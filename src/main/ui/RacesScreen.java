package main.ui;

import main.model.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RacesScreen extends Screen {
    private TablePanel raceResultsTable;
    private JButton addRaceButton;
    private HeaderPanel headerPanel;

    public RacesScreen() {
        super("Race Results");
        List<Race> raceResults = dbHandler.getRaceResults();
        setLayout(new BorderLayout());
        setResultsTable(raceResults);
        setAddRaceButton();
        headerPanel = new HeaderPanel("Race Results", true);
        setUpdateAction();
        add(headerPanel, BorderLayout.PAGE_START);
    }

    private Object[][] getRaceDataForTable(List<Race> raceResults) {
        Object[][] data = new Object[raceResults.size()][];

        int i = 0;
        for (Race next : raceResults) {
            Object[] obj = new Object[7];
            obj[0] = next.getName();
            obj[1] = next.getNumberOfLaps();
            obj[2] = next.getDate();
            obj[3] = next.getFastestLapAverageSpeed();
            obj[4] = next.getCircuitName();
            obj[5] = next.getWinnerDriver();
            obj[6] = next.getWinnerConstructor();
            data[i++] = obj;
        }
        return data;
    };

    private void setResultsTable(List<Race> raceResults) {
        Object[][] data = getRaceDataForTable(raceResults);
        raceResultsTable = new RacesTablePanel(data);
        add(raceResultsTable, BorderLayout.CENTER);
    }

    private void setAddRaceButton() {
        addRaceButton = new JButton("Add Race");
        addRaceButton.setPreferredSize(new Dimension(60, 100));
        addRaceButton.setActionCommand("addRace");
        addRaceButton.addActionListener(this);
        add(addRaceButton, BorderLayout.PAGE_END);
    }

    private void setUpdateAction() {
        headerPanel.updateButton.addActionListener(this);
        headerPanel.updateButton.setActionCommand("update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "addRace":
                AddRaceScreen addRaceScreen = new AddRaceScreen();
                addRaceScreen.setVisible(true);
                break;
            case "update":
                List<Race> raceResults = dbHandler.getRaceResults();
                remove(raceResultsTable);
                this.revalidate();
                this.repaint();
                setResultsTable(raceResults);
                this.revalidate();
                this.repaint();
                break;
        }
    }
}
