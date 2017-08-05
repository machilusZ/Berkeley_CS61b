package db61b;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/** @author Daniel Duazo cs61b-vw */

public class UnitTests {
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(UnitTests.class));
    }
    
    @Test
    public void testRow() {
        Row r1 = new Row(new String[]{"I", "am", "writing", "this", "test"});
        assertEquals(5, r1.size());
        assertEquals("am", r1.get(1));
        
        Column a = new Column("a");
        Column b = new Column("b");
        Column c = new Column("c");
        
        List<Column> listOfColumns = new ArrayList<>();
        listOfColumns.add(a);
        listOfColumns.add(b);
        listOfColumns.add(c);
        
        Row r2 = new Row(listOfColumns);
        assertEquals(3, r2.size());
        assertEquals("b", r2.get(1));
    }
    
    @Test
    public void testTable() {
        Table t = new Table("Test", new String[]{"These", "are", "test", "items"});
        assertEquals(4, t.numColumns());
        assertEquals("Test", t.name());
        assertEquals(2, t.columnIndex("test"));
        assertEquals(-1, t.columnIndex("CS61B"));
        Row r1 = new Row(new String[]{"A", "B", "C", "D"});
        assertEquals(0, t.size());
        t.add(r1);
        assertEquals(1, t.size());
        t.print();
        t.writeTable("testTable");
    }
    
    @Test
    public void testCondition() {
        Column col1 = new Column("1");
        Column col2 = new Column("2");
        
        Column colA = new Column("a");
        Column colB = new Column("b");
        
        Column col2Duplicate = new Column("2");
        
        String equal = "=";
        String notEqual = "!=";
        String lessThan = "<";
        String greaterThan = ">";
        String ge = ">=";
        String le = "<=";
        
        Condition cond1 = new Condition(col1, le, col2);
        Condition cond2 = new Condition(col2, equal, col2Duplicate);
        Condition cond3 = new Condition(col1, greaterThan, col2);
        Condition cond4 = new Condition(col2, lessThan, col2Duplicate);
        Condition condA = new Condition(colA, ge, colB);
        Condition condB = new Condition(colA, notEqual, colB);
        Condition condC = new Condition(colA, notEqual, colA);
        
        assertEquals(true, cond1.test());
        assertEquals(true, cond2.test());
        assertEquals(false, cond3.test());
        assertEquals(false, cond4.test());
        assertEquals(false, condA.test());
        assertEquals(true, condB.test());
        assertEquals(false, condC.test());
    }
    
}

