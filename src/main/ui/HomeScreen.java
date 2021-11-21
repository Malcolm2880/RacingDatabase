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
    private JButton overviewButton;

    public HomeScreen() {
        super("2021 F1 World Championship");
        setConstraints();
        setButton(racesButton,"Race Results", "showRaces", 0,0);
        setButton(driversButton,"Driver Standings", "showDrivers", 0,1);
        setButton(constructorsButton,"Constructor Standings", "showConstructors", 0,2);
        setButton(fastestLapsButton, "Fastest Lap Awards", "showFastestLaps", 0, 3);
        setButton(overviewButton, "Overview of the Season", "showOverview", 0, 4);
        setVisible(true);
    }

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
                ConstructorScreen cScreen = new ConstructorScreen();
                displayScreen(cScreen);
                break;
            case "showFastestLaps":
                // TODO: Add fastest lap screen
                break;
            case "showOverview":
                // TODO: Add overview screen
        }

    }
}
