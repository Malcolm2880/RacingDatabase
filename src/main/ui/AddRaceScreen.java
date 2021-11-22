package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.List;

public class AddRaceScreen extends Screen{
    JLabel grandPrixNameLabel;
    JTextField grandPrixNameField;

    JLabel practiceDateLabel;
    JTextField practiceDateField;

    JLabel raceDateLabel;
    JTextField raceDateField;

    JLabel cityLabel;
    JComboBox<String> cityComboBox;

    JLabel circuitNameLabel;
    JTextField circuitNameField;

    JLabel numberOfLapsLabel;
    JTextField numberOfLapsField;

    JLabel circuitLengthLabel;
    JTextField circuitLengthField;

    JLabel fastestLapDriverNoLabel;
    JTextField fastestLapDriverNoField;

    JLabel fastestLapTimeLabel;
    JTextField fastestLapTimeField;

    JLabel fastestLapAverageSpeedLabel;
    JTextField fastestLapAverageSpeedField;

    JButton addRaceButton;

    public AddRaceScreen() {
        super("Add Race");
        setExtendedState(JFrame.NORMAL);
        setSize(600,900);

        grandPrixNameLabel = new JLabel();
        grandPrixNameField = new JTextField();
        setLabel(grandPrixNameLabel, "Grand Prix Name", 0,0);
        setTextField(grandPrixNameField, "Grand Prix Name", 0, 1);

        practiceDateLabel = new JLabel();
        practiceDateField = new JTextField();
        setLabel(practiceDateLabel, "Practice Date",0, 2);
        setTextField(practiceDateField, "YYYY-MM-DD",0, 3);

        raceDateLabel = new JLabel();
        raceDateField = new JTextField();
        setLabel(raceDateLabel, "Race Date",0, 4);
        setTextField(raceDateField, "YYYY-MM-DD",0, 5);

        cityLabel = new JLabel();
        cityComboBox = new JComboBox<>();
        setLabel(cityLabel, "Race City", 0,6);

        List<String> raceCities = dbHandler.getRaceCities();
        cityComboBox = new JComboBox<>(raceCities.toArray(new String[raceCities.size()]));
        setCityComboBox(cityComboBox, 0, 7);


        circuitNameLabel = new JLabel();
        circuitNameField = new JTextField();
        setLabel(circuitNameLabel, "Circuit Name", 0, 8);
        setTextField(circuitNameField, "Circuit Name", 0,9);

        numberOfLapsLabel = new JLabel();
        numberOfLapsField = new JTextField();
        setLabel(numberOfLapsLabel, "Number of Laps", 0,10);
        setTextField(numberOfLapsField, "Number of Laps", 0, 11);

        fastestLapDriverNoLabel = new JLabel();
        fastestLapDriverNoField = new JTextField();
        setLabel(fastestLapDriverNoLabel, "Number of the Fastest Lap Driver", 0,12);
        setTextField(fastestLapDriverNoField, "Number of the Fastest Lap Driver", 0, 13);

        fastestLapAverageSpeedLabel = new JLabel();
        fastestLapAverageSpeedField = new JTextField();
        setLabel(fastestLapAverageSpeedLabel, "Fastest Lap Average Speed",0, 14);
        setTextField(fastestLapAverageSpeedField, "Fastest Lap Average Speed", 0, 15);

        fastestLapTimeLabel = new JLabel();
        fastestLapTimeField = new JTextField();
        setLabel(fastestLapTimeLabel, "Fastest Lap Time",0, 16);
        setTextField(fastestLapTimeField, "MM:SS.FFF", 0, 17);

        circuitLengthLabel = new JLabel();
        circuitLengthField = new JTextField();
        setLabel(circuitLengthLabel, "Circuit Length", 0, 18);
        setTextField(circuitLengthField, "Circuit Length", 0, 19);

        setButton(addRaceButton, "Add Race", "addRace", 0, 20);
    }

    private void setCityComboBox(JComboBox<String> cityComboBox, Integer xCoord, Integer yCoord) {
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add(cityComboBox, constraints);
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


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addRace")) {
            try {
                dbHandler.insertRace(grandPrixNameField.getText(), practiceDateField.getText(), raceDateField.getText(),
                                     (String) cityComboBox.getSelectedItem(), circuitNameField.getText(),
                                     Integer.valueOf(numberOfLapsField.getText()), Integer.valueOf(circuitLengthField.getText()),
                                     Float.valueOf(fastestLapAverageSpeedField.getText()), fastestLapTimeField.getText(),
                                     Integer.valueOf(fastestLapDriverNoField.getText()));
                this.dispose();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }
}
