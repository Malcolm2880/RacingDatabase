package main.ui;

import main.model.FastestLap;
import main.model.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FastestLapScreen extends Screen {
    private TablePanel flResultsTable;
    private JButton deleteRaceResultButton;
    private JButton updateRaceResultButton;
    private HeaderPanel panel;


    private JPanel buttonPanel;

    public FastestLapScreen() {
        super("Fastest Laps");
        List<FastestLap> FastestLapResults = dbHandler.getFastestLapResults();
        setLayout(new BorderLayout());
        setResultsTable(FastestLapResults);
        //setButtons();

        panel = new HeaderPanel("FastestLap Results", true);
        setUpdateAction();
        add(panel, BorderLayout.PAGE_START);
    }

    private Object[][] getCDataForTable(List<FastestLap> cResults) {
        Object[][] data = new Object[cResults.size()][];

        int i = 0;
        for (FastestLap next : cResults) {
            Object[] obj = new Object[4];
            obj[0] = next.getAverageSpeed();
            obj[1] = next.getLapTime();
            obj[2] = next.getRaceName();
            obj[3] = next.getDriverNumber();
            data[i++] = obj;
        }
        return data;
    }



    private void setUpdateAction() {
        panel.updateButton.addActionListener(this);
        panel.updateButton.setActionCommand("update");
    }

    private void setResultsTable(List<FastestLap> cResults) {
        Object[][] data = getCDataForTable(cResults);
        flResultsTable = new FastestLapTablePanel(data);
        add(flResultsTable, BorderLayout.CENTER);
    }

    private void setButtons() {

        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        Integer buttonSize = (d.width - r.width) / 3;

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(deleteRaceResultButton, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "deleteResult":
               // String driverNameToDelete = (String) flResultsTable.data[flResultsTable.table.getSelectedRow()][3];
                String raceNameToDelete =  (String) flResultsTable.data[flResultsTable.table.getSelectedRow()][2];
                String driverNameToDelete = dbHandler.getFastestLapDriver(raceNameToDelete);
              raceNameToDelete = raceNameToDelete.replaceAll("\\s", " ");
                driverNameToDelete = driverNameToDelete.replaceAll("\\s", " ");

                System.out.println(raceNameToDelete + " " +driverNameToDelete);
                 // dbHandler.deleteFastestLapResult(raceNameToDelete, driverNameToDelete);
                break;
            case "update":
                List<FastestLap> raceResults = dbHandler.getFastestLapResults();
                remove(flResultsTable);
                this.revalidate();
                this.repaint();
                setResultsTable(raceResults);
                this.revalidate();
                this.repaint();
                break;
        }
    }
}