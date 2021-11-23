package main.ui;

import main.model.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RaceScreen extends Screen {
    private TablePanel raceResultTable;
    private JButton addRaceResultButton;
    private JButton updateRaceResultButton;
    private JButton deleteRaceResultButton;
    private JPanel buttonPanel;
    private final String raceName;

    public RaceScreen(String title) {
        super(title);
        this.raceName =  title;

        List<Driver> raceResult = dbHandler.getRaceResult(title);
        setLayout(new BorderLayout());
        setResultTable(raceResult);
        setButtons();
        HeaderPanel headerPanel = new HeaderPanel(title, false);
        add(headerPanel, BorderLayout.PAGE_START);
    }

    private void setResultTable(List<Driver> raceResult) {
        Object[][] data = getResultDataForTable(raceResult);
        raceResultTable = new RaceTablePanel(data);
        add(raceResultTable, BorderLayout.CENTER);
    }

    private Object[][] getResultDataForTable(List<Driver> raceResult) {
        Object[][] data = new Object[raceResult.size()][];

        int i = 0;
        int last = raceResult.size() - 1;
        for (Driver next: raceResult) {
            Object[] obj = new Object[5];
            obj[0] = next.getPosition();
            obj[1] = next.getNumber();
            obj[2] = next.getName();
            obj[3] = next.getConstructorName();
            obj[4] = next.getPoints();

            if (next.getPosition() == 0) {
                obj[0] = "DNF";
                data[last--] = obj;
                continue;
            }

            data[i++] = obj;
        }
        return data;
    }

    private void setButtons() {

        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        Integer buttonSize = (d.width - r.width)/3;

        addRaceResultButton = new JButton("Add Result");
        addRaceResultButton.setPreferredSize(new Dimension(buttonSize, 100));
        addRaceResultButton.addActionListener(this);
        addRaceResultButton.setActionCommand("addResult");

        updateRaceResultButton = new JButton("Update Result");
        updateRaceResultButton.setPreferredSize(new Dimension(buttonSize, 100));
        updateRaceResultButton.addActionListener(this);
        updateRaceResultButton.setActionCommand("updateResult");

        deleteRaceResultButton = new JButton("Delete Result");
        deleteRaceResultButton.setPreferredSize(new Dimension(buttonSize, 100));
        deleteRaceResultButton.addActionListener(this);
        deleteRaceResultButton.setActionCommand("deleteResult");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(addRaceResultButton, BorderLayout.LINE_START);
        buttonPanel.add(updateRaceResultButton, BorderLayout.CENTER);
        buttonPanel.add(deleteRaceResultButton, BorderLayout.LINE_END);

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "addResult":
                AddResultScreen addResultScreen = new AddResultScreen(raceName, false, null);
                addResultScreen.setVisible(true);
                break;
            case "updateResult":
                String driverName = (String) raceResultTable.data[raceResultTable.table.getSelectedRow()][2];
                AddResultScreen updateResultScreen = new AddResultScreen(raceName, true, driverName);
                updateResultScreen.setVisible(true);
                break;
            case "deleteResult":
                String driverNameToDelete = (String) raceResultTable.data[raceResultTable.table.getSelectedRow()][2];
                dbHandler.deleteResult(raceName, driverNameToDelete);
                break;
        }

    }
}
