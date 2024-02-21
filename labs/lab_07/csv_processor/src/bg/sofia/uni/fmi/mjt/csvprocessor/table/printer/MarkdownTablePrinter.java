package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MarkdownTablePrinter implements TablePrinter{
    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        StringBuilder headRow = new StringBuilder();
        headRow.append("| ");
        for (String str : table.getColumnNames()) {
            headRow.append(str);
            headRow.append("| ");
        }
        headRow.append("|");
        StringBuilder secondRow = new StringBuilder();
        Map<String, Integer> maxLengthForAlignments = new HashMap<>();
        Map<String, List<String>> mapForRows = new HashMap<>();
        for (String str : table.getColumnNames()) {
            mapForRows.put(str,new ArrayList<>());
            maxLengthForAlignments.put(str,str.length());
        }
        for (String str : table.getColumnNames()) {
            for(String column : table.getColumnData(str)){
                mapForRows.get(str).add(column);
                if(maxLengthForAlignments.get(str)<column.length()){
                    maxLengthForAlignments.put(str,column.length());
                }
                if(maxLengthForAlignments.get(str)<3){
                    maxLengthForAlignments.put(str,3);
                }
            }
        }
        int index=0;
        for (String str : table.getColumnNames()) {
            secondRow.append("| ");
            if(index>=alignments.length) {
                secondRow.append("-".repeat(Math.max(0, maxLengthForAlignments.get(str))));
            }
            switch (alignments[index]){
                case LEFT -> secondRow.append(":").append("-".repeat(Math.max(0, maxLengthForAlignments.get(str))));
                case RIGHT -> secondRow.append("-".repeat(Math.max(0, maxLengthForAlignments.get(str)))).append(":");
                case CENTER -> secondRow.append(":").append("-".repeat(Math.max(0, maxLengthForAlignments.get(str)))).append(":");
                case NOALIGNMENT -> secondRow.append("-".repeat(Math.max(0, maxLengthForAlignments.get(str))));
            }
            index++;
        }
        List<String> ans = new ArrayList<>();
        ans.add(headRow.toString());
        ans.add(secondRow.toString());
        for (String str : table.getColumnNames()) {
            StringBuilder temp = new StringBuilder();
            temp.append('|');
            for (String curr : mapForRows.get(str)) {
                temp.append(curr);
                for (int i=0;i<maxLengthForAlignments.get(str);i++) {
                    temp.append(" ");
                }
            }
            temp.append("|");
            ans.add(temp.toString());
        }
        return ans;
    }
}
