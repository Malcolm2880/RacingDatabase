package main.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RaceTablePanel extends TablePanel{
    private static final String[] columnNames = {"Grand Prix", "Laps", "Date", "Fastest Lap Average Speed",
            "Circuit Name", "Winner (Driver)", "Winner (Constructor)"};

    public RaceTablePanel(Object[][] data) {
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
                    System.out.println(data[row][col]);
                }
            }
        });
    }
}
