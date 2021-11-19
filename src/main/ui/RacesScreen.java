package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RacesScreen extends Screen {
    private static final String[] columnNames = {"Grand Prix",
                                                 "Date", "Winner (Driver)", "Winner (Constructor)", "Laps"};
    Object[][] data = {
            {"Kathy", "Smith",
                    "Snowboarding", new Integer(5), new Boolean(false)},
            {"John", "Doe",
                    "Rowing", new Integer(3), new Boolean(true)},
            {"Sue", "Black",
                    "Knitting", new Integer(2), new Boolean(false)},
            {"Jane", "White",
                    "Speed reading", new Integer(20), new Boolean(true)},
            {"Joe", "Brown",
                    "Pool", new Integer(10), new Boolean(false)}
    };

    private TablePanel raceResultsTable;
    private JButton addRaceButton;

    public RacesScreen() {
        super("Race Results");
        setLayout(new BorderLayout());
        setResultsTable();
        setAddRaceButton();
        HeaderPanel panel = new HeaderPanel();
        add(panel, BorderLayout.PAGE_START);
    }


    private void setResultsTable() {
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
