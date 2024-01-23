package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseColumnTest {
    private static Set<String> valuesForTest ;
    private static Column columnForTest;

    @BeforeAll
    static void setUp(){
        valuesForTest = new LinkedHashSet<>();
        columnForTest = new BaseColumn(valuesForTest);
    }

    @Test
    void testAddDataToColumn(){
        String test = "test";

        columnForTest.addData(test);

        assertTrue(valuesForTest.contains(test), "Values must contain test");
    }

    @Test
    void testAddDataToColumnWhenDataIsNull(){
        assertThrows(IllegalArgumentException.class,() ->columnForTest.addData(null),
            "Add data must throw exception when value is null");
    }

    @Test
    void testGetData(){
        Collection<String> testCollection = Collections.unmodifiableCollection(List.of("test1","test2","test3"));

        columnForTest.addData("test1");
        columnForTest.addData("test2");
        columnForTest.addData("test3");

        Collection<String> result = columnForTest.getData();

        assertIterableEquals(testCollection,result,"Data does not match");
    }
}
