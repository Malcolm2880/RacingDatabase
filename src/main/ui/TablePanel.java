package main.ui;

import javax.swing.*;
import java.awt.*;


public abstract class TablePanel extends JPanel {
    public JTable table;
    Object[][] data;

    public TablePanel(Object[][] data, String[] columnNames) {
        setLayout(new BorderLayout());

        table = new JTable(data, columnNames);
        table.setRowHeight(50);
        table.setDefaultEditor(Object.class, null);
        this.data = data;

        add(table.getTableHeader(), BorderLayout.PAGE_START);
        add(table, BorderLayout.CENTER);
        checkClick();

        setVisible(true);
    }

    public abstract void checkClick();
}
