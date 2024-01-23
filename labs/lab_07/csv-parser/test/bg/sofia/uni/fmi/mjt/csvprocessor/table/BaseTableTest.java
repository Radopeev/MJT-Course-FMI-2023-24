package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseTableTest {
    private static BaseTable tableForTesting;
    @BeforeEach
    void setUp(){
        tableForTesting = new BaseTable();
    }

    @Test
    void testAddDataWhenDataIsNull() {
        assertThrows(IllegalArgumentException.class, () -> tableForTesting.addData(null),
            "Add data should throw exception when data is null");
    }

    @Test
    void testAddDataWhenColumnIsEmptyButThereAreDuplicates(){
        String[] data = {"test1", "test2", "test1"};

        assertThrows(CsvDataNotCorrectException.class, ()-> tableForTesting.addData(data),
            "Add data should throw exception when there are duplicates");
    }

    @Test
    void testAddDataWhenColumnIsEmptyAndThereAreNoDuplicates() throws CsvDataNotCorrectException {
        String[] data = {"test1", "test2", "test3"};

        tableForTesting.addData(data);
        Collection<String> columnNames = tableForTesting.getColumnNames();

        assertIterableEquals(columnNames,List.of(data),"Column names are not right");
    }

    @Test
    void testAddDataWhenColumnIsNotEmpty() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        String[] dataRow = {"row1", "row2", "row3"};

        tableForTesting.addData(dataColumns);
        tableForTesting.addData(dataRow);

        Collection<String> rowForTest = new ArrayList<>();

        for (int i = 0; i < dataColumns.length;i++) {
            Collection<String> currColumnData = tableForTesting.getColumnData(dataColumns[i]);
            rowForTest.addAll(currColumnData);
        }

        assertIterableEquals(rowForTest,List.of(dataRow),"Column data is not right");
    }

    @Test
    void testAddDataWhenDataLengthIsNotRight() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        tableForTesting.addData(dataColumns);
        String[] dataRow = {"row1", "row2", "row3","row4"};

        assertThrows(CsvDataNotCorrectException.class, () -> tableForTesting.addData(dataRow),
            "Add data should throw exception when data is not in the right size");
    }

    @Test
    void testGetColumnNames() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        tableForTesting.addData(dataColumns);
        Collection<String> result = tableForTesting.getColumnNames();

        assertIterableEquals(result,List.of(dataColumns),"Column names do not match");
    }

    @Test
    void testUnmodifiableCollectionInGetColumnNames() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        tableForTesting.addData(dataColumns);
        Collection<String> result = tableForTesting.getColumnNames();

        try {
            result.add("NewValue");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            e.getCause();
        }
    }

    @Test
    void testUnmodifiableCollectionInGetData() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        String[] dataRow = {"row1", "row2", "row3"};
        String[] dataRowTwo = {"row4", "row5", "row6"};

        tableForTesting.addData(dataColumns);
        tableForTesting.addData(dataRow);
        tableForTesting.addData(dataRowTwo);

        Collection<String> result = tableForTesting.getColumnData("column1");
        try {
            result.add("NewValue");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            e.getCause();
        }
    }

    @Test
    void testGetColumnDataWhenColumnIsNull(){
        assertThrows(IllegalArgumentException.class, ()-> tableForTesting.getColumnData(null),
            "Get column data should throw exception when column is null");
    }

    @Test
    void testGetColumnDataWhenColumnExistsInTable() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        String[] dataRow = {"row1", "row2", "row3"};
        String[] dataRowTwo = {"row4", "row5", "row6"};

        tableForTesting.addData(dataColumns);
        tableForTesting.addData(dataRow);
        tableForTesting.addData(dataRowTwo);

        Collection<String> result = tableForTesting.getColumnData("column1");
        Collection<String> testValues = new ArrayList<>(List.of("row1","row4"));

        assertIterableEquals(result,testValues,"Column data does not match");
    }

    @Test
    void testAddDataWhenColumnDoesNotExistsInTable() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        tableForTesting.addData(dataColumns);

        assertThrows(IllegalArgumentException.class, () -> tableForTesting.getColumnData("column4"),
            "Add data should throw exception when the column does not exists in the table");
    }

    @Test
    void testGetRowCount() throws CsvDataNotCorrectException {
        String[] dataColumns = {"column1", "column2", "column3"};
        String[] dataRow = {"row1", "row2", "row3"};
        String[] dataRowTwo = {"row4", "row5", "row6"};

        tableForTesting.addData(dataColumns);
        tableForTesting.addData(dataRow);
        tableForTesting.addData(dataRowTwo);

        assertEquals(3, tableForTesting.getRowsCount(),"The row count does not match");
    }
}
