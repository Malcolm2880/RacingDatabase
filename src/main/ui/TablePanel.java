package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TablePanel extends JPanel implements ActionListener {
    private JTable table;

    public TablePanel(Object[][] data, String[] columnNames) {
        setLayout(new BorderLayout());

        table = new JTable(data, columnNames);
        table.setRowHeight(50);
        table.setDefaultEditor(Object.class, null);

        add(table.getTableHeader(), BorderLayout.PAGE_START);
        add(table, BorderLayout.CENTER);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
