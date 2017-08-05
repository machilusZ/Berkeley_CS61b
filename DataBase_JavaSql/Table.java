// This is a SUGGESTED skeleton for a class that describes a single Row of a
// Table. You can throw this away if you want, but it is a good idea to try to
// understand it first.  Our solution changes or adds about 10 lines in this
// skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static db61b.Utils.*;

/** A single table in a database.
 * @author Daniel Duazo cs61b-vw */
class Table implements Iterable<Row> {
    /**
     * A new Table named NAME whose columns are give by COLUMNTITLES, which must
     * be distinct (else exception thrown).
     */
    Table(String name, String[] columnTitles) {
        Set<String> colTitles = new HashSet<String>();
        for (String c : columnTitles) {
            colTitles.add(c);
        }
        if (colTitles.size() != columnTitles.length) {
            throw error("Titles of columns must be unique.");
        } else {
            _name = name;
            _titles = columnTitles;
        }
    }

    /** A new Table named NAME whose column names are give by COLUMNTITLES. */
    Table(String name, List<String> columnTitles) {
        this(name, columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    int numColumns() {
        return _titles.length;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns a TableIterator over my rows in an unspecified order. */
    TableIterator tableIterator() {
        return new TableIterator(this);
    }

    /** Returns an iterator that returns my rows in an unspecified order. */
    @Override
    public Iterator<Row> iterator() {
        Iterator<Row> rowIterator = this.iterator();
        return rowIterator;
    }

    /** Return the title of the Kth column. Requires 0 <= K < columns(). */
    String title(int k) {
        return _titles[k];
    }

    /**
     * Return the number of the column whose title is TITLE, or -1 if there
     * isn't one.
     */
    int columnIndex(String title) {
        int numColumn = numColumns();
        for (int i = 0; i < numColumn; i += 1) {
            if (title.equals(_titles[i])) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    int size() {
        return rows.size();
    }

    /**
     * Add ROW to THIS if no equal row already exists. Return true if anything
     * was added, false otherwise.
     */
    boolean add(Row row) {
        if (row.size() != numColumns()) {
            throw error("Number of entries in this row is not the same as the number of columns");
        }
        for (Row r : rows) {
            if (row.equals(r)) {
                return false;
            }
        }
        rows.add(row);
        return true;
    }

    /**
     * Read the contents of the file NAME.db, and return as a Table. Format
     * errors in the .db file cause a DBException.
     */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(name, columnNames);
            while ((header = input.readLine()) != null) {
                String[] stringRow = header.split(",");
                Row tempRow = new Row(stringRow);
                table.add(tempRow);
            }
        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /**
     * Write the contents of TABLE into the file NAME.db. Any I/O errors cause a
     * DBException.
     */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = ",";
            output = new PrintStream(name + ".db");
            for (int i = 0; i < _titles.length; i += 1) {
                if (i == _titles.length - 1) {
                    output.println(_titles[i]);
                } else {
                    output.print(_titles[i] + sep);
                }
            }
            for (Row r : rows) {
                int n = numColumns();
                for (int j = 0; j < n; j += 1) {
                    if (j == n - 1) {
                        output.println(r.get(j));
                    } else {
                        output.print(r.get(j) + sep);
                    }
                }
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * Print my contents on the standard output, separated by spaces and
     * indented by two spaces.
     */
    void print() {
        String indent = "  ";
        String sep = " ";
        int n = numColumns();

        for (Row r : rows) {
            System.out.print(indent);
            for (int i = 0; i < n; i += 1) {
                System.out.print(r.get(i) + sep);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    /** Allows access to the names of the columns of this table. */
    public String[] getTitles() {
        return _titles;
    }
    
    /** Allows access to the rows of this table. */
    public ArrayList<Row> getRows() {
        return rows;
    }
    
    /** Deletes the row at index K. */
    public void deleteRow(int k) {
    	rows.remove(k);
    }
    
    /** Return the number of rows in this table. */
    public int numRows() {
    	return rows.size();
    }
    
    /** My name. */
    private final String _name;
    /** My column titles. */
    private String[] _titles;
    // OTHER FIELDS MIGHT GO HERE
    /** Stores the rows of the table. */
    private ArrayList<Row> rows = new ArrayList<>();
}

