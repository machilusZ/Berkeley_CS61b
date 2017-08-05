// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static db61b.Utils.*;
import static db61b.Tokenizer.*;

/** An object that reads and interprets a sequence of commands from an
 *  input source.
 *  @author Yunan Zhang
class CommandInterpreter {

    /* STRATEGY.
     *
     *   This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar.  These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     *    <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement.  The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule.  For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token.  For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value.  We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     *    _input.next("create");
     *    _input.next("table");
     *    String name = name();
     *    Table table = tableDefinition();
     *    _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement.  The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument.  Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar).  Fortunately, our grammar has
     * been written with this problem in mind.  When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token.  For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     *     if (_input.nextIs("(")) {
     *          _input.next();
     *                                   +
     *         // code to process "<name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * or for convenience,
     *
     *     if (_input.nextIf("(")) {
     *                                   +
     *         // code to process "<name>,  )"
     *     } else {
     *     ...
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     *     call name() and do something with it;
     *     while (_input.nextIs(",")) {
     *         _input.next(",");
     *         call name() and do something with it;
     *     }
     *
     * or if you prefer even greater concision:
     *
     *     call name() and do something with it;
     *     while (_input.nextIf(",")) {
     *         call name() and do something with it;
     *     }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     *
     */

    /** A new CommandParser executing commands read from INP, writing
     *  prompts on PROMPTER, if it is non-null, and using DATABASE
     *  to map names of tables to corresponding Tables. */
    CommandInterpreter(Map<String, Table> database,
                       Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = database;
    }

    /** Parse and execute one statement from the token stream.  Return true
     *  iff the command is something other than quit or exit. */
    boolean statement() {
        switch (_input.peek()) {
        case "create":
            createStatement();
            break;
        case "load":
            loadStatement();
            break;
        case "exit": case "quit":
            exitStatement();
            return false;
        case "*EOF*":
            return false;
        case "insert":
            insertStatement();
            break;
        case "print":
            printStatement();
            break;
        case "select":
            selectStatement();
            break;
        case "store":
            storeStatement();
            break;
        default:
            throw error("unrecognizable command");
        }
        return true;
    }

    /** Parse and execute a create statement from the token stream. */
    private void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = name();
        Table table = tableDefinition(name);
        _database.put(name, table);
        selectStatement();
        _input.next(";");        
    }

    /** Parse and execute an exit or quit statement. Actually does nothing
     *  except check syntax, since statement() handles the actual exiting. */
    private void exitStatement() {
        if (!_input.nextIf("quit")) {
            _input.next("exit");
        }
        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    private void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = tableName();
        _input.next("values");

        ArrayList<String> values = new ArrayList<>();
        String entry = literal();
        values.add(entry);
        while (_input.nextIf(",")) {
            entry = literal();
            values.add(entry);
        }
        String[] newVals = new String[values.size()];
        newVals = values.toArray(newVals);
        Row inserted = new Row(newVals);
        table.add(inserted);
        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    private void loadStatement() {
        _input.next("load");
        String name = name();
        Table t = Table.readTable(name);
        _database.put(name, t);
        System.out.println("Loaded " + name + ".db");
        _input.next(";");
    }

    /** Parse and execute a store statement from the token stream. */
    private void storeStatement() {
        _input.next("store");
        String name = _input.peek();
        Table table = tableName();
        table.writeTable(name);
        System.out.println("Stored " + name + " into " + name + ".db");
        _input.next(";");
    }

    /** Parse and execute a print statement from the token stream. */
    private void printStatement() {
        _input.next("print");
        String tableName = _input.peek();
        Table table = tableName();
        System.out.println("Contents of " + tableName + ":");
        table.print();
        _input.next(";");
    }

    /** Parse and execute a select statement from the token stream. */
    private void selectStatement() {
        _input.next("select");
        Table returnTable = selectClause("");
       _input.next(";");
       System.out.println("Search results:");
       returnTable.print();
    }

    /** Parse and execute a table definition for a Table named NAME,
     *  returning the specified table. */
    Table tableDefinition(String name) {
        Table table;
        if (_input.nextIf("(")) {
            ArrayList<String> colNames = new ArrayList<>();
            colNames.add(name());
            while (_input.nextIf(",")) {
                colNames.add(name());
            }
            table = new Table(name, colNames);
        } else {
            _input.next("as");
            table = selectClause(name);
        }
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table, with name TABLENAME. */
    Table selectClause(String tableName) {
        ArrayList<String> officialColNames = new ArrayList<>();
        ArrayList<Table> listTables = new ArrayList<>();
        ArrayList<String> colAs = new ArrayList<>();
        String tempColumnLabel;
        
        tempColumnLabel = name();
        if (_input.peek().equals(".")) {
        	_input.next(".");
        	tempColumnLabel = name();
        	officialColNames.add(tempColumnLabel);
        } else {
            officialColNames.add(tempColumnLabel);
        } 
        
        if (_input.nextIf("as")) {
            tempColumnLabel = name();
            while (!_input.peek().equals("from") && !_input.peek().equals(",")) {
            	tempColumnLabel += " " + name();
            }
        }
        colAs.add(tempColumnLabel);
        
        while (_input.nextIf(",")) {
        	tempColumnLabel = name();
            if (_input.peek().equals(".")) {
            	_input.next(".");
            	tempColumnLabel = name();
            	officialColNames.add(tempColumnLabel);
            } else {
                officialColNames.add(tempColumnLabel);
            } 
            
            if (_input.nextIf("as")) {
                tempColumnLabel = name();
                while (!_input.peek().equals("from") && !_input.peek().equals(",")) {
                	tempColumnLabel += " " + name();
                }
            }
            colAs.add(tempColumnLabel);
      	}
        
        _input.next("from");
        
        Table t = tableName();
        listTables.add(t);
        while (_input.nextIf(",")) {
            Table temp = tableName();
            listTables.add(temp);
        }
        
        ArrayList<TableIterator> tableIters = new ArrayList<>();
        for (Table table : listTables) {
            tableIters.add(table.tableIterator());
        }
        
        ArrayList<Column> validCols = new ArrayList<>();
        ArrayList<String> validColsLabels = new ArrayList<>();
        for (int i = 0; i < officialColNames.size(); i += 1) {
            for (TableIterator iter : tableIters) {
                if (iter.columnIndex(officialColNames.get(i)) != -1) {
                    Column tempCol = new Column(iter.table(), officialColNames.get(i));
                    validCols.add(tempCol);
                    validColsLabels.add(colAs.get(i));
                }
            }
        }
        
	    Table rtn = new Table(tableName, validColsLabels);
	    List<Condition> conditions = conditionClause(tableIters);
	    select(rtn, validCols, tableIters, conditions);
	    return rtn;
    }
        
    
    
    /** Parse and return a valid name (identifier) from the token stream.
     *  The identifier need not have a meaning. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /** Parse valid column designation (name or table.name), and
     *  return as an unresolved Column. */
    Column columnSelector() {
        return null; 
    }

    /** Parse and return a column designator, after resolving against
     *  ITERATORS. */
    Column columnSelector(List<TableIterator> iterators) {
        Column col = columnSelector();
        col.resolve(iterators);
        return col;
    }

    /** Parse a valid table name from the token stream, and return the Table
     *  that it designates, which must be loaded. */
    Table tableName() {
        String name = name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /** Parse a literal and return the string it represents (i.e., without
     *  single quotes). */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /** Parse and return a list of Conditions that apply to TABLES from the
     *  token stream.  This denotes the conjunction (`and') of zero
     *  or more Conditions.  Resolves all Columns within the clause
     *  against ITERATORS. */
    List<Condition> conditionClause(List<TableIterator> iterators) {
        ArrayList<Condition> conds = new ArrayList<>();
        if (_input.nextIf("where")) {
            Condition condition = condition(iterators);
            conds.add(condition);
            while (_input.nextIf("and")) {
                Condition tempCond = condition(iterators);
                conds.add(tempCond);
            }
        }
        return conds;
    }

    /** Parse and return a Condition that applies to ITERATORS from the
     *  token stream. */
    Condition condition(List<TableIterator> iterators) {
        String columnName = name();
        if (_input.peek().equals(".")) {
        	_input.next(".");
        	columnName = name();
        }
        String relation = _input.next(Tokenizer.RELATION);
        
        if (_input.nextIs(Tokenizer.LITERAL)) {
            String col2 = literal();
            for (TableIterator iter : iterators) {
                int col1Index = iter.columnIndex(columnName);
                if (col1Index != -1) {
                    Column col1 = new Column(iter.table(), columnName);
                    col1.resolve(iterators);
                    Condition condition = new Condition(col1, relation, col2);
                    return condition;
                }
            }
        } else {
            String col2Name = name();
            if (_input.peek().equals(".")) {
            	_input.next(".");
            	columnName = name();
            }
            for (TableIterator iter : iterators) {
                int col1Index = iter.columnIndex(columnName);
                int col2Index = iter.columnIndex(col2Name);
                if (col1Index != -1 && col2Index != -1) {
                    Column col1 = new Column(iter.table(), columnName);
                    Column col2 = new Column(iter.table(), col2Name);
                    col1.resolve(iterators);
                    col2.resolve(iterators);
                    Condition condition = new Condition(col1, relation, col2);
                    return condition;
                }
            }
        }
        return null; 
    }

    /** Fill TABLE with the result of selecting COLUMNS from the rows returned
     *  by ITERATORS that satisfy CONDITIONS.  ITERATORS must have size 1 or 2.
     *  All selected Columns and all Columns mentioned in CONDITIONS must be
     *  resolved to iterators listed among ITERATORS.  The number of
     *  COLUMNS must equal TABLE.columns(). */
    private void select(Table table, ArrayList<Column> columns,
                        List<TableIterator> iterators,
                        List<Condition> conditions) {
        for (Column c : columns) {
            c.resolve(iterators);
        }
        if (iterators.size() == 1) {
	        for (TableIterator iter : iterators) {
	            while (iter.hasRow()) {
	                if (Condition.test(conditions)) {
	                	ArrayList<String> row = new ArrayList<>();
	                    for (Column c : columns) {
	                        String colName = c.name();
	                        int colIndex = iter.columnIndex(colName);
	                        if (colIndex != -1) {
	                        	row.add(iter.value(colIndex));
	                        }
	                    }
	                    String[] convertedArray = new String[row.size()];
	                    convertedArray = row.toArray(convertedArray);
	                    Row completeRow = new Row(convertedArray);
	                    table.add(completeRow);
	                }
	                iter.next();	
	             } 
	        }
        } else {
        	ArrayList<Column> t1Cols = new ArrayList<>();
        	ArrayList<Column> t2Cols = new ArrayList<>();
        	for (int t = 0; t < iterators.size(); t += 1) {
        		TableIterator currIter = iterators.get(t);
        		for (Column c : columns) {
        			String currColName = c.name();
        			int currColIndex = currIter.columnIndex(currColName);
        			if (currColIndex != -1) {
        				if (t == 0) {
        					t1Cols.add(c);
        				} else {
        					t2Cols.add(c);
        				}
        			}
        		}
        	}
        	for (int t = 0; t < iterators.size(); t += 1) {
        		if (t == 0) {
        			TableIterator firstIterator = iterators.get(t);
        			while (firstIterator.hasRow()) {
        				if (Condition.test(conditions)) {
        					TableIterator secondIterator = iterators.get(1);
            				while (secondIterator.hasRow()) {
            					ArrayList<String> tempRow = new ArrayList<>();
                				for (Column c : t1Cols) {
                					String colName = c.name();
                					int colIndex = firstIterator.columnIndex(colName);
                					tempRow.add(firstIterator.value(colIndex));
                				}
            					for (Column c : t2Cols) {
                					String colName = c.name();
                					int colIndex = secondIterator.columnIndex(colName);
                					tempRow.add(secondIterator.value(colIndex));
                				}
            					String[] tempArray = new String[tempRow.size()];
            					tempArray = tempRow.toArray(tempArray);
            					Row row = new Row(tempArray);
            					table.add(row);
            					secondIterator.next();
            				}
            				secondIterator.reset();
        				}
        				firstIterator.next();
        			}
        			firstIterator.reset(); 
        		}
        	}
        }
    }
    
    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    /** The command input source. */
    private Tokenizer _input;
    /** Database containing all tables. */
    private Map<String, Table> _database;
}
