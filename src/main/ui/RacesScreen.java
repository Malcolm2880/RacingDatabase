package main.ui;

import main.model.Race;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

public class RacesScreen extends Screen {
    private static final String[] columnNames = {"Grand Prix", "Laps", "Date", "Fastest Lap Average Speed",
                                                 "Circuit Name", "Winner (Driver)", "Winner (Constructor)"};

    private TablePanel raceResultsTable;
    private JButton addRaceButton;

    public RacesScreen() {
        super("Race Results");
        List<Race> raceResults = dbHandler.getRaceResults();
        setLayout(new BorderLayout());
        setResultsTable(raceResults);
        setAddRaceButton();
        HeaderPanel panel = new HeaderPanel();
        add(panel, BorderLayout.PAGE_START);
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
        raceResultsTable = new TablePanel(data, columnNames);
        add(raceResultsTable, BorderLayout.CENTER);
    }

    private void setAddRaceButton() {
        addRaceButton = new JButton("Add Race");
        addRaceButton.setPreferredSize(new Dimension(60, 100));
        add(addRaceButton, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
