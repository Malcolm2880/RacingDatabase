package main.ui;

import main.model.Constructor;
import main.model.ConstructorRace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ConstructorSingleScreen extends Screen {
    private TablePanel cResultsTable;
    private JButton groupConstructorButton;
//get  race result query
    public ConstructorSingleScreen(String title) {
        super(title);
        List<ConstructorRace> constructorResults = dbHandler.getConstructorRaceResults(title);
        setLayout(new BorderLayout());
        setResultsTable(constructorResults);
        HeaderPanel panel = new HeaderPanel(title, true);
        add(panel, BorderLayout.PAGE_START);
    }

    private Object[][] getCDataForTable(List<ConstructorRace> cResults) {
        Object[][] data = new Object[cResults.size()][];

        int i = 0;
        for (ConstructorRace next : cResults) {
            Object[] obj = new Object[3];
            obj[0] = next.getGrandPrix();
            obj[1] = next.getDate();
            obj[2] = next.getPoints();
            data[i++] = obj;
        }
        return data;
    };

    private void setResultsTable(List<ConstructorRace> cResults) {
        Object[][]data = getCDataForTable(cResults);
        cResultsTable = new ConstructorTablePanel(data);
        add(cResultsTable, BorderLayout.CENTER);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }
}