package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a home screen that contains the
// main menu and buttons to the other screens.
public class HomeScreen extends Screen {

    private JButton racesButton;
    private JButton driversButton;
    private JButton constructorsButton;
    private JButton fastestLapsButton;

    public HomeScreen() {
        super("2021 F1 World Championship");
        setButton(racesButton,"Races", "showRaces", 0,0);
        setButton(driversButton,"Drivers", "showDrivers", 0,1);
        setButton(constructorsButton,"Constructors", "showConstructors", 0,2);
        setButton(fastestLapsButton, "Fastest Lap Awards", "showFastestLaps", 0, 3);
    }

    @Override
     protected void setConstraints() {
        // button size
        constraints.ipadx = 300;
        constraints.ipady = 100;

        // vertical spacing
        constraints.insets = new Insets(25,0,0,0);

        constraints.fill = GridBagConstraints.HORIZONTAL;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "showRaces":
                RacesScreen racesScreen = new RacesScreen();
                displayScreen(racesScreen);
                break;
            case "showDrivers":
                // TODO: Add driver screen
                break;
            case "showConstructors":
                // TODO: Add constructor screen
                break;
            case "showFastestLaps":
                // TODO: Add fastest lap screen
                break;
        }

    }
}
