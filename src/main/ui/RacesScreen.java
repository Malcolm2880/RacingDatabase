package main.ui;

import main.model.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RacesScreen extends Screen {
    private TablePanel raceResultsTable;
    private JButton addRaceButton;
    private JButton deleteRaceButton;
    private JPanel buttonPanel;
    private HeaderPanel headerPanel;

    public RacesScreen() {
        super("Race Results");
        List<Race> raceResults = dbHandler.getRaceResults();
        setLayout(new BorderLayout());
        setResultsTable(raceResults);
        setButtons();

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

    private void setButtons() {

        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        Integer buttonSize = (d.width - r.width)/3;

        addRaceButton = new JButton("Add Race");
        addRaceButton.setPreferredSize(new Dimension(buttonSize, 100));
        addRaceButton.addActionListener(this);
        addRaceButton.setActionCommand("addRace");

        deleteRaceButton = new JButton("Delete Race");
        deleteRaceButton.setPreferredSize(new Dimension(buttonSize, 100));
        deleteRaceButton.addActionListener(this);
        deleteRaceButton.setActionCommand("deleteRace");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(addRaceButton, BorderLayout.LINE_START);
        buttonPanel.add(deleteRaceButton, BorderLayout.LINE_END);

        add(buttonPanel, BorderLayout.PAGE_END);
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
            case "deleteRace":
                // String driverNameToDelete = (String) flResultsTable.data[flResultsTable.table.getSelectedRow()][3];
                String raceNameToDelete =  (String) raceResultsTable.data[raceResultsTable.table.getSelectedRow()][0];
                String driverNameToDelete = dbHandler.getFastestLapDriver(raceNameToDelete);
                raceNameToDelete = raceNameToDelete.replaceAll("\\s", " ");
                driverNameToDelete = driverNameToDelete.replaceAll("\\s", " ");

                System.out.println(raceNameToDelete + " " +driverNameToDelete);
                dbHandler.deleteRace(raceNameToDelete);
                break;
        }
    }
}
