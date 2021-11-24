package main.ui;



import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FastestLapTablePanel extends TablePanel{
    private static final String[] columnNames = {"Average Speed", "Lap Time", "Race Name", "Driver Number"};

    public FastestLapTablePanel(Object[][] data) {
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
                    System.out.println(data[row][col]);
                }
            }
        });
    }
}
