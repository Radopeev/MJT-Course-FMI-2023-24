package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTable implements Table {
    private List<Column> columns;
    private int rowCount;

    public BaseTable() {
        columns = new ArrayList<>();
        rowCount = 0;
    }

    private boolean checkForDuplicates(String[] data) {
        Map<String, Integer> checkFrequencies = new HashMap<>();

        for (String curr: data) {
            checkFrequencies.put(curr, 0);
        }
        for (String curr: data) {
            int currValue = checkFrequencies.get(curr);
            currValue++;
            checkFrequencies.put(curr, currValue);
        }
        for (String curr: data) {
            if (checkFrequencies.get(curr) > 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        if (columns.isEmpty()) {
            if (!checkForDuplicates(data)) {
                throw new CsvDataNotCorrectException("There cannot be duplicate names");
            }
            for (String columnName : data) {
                Column column = new BaseColumn();
                column.addData(columnName);
                columns.add(column);
            }
        } else {
            if (data.length != columns.size()) {
                throw new CsvDataNotCorrectException("Data is not in the correct format");
            }

            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                column.addData(data[i]);
            }
        }
        rowCount++;
    }

    @Override
    public Collection<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        for (Column column : columns) {
            columnNames.add(column.getData().iterator().next());
        }
        return Collections.unmodifiableCollection(columnNames);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isEmpty() || column.isBlank()) {
            throw new IllegalArgumentException("Column cannot be null");
        }
        for (Column curr : columns) {
            if (curr.getData().iterator().next().equals(column)) {
                List<String> columnData = new ArrayList<>(curr.getData());
                columnData.remove(0);
                return Collections.unmodifiableCollection(columnData);
            }
        }
        throw new IllegalArgumentException("There is no column with that name");
    }

    @Override
    public int getRowsCount() {
        return rowCount;
    }

}
