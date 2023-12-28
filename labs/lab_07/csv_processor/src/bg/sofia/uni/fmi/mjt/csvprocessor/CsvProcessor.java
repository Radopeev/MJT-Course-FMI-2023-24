package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvProcessor implements CsvProcessorAPI {

    private Table table;
    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException{
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;
        while (true) {
            try {
                if ((line = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] data = line.split("\\Qdelimiter\\E");
            table.addData(data);
        }

    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments){
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        Map<String, List<String>> mapForRows = new HashMap<>();
        for (String str : table.getColumnNames()) {
            mapForRows.put(str,new ArrayList<>());
        }
        for (String str : table.getColumnNames()) {
            for(String column : table.getColumnData(str)){
                mapForRows.get(str).add(column);
            }
        }
        for (String str : table.getColumnNames()) {
            StringBuilder temp = new StringBuilder();
            for (String curr : mapForRows.get(str)) {
                temp.append(curr);
                temp.append(',');
            }
            try {
                writer.write(temp.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
