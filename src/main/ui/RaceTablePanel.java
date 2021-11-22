package main.ui;

public class RaceTablePanel extends TablePanel {
    private static final String[] columnNames = {"Position", "Number", "Driver", "Constructor",
            "Points"};

    public RaceTablePanel(Object[][] data) {
        super(data, columnNames);
    }

    @Override
    public void checkClick() {

    }
}
