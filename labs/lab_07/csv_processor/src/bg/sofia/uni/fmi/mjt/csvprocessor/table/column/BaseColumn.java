package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BaseColumn implements Column {

    private Set<String> values;
    private int maxLength = 3;
    public BaseColumn() {
        this(new LinkedHashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }
    @Override
    public void addData(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (data.length() > maxLength) {
            maxLength = data.length();
        }
        values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return values;
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, maxLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseColumn that = (BaseColumn) o;
        return maxLength == that.maxLength &&
            Objects.equals(values, that.values);
    }
}
