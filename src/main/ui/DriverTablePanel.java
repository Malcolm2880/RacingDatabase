package main.ui;

public class DriverTablePanel extends TablePanel {
    private static final String[] columnNames = {"RaceName", "Rank"};

    public DriverTablePanel(Object[][] data) {
        super(data, columnNames);
    }

    @Override
    public void checkClick() {


    }
}
