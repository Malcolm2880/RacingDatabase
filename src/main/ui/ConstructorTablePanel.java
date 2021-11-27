package main.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConstructorTablePanel extends TablePanel{
    private static final String[] columnNames = {"Name", "Position", "Points"};

    public ConstructorTablePanel(Object[][] data) {
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
                    ConstructorSingleScreen sScreen = new ConstructorSingleScreen((String) data[row][col]);
                    sScreen.setVisible(true);
                }
            }
        });
    }
}

