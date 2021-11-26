package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel implements ActionListener {
    JLabel headerLabel;
    JButton backButton;
    JButton updateButton;
    String header;

    public HeaderPanel(String header, boolean canGoBack) {
        this.header = header;
        setPreferredSize(new Dimension(200,100));
        setHeaderLabel();

        if (canGoBack) {
            setBackButton();
        }

        setUpdateButton();

        setLayout(new BorderLayout());
        setVisible(true);
    }

    private void setHeaderLabel() {
        headerLabel = new JLabel(header);
        headerLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        headerLabel.setBounds((d.width - r.width)/2,25,400,75);
        add(headerLabel);
    }

    private void setBackButton() {
        backButton = new JButton("Back");
        backButton.setBounds(0, 25, 100, 50);
        backButton.addActionListener(this);
        backButton.setActionCommand("goBack");
        add(backButton);
    }

    private void setUpdateButton() {
        updateButton = new JButton("Update");
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        updateButton.setBounds((d.width - r.width)-100, 25, 100, 50);
        updateButton.addActionListener(this);
        updateButton.setActionCommand("update");
        add(updateButton);
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
