package main.ui;

import main.model.FastestLap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FastestLapScreen extends Screen {
    private TablePanel flResultsTable;
    private JButton groupFastestLapButton;

    public FastestLapScreen() {
        super("Fastest Laps");
        List<FastestLap> FastestLapResults = dbHandler.getFastestLapResults();
        setLayout(new BorderLayout());
        setResultsTable(FastestLapResults);
        HeaderPanel panel = new HeaderPanel("FastestLap Results", true);
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
    };

    private void setResultsTable(List<FastestLap> cResults) {
        Object[][]data = getCDataForTable(cResults);
        flResultsTable = new FastestLapTablePanel(data);
        add(flResultsTable, BorderLayout.CENTER);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }
}