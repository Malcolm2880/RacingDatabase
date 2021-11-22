package main.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RacesTablePanel extends TablePanel{
    private static final String[] columnNames = {"Grand Prix", "Laps", "Date", "Fastest Lap Average Speed",
            "Circuit Name", "Winner (Driver)", "Winner (Constructor)"};

    public RacesTablePanel(Object[][] data) {
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
