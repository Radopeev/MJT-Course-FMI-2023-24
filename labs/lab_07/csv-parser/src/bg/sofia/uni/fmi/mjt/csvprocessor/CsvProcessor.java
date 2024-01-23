package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

public class CsvProcessor implements CsvProcessorAPI {
    private Table table;
    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;
        while (true) {
            try {
                if ((line = bufferedReader.readLine()) == null) break;
                String[] resultingLine = line.split("\\Q" + delimiter + "\\E");
                table.addData(resultingLine);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        MarkdownTablePrinter tablePrinter = new MarkdownTablePrinter();
        Collection<String> tableForFile = tablePrinter.printTable(table, alignments);

        try {
            int lineCount = 0;
            for (String line : tableForFile) {
                writer.write(line);
                lineCount++;
                if (lineCount < tableForFile.size()) {
                    writer.write(System.lineSeparator());
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}