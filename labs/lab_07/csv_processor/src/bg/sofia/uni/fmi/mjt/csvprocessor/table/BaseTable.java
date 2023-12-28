package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseTable implements Table{
    private Map<String,Column> columns;

    public BaseTable() {
        columns = new LinkedHashMap<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if(data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (columns.isEmpty()) {
            for (int i = 0;i < data.length;i++){
                Column column = new BaseColumn();
                column.addData(data[i]);
                columns.put(data[i],column);
            }
            return;
        }
        if (data.length < columns.size() || data.length > columns.size()){
           throw new CsvDataNotCorrectException("Data is not in the correct format") ;
        }
        Iterator<Column> it = columns.values().iterator();
        for (int i = 0;i < data.length;i++){
            it.next().addData(data[i]);
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        return List.copyOf(columns.keySet());
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isEmpty() || column.isBlank()) {
            throw new IllegalArgumentException("Column cannot be null");
        }
        for (String curr : columns.keySet()){
            if(curr.equals(column)){
                return List.copyOf(columns.get(curr).getData());
            }
        }
        throw  new IllegalArgumentException("There is no column with that name");
    }

    @Override
    public int getRowsCount() {
        return columns.values().iterator().next().getData().size()  ;
    }
}
