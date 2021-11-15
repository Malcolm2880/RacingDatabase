package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a home screen that contains the
// main menu and buttons to the other screens.
public class HomeScreen extends JFrame implements ActionListener {

    private static final Integer WIDTH = 750;
    private static final Integer HEIGHT = 250;

    private JButton racesButton;
    private JButton driversButton;
    private JButton constructorsButton;
    private JButton fastestLapsButton;
    private GridBagConstraints constraints;

    public HomeScreen() {
        super("2021 F1 World Championship ");

        constraints = new GridBagConstraints();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridBagLayout());
        setButton(racesButton,"Races", "showRaces", 0,0);
        setButton(driversButton,"Drivers", "showDrivers", 0,1);
        setButton(constructorsButton,"Constructors", "showConstructors", 0,2);
        setButton(fastestLapsButton, "Fastest Lap Awards", "showFastestLaps", 0, 3);

        pack();
        setVisible(true);
    }

    private void setButton(JButton button, String buttonName, String actionCommand, Integer xCoord, Integer yCoord) {
        button = new JButton(buttonName);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;

        // button size
        constraints.ipadx = 300;
        constraints.ipady = 100;

        // vertical spacing
        constraints.insets = new Insets(25,0,0,0);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        add(button, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
