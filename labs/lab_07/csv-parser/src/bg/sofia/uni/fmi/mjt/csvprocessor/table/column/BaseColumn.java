package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class BaseColumn implements Column {

    private Set<String> values;
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
        values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseColumn that = (BaseColumn) o;
        return Objects.equals(values, that.values);
    }
}