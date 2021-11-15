package main.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RacesScreen extends Screen {
    JLabel screenLabel;

    public RacesScreen() {
        super("Race Results");
        screenLabel = new JLabel("Race Results");
        add(screenLabel);
    }

    @Override
    protected void setConstraints() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
