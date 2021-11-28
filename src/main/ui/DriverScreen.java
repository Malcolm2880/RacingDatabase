package main.ui;

import main.model.Driver;
import main.model.DriverPlacementInRace;
import main.model.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DriverScreen extends Screen {
    private TablePanel driverStandingTable;
    private JButton updateDriverStandingButton;
    private HeaderPanel headerPanel;
    private final int driverNum;
    private String driverName;

    public DriverScreen(String title) {
        super(title);
        this.driverNum =  Integer.parseInt(title);
        this.driverName = dbHandler.getDriverNameFromNumber(driverNum);
        setTitle(driverName);
        System.out.println("here");

        List<DriverPlacementInRace> driverStanding = dbHandler.getDriverStanding(driverNum);
        setLayout(new BorderLayout());
        setResultTable(driverStanding);
        headerPanel = new HeaderPanel(driverName, false);
        add(headerPanel, BorderLayout.PAGE_START);
        setUpdateAction();
    }

    private void setResultTable(List<DriverPlacementInRace> driverStanding) {
        Object[][] data = getResultDataForTable(driverStanding);
        driverStandingTable = new DriverTablePanel(data);
        add(driverStandingTable, BorderLayout.CENTER);
    }

    private Object[][] getResultDataForTable(List<DriverPlacementInRace> driverStanding) {
        Object[][] data = new Object[driverStanding.size()][];

        int i = 0;
        for (DriverPlacementInRace next: driverStanding) {
            Object[] obj = new Object[2];
            obj[0] = next.getRaceName();
            obj[1] = next.getRank();
            data[i++] = obj;
        }
        return data;
    }

    private void setUpdateAction() {
        headerPanel.updateButton.addActionListener(this);
        headerPanel.updateButton.setActionCommand("update");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "update":
                List<DriverPlacementInRace> driverStanding = dbHandler.getDriverStanding(driverNum);
                remove(driverStandingTable);
                this.revalidate();
                this.repaint();
                setResultTable(driverStanding);
                this.revalidate();
                this.repaint();
                break;
        }

    }
}
