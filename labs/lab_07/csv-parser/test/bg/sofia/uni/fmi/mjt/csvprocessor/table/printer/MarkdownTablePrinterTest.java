package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarkdownTablePrinterTest {
    private static Table tableForTesting ;
    private static MarkdownTablePrinter markdownTablePrinterForTesting;

    @BeforeAll
    static void setup(){
        tableForTesting = new BaseTable();
        markdownTablePrinterForTesting = new MarkdownTablePrinter();
    }

    @Test
    void testPrintTable() throws CsvDataNotCorrectException {
        String[] mainRow = {"column1","column2","column3","column4","column5"};
        String[] firstRow = {"row1","row2","row3","row4","row5"};
        String[] secondRow = {"row6","row7","row8","row9","row10"};
        String[] thirdRow = {"row11","row12","row13","row14","row15"};
        ColumnAlignment[] alignments={ColumnAlignment.LEFT,ColumnAlignment.RIGHT,ColumnAlignment.CENTER,ColumnAlignment.NOALIGNMENT};

        tableForTesting.addData(mainRow);
        tableForTesting.addData(firstRow);
        tableForTesting.addData(secondRow);
        tableForTesting.addData(thirdRow);

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| column1 | column2 | column3 | column4 | column5 |");
        expectedResult.add("| :------ | ------: | :-----: | ------- | ------- |");
        expectedResult.add("| row1    | row2    | row3    | row4    | row5    |");
        expectedResult.add("| row6    | row7    | row8    | row9    | row10   |");
        expectedResult.add("| row11   | row12   | row13   | row14   | row15   |");

        Collection<String> result = markdownTablePrinterForTesting.printTable(tableForTesting,alignments);

        assertIterableEquals(result,expectedResult,"Table does not match");
    }
}
