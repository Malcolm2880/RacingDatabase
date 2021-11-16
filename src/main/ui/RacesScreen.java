package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RacesScreen extends Screen {
    private JLabel screenLabel;
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
    private JTable resultsTable;

    public RacesScreen() {
        super("Race Results");
        setScreenLabel();
        setResultsTable();
    }

    private void setScreenLabel() {
        screenLabel = new JLabel("Race Results");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(screenLabel, constraints);
    }

    private void setResultsTable() {
        resultsTable = new JTable(data, columnNames);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        add(resultsTable.getTableHeader(), constraints);

        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.BOTH;
        add(resultsTable, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
