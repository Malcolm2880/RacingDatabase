package main.ui;

public class ConstructorSingleTable extends TablePanel {
    private static final String[] columnNames = {"Grand prix", "Date", "Pts"};

    public ConstructorSingleTable(Object[][] data) {
        super(data, columnNames);
    }

    @Override
    public void checkClick() {

    }
}