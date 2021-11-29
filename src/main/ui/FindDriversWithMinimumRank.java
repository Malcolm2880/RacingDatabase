package main.ui;

import main.model.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FindDriversWithMinimumRank extends Screen {

    private JLabel infoLabel;
    private JTextField minRankInAllRacesField;

    private JButton resultButton;
    private TablePanel results;

    public FindDriversWithMinimumRank() {
        super("Find Drivers With Minimum Score in All Races");
        setExtendedState(JFrame.NORMAL);
        setSize(600, 800);

        infoLabel = new JLabel();
        setLabel(infoLabel, "Enter a score to see the drivers that achieved at least that rank in every race.", 0, 1);
        minRankInAllRacesField = new JTextField();
        setTextField(minRankInAllRacesField, "0", 1,1);

        setButton(resultButton, "See results","seeResults",0,2);

        Object[][] data = new Object[1][];
        results = new DriversTablePanel(data);
        add(results);
    }

    private void setLabel(JLabel label, String labelText, Integer xCoord, Integer yCoord) {
        label.setText(labelText);
        label.setPreferredSize(new Dimension(200, 30));
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add(label, constraints);
    }

    private void setTextField(JTextField textField, String placeholder, Integer xCoord, Integer yCoord) {
        textField.setText(placeholder);
        textField.setForeground(Color.gray);
        textField.setPreferredSize(new Dimension(200, 35));
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add(textField, constraints);
    }

    private Object[][] getResultsDataForTable(List<Driver> resultDrivers) {
        Object[][] data = new Object[resultDrivers.size()][];

        int i = 0;
        for (Driver next : resultDrivers) {
            Object[] obj = new Object[5];
            obj[0] = next.getName();
            obj[1] = next.getNumber();
            obj[2] = next.getAge();
            obj[3] = next.getPoints();
            obj[4] = next.getConstructorName();
            data[i++] = obj;
        }
        return data;
    }

    private void setResultsTable(List<Driver> resultDrivers) {
        Object[][] data = getResultsDataForTable(resultDrivers);
        results = new DriversTablePanel(data);
        add(results);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("seeResults")) {
            remove(results);
            List<Driver> resultDrivers = dbHandler.getAllDriversWithMinimumRank(Integer.parseInt(minRankInAllRacesField.getText()));
            setResultsTable(resultDrivers);
            this.revalidate();
            this.repaint();
        }

    }


}
