package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarkdownTablePrinter implements TablePrinter {
    private static final int MIN_LENGTH_OF_CELL = 3;

    private Map<String, Integer> calculateAlignmentsForEachRow(Table table) {
        Map<String, Integer> alignmentsMap = new LinkedHashMap<>();

        for (String column : table.getColumnNames()) {
            alignmentsMap.put(column, MIN_LENGTH_OF_CELL);
        }

        for (String column : table.getColumnNames()) {
            int currMaxLengthForColumnName = alignmentsMap.get(column);
            alignmentsMap.put(column, Math.max(currMaxLengthForColumnName, column.length()));
            for (String columnData : table.getColumnData(column)) {
                int currMaxLengthForColumnData = alignmentsMap.get(column);
                alignmentsMap.put(column, Math.max(currMaxLengthForColumnData, columnData.length()));
            }
        }
        return alignmentsMap;
    }

    private String appendMainRow(Table table, Map<String , Integer> alignmentsMap) {
        StringBuilder mainRow = new StringBuilder();

        mainRow.append("|");
        for (String column : table.getColumnNames()) {
            mainRow.append(" ").append(column);
            int length = column.length();
            while (length <= alignmentsMap.get(column)) {
                mainRow.append(" ");
                length++;
            }
            mainRow.append("|");
        }
        return mainRow.toString();
    }

    private void generateLeftAlignments(StringBuilder secondRow, Map<String, Integer> alignmentsMap, String column) {
        secondRow.append(":");
        secondRow.append("-".repeat(alignmentsMap.get(column) -  ColumnAlignment.LEFT.getAlignmentCharactersCount()));
        secondRow.append(" |");
    }

    private void generateRightAlignments(StringBuilder secondRow, Map<String, Integer> alignmentsMap, String column) {
        secondRow.append("-".repeat(alignmentsMap.get(column) - ColumnAlignment.RIGHT.getAlignmentCharactersCount()));
        secondRow.append(": |");
    }

    private void generateCenterAlignments(StringBuilder secondRow, Map<String, Integer> alignmentsMap, String column) {
        secondRow.append(":");
        secondRow.append("-".repeat(alignmentsMap.get(column) - ColumnAlignment.CENTER.getAlignmentCharactersCount()));
        secondRow.append(": |");
    }

    private String appendSecondRow(Table table, Map<String , Integer> alignmentsMap, ColumnAlignment... alignments) {
        StringBuilder secondRow = new StringBuilder();
        int index = 0;
        secondRow.append("|");
        for (String column : table.getColumnNames()) {
            secondRow.append(" ");
            if (index < alignments.length) {
                switch (alignments[index++]) {
                    case LEFT -> generateLeftAlignments(secondRow, alignmentsMap, column);
                    case RIGHT -> generateRightAlignments(secondRow, alignmentsMap, column);
                    case CENTER -> generateCenterAlignments(secondRow, alignmentsMap, column);
                    case NOALIGNMENT -> secondRow.append("-".repeat(alignmentsMap.get(column))).append(" |");
                }
            } else {
                secondRow.append("-".repeat(alignmentsMap.get(column))).append(" |");
            }
        }
        return secondRow.toString();
    }

    private String generateRow(Table table, Collection<String> columnNames,
                               int rowIndex, Map<String, Integer> alignmentsMap) {
        StringBuilder rowData = new StringBuilder("|");

        for (String columnName : columnNames) {
            rowData.append(" ");
            Collection<String> columnData = table.getColumnData(columnName);
            Iterator<String> columnDataIterator = columnData.iterator();

            for (int i = 0; i < rowIndex; i++) {
                if (columnDataIterator.hasNext()) {
                    columnDataIterator.next();
                }
            }

            if (columnDataIterator.hasNext()) {
                String currColumnRowData = columnDataIterator.next();
                int length = currColumnRowData.length();
                rowData.append(currColumnRowData);
                while (length <= alignmentsMap.get(columnName)) {
                    rowData.append(" ");
                    length++;
                }
            }
            rowData.append("|");
        }
        return rowData.toString();
    }

    private void parseTableForPrinting(List<String> tableParsedForPrinting,
                                       Table table, Map<String, Integer> alignmentsMap) {
        int rowCount = table.getRowsCount();
        Collection<String> columnNames = table.getColumnNames();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            String currRow = generateRow(table, columnNames, rowIndex, alignmentsMap);
            tableParsedForPrinting.add(currRow);
        }
        tableParsedForPrinting.remove(tableParsedForPrinting.size() - 1);
    }

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Map<String, Integer> alignmnetsMap = calculateAlignmentsForEachRow(table);

        List<String> tableParsedForPrinting = new ArrayList<>();
        tableParsedForPrinting.add(appendMainRow(table, alignmnetsMap));
        tableParsedForPrinting.add(appendSecondRow(table, alignmnetsMap, alignments));
        parseTableForPrinting(tableParsedForPrinting, table, alignmnetsMap);

        return tableParsedForPrinting;
    }
}
