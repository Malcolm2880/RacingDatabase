package main.ui;

import main.model.Constructor;
import main.model.Race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ConstructorScreen extends Screen {
    private TablePanel cResultsTable;
    private JButton joinConstructorButton;

    public ConstructorScreen() {
        super("Constructor Results");
        List<Constructor> constructorResults = dbHandler.getConstructorResults();
        setLayout(new BorderLayout());
        setResultsTable(constructorResults);
        HeaderPanel panel = new HeaderPanel("Constructor Results", true);
        add(panel, BorderLayout.PAGE_START);
    }

    private Object[][] getCDataForTable(List<Constructor> cResults) {
        Object[][] data = new Object[cResults.size()][];

        int i = 0;
        for (Constructor next : cResults) {
            Object[] obj = new Object[3];
            obj[0] = next.getName();
            obj[1] = next.getPosition();
            obj[2] = next.getPoints();
            data[i++] = obj;
        }
        return data;
    };

    private void setResultsTable(List<Constructor> cResults) {
        Object[][]data = getCDataForTable(cResults);
        cResultsTable = new ConstructorTablePanel(data);
        add(cResultsTable, BorderLayout.CENTER);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
