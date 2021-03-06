package main.ui;

import main.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Parent class for the screens(frames) of the project.
public abstract class Screen extends JFrame implements ActionListener {
    protected GridBagConstraints constraints;
    protected DatabaseConnectionHandler dbHandler;

    public Screen(String title) {
        super(title);
        constraints = new GridBagConstraints();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridBagLayout());
        dbHandler = DatabaseConnectionHandler.getInstance();
    }

    protected void displayScreen(JFrame screen) {
        this.dispose();
        screen.setVisible(true);
    }

    protected void setButton(JButton button, String buttonName, String actionCommand, Integer xCoord, Integer yCoord) {
        button = new JButton(buttonName);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        constraints.gridx = xCoord;
        constraints.gridy = yCoord;
        add(button, constraints);
    }

}
