import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Samuel Smith
 * @author Yunan Zhang
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */
    /**
     * Checks that a provided token is the expected keyword.
     *
     * @param expected
     * @param received
     */
    private static void checkKeyword(String expected, String received) {
        Reporter.assertElseFatalError(
                (Tokenizer.isKeyword(received) && expected.equals(received)),
                "INVALID KEYWORD: Expected " + expected + " but received "
                        + received);
    }

    /**
     * Checks that the length of tokens is greater than 0 and that the front of
     * it is not END OF INPUT.
     *
     * @param tokens
     */
    private static void checkLength(Queue<String> tokens) {
        Reporter.assertElseFatalError(
                (tokens.length() > 0
                        && !tokens.front().equals(Tokenizer.END_OF_INPUT)),
                "ERROR: Unexpected end of statement file");
    }

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [<"IF"> is a proper prefix of tokens]
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";

        tokens.dequeue();
        checkLength(tokens);

        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                tokens.front() + " is not a valid condition.");

        /*
         * Parse condition
         */
        Condition cond = parseCondition(tokens.dequeue());
        checkLength(tokens);

        /*
         * Check THEN
         */
        checkKeyword("THEN", tokens.front());

        tokens.dequeue();
        checkLength(tokens);
        /*
         * Create and parse body statement
         */
        Statement body = s.newInstance();
        body.parseBlock(tokens);
        Reporter.assertElseFatalError((tokens.length() > 0),
                "ERROR: Unexpected end to definition.");

        /*
         * Check for valid keyword
         */
        Reporter.assertElseFatalError(Tokenizer.isKeyword(tokens.front()),
                "Invalid keyword.");

        if (tokens.front().equals("ELSE")) {
            /*
             * Get rid of ELSE, make sure there's more tokens
             */
            tokens.dequeue();
            checkLength(tokens);

            /*
             * If ELSE isn't an empty block, parse the block
             */
            Statement elseBody = s.newInstance();
            if (!tokens.front().equals("END")) {
                elseBody.parseBlock(tokens);
            }

            /*
             * Check the lengths after removing each item, then make sure END IF
             * follows
             */
            checkLength(tokens);
            checkKeyword("END", tokens.front());
            tokens.dequeue();
            checkLength(tokens);
            checkKeyword("IF", tokens.front());
            tokens.dequeue();

            /*
             * Assemble the If-Else statement
             */
            s.assembleIfElse(cond, body, elseBody);
        } else if (tokens.front().equals("END")) {
            tokens.dequeue();

            checkLength(tokens);
            checkKeyword("IF", tokens.front());
            tokens.dequeue();

            /*
             * Assemble statement
             */
            s.assembleIf(cond, body);
        } else {
            Reporter.assertElseFatalError(false,
                    "Expected ELSE or END, received: " + tokens.front());
        }

    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [<"WHILE"> is a proper prefix of tokens]
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        /*
         * Remove WHILE
         */
        tokens.dequeue();

        /*
         * Check length and condition
         */
        checkLength(tokens);
        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                "Invalid condition: " + tokens.front());

        /*
         * Add condition
         */
        Condition cond = parseCondition(tokens.dequeue());
        checkLength(tokens);

        /*
         * Check for DO, then remove
         */
        checkKeyword("DO", tokens.front());
        tokens.dequeue();
        checkLength(tokens);
        /*
         * Create body and parse block
         */
        Statement body = s.newInstance();
        body.parseBlock(tokens);

        /*
         * Check length, then check END, then remove END
         */
        checkLength(tokens);
        checkKeyword("END", tokens.front());
        tokens.dequeue();

        /*
         * Check length, then check WHILE, then remove WHILE
         */
        checkLength(tokens);
        checkKeyword("WHILE", tokens.front());
        tokens.dequeue();

        /*
         * Construct while
         */
        s.assembleWhile(cond, body);
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        String call = tokens.dequeue();

        s.assembleCall(call);

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        switch (tokens.front()) {
            case "IF": {
                parseIf(tokens, this);
                break;
            }
            case "WHILE": {
                parseWhile(tokens, this);
                break;
            }
            case "ELSE": {
                /*
                 * Do nothing?
                 */
                break;
            }
            default: {
                Reporter.assertElseFatalError(
                        Tokenizer.isIdentifier(tokens.front()),
                        tokens.front() + " is invalid syntax.");
                parseCall(tokens, this);
            }

        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        while (tokens.length() > 0 && !tokens.front().equals("END")
                && !tokens.front().equals("ELSE")) {
            Statement aPiece = this.newInstance();
            aPiece.parse(tokens);
            this.addToBlock(this.lengthOfBlock(), aPiece);
        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
