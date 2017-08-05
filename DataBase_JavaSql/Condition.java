// This is a SUGGESTED skeleton for a class that describes a single
// Condition (such as CCN = '99776').  You can throw this away if you
// want,  but it is a good idea to try to understand it first.
// Our solution changes or adds about 30 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.util.List;

/** Represents a single 'where' condition in a 'select' command.
 *  @author Daniel Duazo cs61b-vw */
class Condition {

    /** Internally, we represent our relation as a 3-bit value whose
     *  bits denote whether the relation allows the left value to be
     *  greater than the right (GT), equal to it (EQ),
     *  or less than it (LT). */
    private static final int GT = 1, EQ = 0, LT = -1;

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
        _col1 = col1;
        _col2 = col2;
        _relation = relation;
    }

    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, new Literal(val2));
    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote. */
    boolean test() {
        String column1, column2;
        column1 = _col1.value();
        column2 = _col2.value();
        
        if (_relation.equals("=")) {
            if (column1.compareTo(column2) == EQ) {
                return true;
            }
        }
        if (_relation.equals("<")) {
            if (column1.compareTo(column2) < EQ) {
                return true;
            }
        }
        if (_relation.equals(">")) {
            if (column1.compareTo(column2) > EQ) {
                return true;
            }
        }
        if (_relation.equals(">=")) {
            if (column1.compareTo(column2) > EQ || column1.compareTo(column2) == EQ) {
                return true;
            }
        }
        if (_relation.equals("<=")) {
            if (column1.compareTo(column2) < EQ || column1.compareTo(column2) == EQ) {
                return true;
            }
        }
        if (_relation.equals("!=")) {
            if (column1.compareTo(column2) != EQ) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff all CONDITIONS are satisfied. */
    static boolean test(List<Condition> conditions) {
        for (Condition c : conditions) {
            if (c.test() == false) {
                return false;
            }
        }
        return true;     
    }
    
    /** Returns the condition of the current condition. */
    public String relation() {
    	return _relation;
    }

    // ADD ADDITIONAL FIELDS HERE
    /** Represents the columns we're working with. */
    private Column _col1, _col2;
    /** Relationship between _COL1 and _COL2. */
    private String _relation;
}

