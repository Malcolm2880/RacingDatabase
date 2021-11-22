package main.ui;

import main.model.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RaceScreen extends Screen {
    private TablePanel raceResultTable;
    private JButton addRaceResult;
    private final String raceName;

    public RaceScreen(String title) {
        super(title);
        this.raceName =  title;

        List<Driver> raceResult = dbHandler.getRaceResult(title);
        setLayout(new BorderLayout());
        setResultTable(raceResult);
        setAddResultButton();
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

    private void setAddResultButton() {
        addRaceResult = new JButton("Add Result");
        addRaceResult.setPreferredSize(new Dimension(60, 100));
        addRaceResult.addActionListener(this);
        addRaceResult.setActionCommand("addResult");
        add(addRaceResult, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addResult")) {
            AddResultScreen addResultScreen = new AddResultScreen(raceName);
            addResultScreen.setVisible(true);
        }
    }
}
