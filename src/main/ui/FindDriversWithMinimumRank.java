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
        super("Find Drivers With Minimum Rank in All Races");
        setExtendedState(JFrame.NORMAL);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setSize(600, 800);

        infoLabel = new JLabel("Enter a rank to see the drivers that achieved at least that rank in every race.");
        add(infoLabel);

        minRankInAllRacesField = new JTextField(2);
        minRankInAllRacesField.setMinimumSize(new Dimension(300, 20));
        add(minRankInAllRacesField);

        resultButton = new JButton("See Results");
        resultButton.addActionListener(this);
        resultButton.setActionCommand("seeResults");
        add(resultButton);

        Object[][] data = new Object[0][];
        results = new DriversTablePanel(data);
        add(results);
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
            this.revalidate();
            this.repaint();
            List<Driver> resultDrivers = dbHandler.getAllDriversWithMinimumRank(Integer.parseInt(minRankInAllRacesField.getText()));
            setResultsTable(resultDrivers);
            this.revalidate();
            this.repaint();
        }

    }


}
