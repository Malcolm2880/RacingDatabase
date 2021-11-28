package main.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DriverTablePanel extends TablePanel {
    private static final String[] columnNames = {"Grand Prix", "Rank"};

    public DriverTablePanel(Object[][] data) {
        super(data, columnNames);
    }

    @Override
    public void checkClick() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getColumnName(table.columnAtPoint(e.getPoint())).equals("Grand Prix")) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    RaceScreen raceScreen = new RaceScreen((String) data[row][col]);
                    raceScreen.setVisible(true);
                }
            }
        });

    }
}
