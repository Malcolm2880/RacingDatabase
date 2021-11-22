package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddResultScreen extends Screen{

    private JLabel driverLabel;
    private JComboBox<String> driverComboBox;
    private JLabel positionLabel;
    private JTextField positionField;
    private JButton addResultButton;
    private final String raceName;

    public AddResultScreen(String raceName) {
        super("Add Race Result");
        this.raceName = raceName;

        setExtendedState(JFrame.NORMAL);
        setSize(600, 300);

        driverLabel = new JLabel();
        List<String> drivers = dbHandler.getDriverNames();
        driverComboBox = new JComboBox<>(drivers.toArray(new String[drivers.size()]));
        setLabel(driverLabel, "Driver Name", 0,0);
        setDriverComboBox(driverComboBox, 1,0);

        positionLabel = new JLabel();
        positionField = new JTextField();
        setLabel(positionLabel, "Position in Race", 0,1);
        setTextField(positionField, "Position in Race", 1,1);

        setButton(addResultButton, "Add Result","addResult",0,2);
    }

    private void setDriverComboBox(JComboBox<String> driverComboBox, Integer xCoord, Integer yCoord) {
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add(driverComboBox, constraints);
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
        if (e.getActionCommand().equals("addResult")) {
            dbHandler.insertRaceResult(raceName, (String) driverComboBox.getSelectedItem(),
                                       Integer.valueOf(positionField.getText()));
        }

    }
}
