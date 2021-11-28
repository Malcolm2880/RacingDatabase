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
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                DriverScreen driverScreen;

                switch(table.getColumnName(table.columnAtPoint(e.getPoint()))) {
                    case "Name":
                        driverScreen = new DriverScreen(data[row][col+1].toString());
                        driverScreen.setVisible(true);
                        break;
                    case "Number":
                        driverScreen = new DriverScreen(data[row][col].toString());
                        driverScreen.setVisible(true);
                        break;
                    case "Constructor":
                        ConstructorScreen constructorScreen = new ConstructorScreen((String) data[row][col]);
                        constructorScreen.setVisible(true);
                        break;
                }
            }
        });
    }
}
