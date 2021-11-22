package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel implements ActionListener {
    JLabel headerLabel;
    JButton backButton;
    String header;

    public HeaderPanel(String header, boolean canGoBack) {
        this.header = header;
        setPreferredSize(new Dimension(200,100));
        setHeaderLabel();

        if (canGoBack) {
            setBackButton();
        }

        setLayout(new BorderLayout());
        setVisible(true);
    }

    private void setHeaderLabel() {
        headerLabel = new JLabel(header);
        headerLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        headerLabel.setBounds(900,25,200,75);
        add(headerLabel);
    }

    private void setBackButton() {
        backButton = new JButton("Back");
        backButton.setBounds(0, 25, 100, 50);
        backButton.addActionListener(this);
        backButton.setActionCommand("goBack");
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "goBack") {
            SwingUtilities.getWindowAncestor(this).dispose();
            HomeScreen homeScreen = new HomeScreen();
            homeScreen.setVisible(true);
        }
    }
}
