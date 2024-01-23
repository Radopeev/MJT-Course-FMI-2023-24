package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CsvProcessorTest {
    private static Table tableForTesting;
    private static CsvProcessor csvProcessorForTesting;

    @BeforeEach
    void setUp() {
        tableForTesting = new BaseTable();
        csvProcessorForTesting = new CsvProcessor(tableForTesting);
    }

    @Test
    void testReadCsvWhenNoIOExceptionIsThrown() throws CsvDataNotCorrectException {
        String stringForTesting = """
              column1,column2,column3
              row1,row2,row3
              row11,row22,row33
              """;
        String[] mainRow = {"column1","column2","column3"};
        String[] columnOneData = {"row1","row11"};
        String[] columnTwoData = {"row2","row22"};
        String[] columnThreeData = {"row3","row33"};
        Reader readerForTesting = new StringReader(stringForTesting);
        csvProcessorForTesting.readCsv(readerForTesting,",");
        assertTrue(tableForTesting.getColumnNames().containsAll(List.of(mainRow)));
        assertTrue(tableForTesting.getColumnData("column1").containsAll(List.of(columnOneData)));
        assertTrue(tableForTesting.getColumnData("column2").containsAll(List.of(columnTwoData)));
        assertTrue(tableForTesting.getColumnData("column3").containsAll(List.of(columnThreeData)));
    }

    @Test
    void testWriteTable() throws CsvDataNotCorrectException {
        Writer stringWriter = new StringWriter();
        String[] mainRow = {"column1","column2","column3","column4","column5"};
        String[] firstRow = {"row1","row2","row3","row4","row5"};
        String[] secondRow = {"row6","row7","row8","row9","row10"};
        String[] thirdRow = {"row11","row12","row13","row14","row15"};
        ColumnAlignment[] alignments={ColumnAlignment.LEFT,ColumnAlignment.RIGHT,ColumnAlignment.CENTER,ColumnAlignment.NOALIGNMENT};

        tableForTesting.addData(mainRow);
        tableForTesting.addData(firstRow);
        tableForTesting.addData(secondRow);
        tableForTesting.addData(thirdRow);

        String result = """
            | column1 | column2 | column3 | column4 | column5 |
            | :------ | ------: | :-----: | ------- | ------- |
            | row1    | row2    | row3    | row4    | row5    |
            | row6    | row7    | row8    | row9    | row10   |
            | row11   | row12   | row13   | row14   | row15   |
            """;
        csvProcessorForTesting.writeTable(stringWriter,alignments);

        String normalizedExpected = result.trim().replaceAll("\\R+", "\n");
        String normalizedActual = stringWriter.toString().trim().replaceAll("\\R+", "\n");

        assertEquals(normalizedExpected, normalizedActual);
    }
}
