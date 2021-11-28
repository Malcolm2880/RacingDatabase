package main.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DriversTablePanel extends TablePanel{
    private static final String[] columnNames = {"Name", "Number", "Age", "Points", "Constructor"};

    public DriversTablePanel(Object[][] data) {
        super(data, columnNames);
    }

    @Override
    public void checkClick() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getColumnName(table.columnAtPoint(e.getPoint())).equals("Name")) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    RaceScreen raceScreen = new RaceScreen((String) data[row][col]); //TODO: choose driver screen from name OR number selection
                    raceScreen.setVisible(true);
                }

                if (table.getColumnName(table.columnAtPoint(e.getPoint())).equals("Constructor")) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    ConstructorSingleScreen constructorScreen = new ConstructorSingleScreen((String) data[row][col]);
                    constructorScreen.setVisible(true);
                }
            }
        });
    }
}
