import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Samuel Smith
 * @author Yunan Zhang
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires [<"INSTRUCTION"> is a proper prefix of tokens]
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to statement string of body of
     *          instruction at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        /*
         * Get rid of INSTRUCTION
         */
        tokens.dequeue();

        String name = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(name),
                name + " is not a valid instruction name.");

        Reporter.assertElseFatalError(
                (tokens.length() > 0
                        && !tokens.front().equals(Tokenizer.END_OF_INPUT)),
                "Error: Unexpected end to program");

        /*
         * Check to ensure it the name is not primitive
         */
        Instruction[] prims = Instruction.values();
        for (Instruction primitive : prims) {
            Reporter.assertElseFatalError(
                    !name.equals(primitive.toString().toLowerCase()),
                    name + " is invalid instruction name since it is a primitive call.");
        }

        /*
         * Check to ensure IS keyword follows, then throw away
         */
        Reporter.assertElseFatalError(Tokenizer.isKeyword(tokens.front()),
                tokens.front() + " is not valid keyword.");
        tokens.dequeue();

        Reporter.assertElseFatalError(
                (tokens.length() > 0
                        && !tokens.front().equals(Tokenizer.END_OF_INPUT)),
                "Error: Unexpected end to program");

        /*
         * Load the instruction into the statement
         */
        body.parseBlock(tokens);

        Reporter.assertElseFatalError(
                (tokens.length() > 0
                        && !tokens.front().equals(Tokenizer.END_OF_INPUT)),
                "Error: Unexpected end to program");

        /*
         * Check to make sure END is present after instruction definition, then
         * throw away
         */
        Reporter.assertElseFatalError(Tokenizer.isKeyword(tokens.front()),
                "Syntax error in instruction " + name + " ending.");
        tokens.dequeue();

        /*
         * Load the name, check to make sure that the end name and beginning
         * name match
         */
        String endName = tokens.dequeue();

        Reporter.assertElseFatalError(name.equals(endName), "Provided name "
                + name + " does not match end name " + endName);

        return name;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

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
                "ERROR: Unexpected end of program file");
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        /*
         * Check that Program starts at program
         */
        checkKeyword("PROGRAM", tokens.front());
        checkLength(tokens);

        /*
         * Get rid of the PROGRAM keyword
         */
        tokens.dequeue();
        checkLength(tokens);

        /*
         * Enter the name into this
         */
        String actName = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(actName),
                "INVALID IDENTIFIER: " + actName);
        this.replaceName(actName);
        checkLength(tokens);

        /*
         * Get rid of IS keyword
         */
        checkKeyword("IS", tokens.front());
        tokens.dequeue();
        checkLength(tokens);

        Map<String, Statement> builtContext = this.newContext();

        while (tokens.front().equals("INSTRUCTION")) {
            checkKeyword("INSTRUCTION", tokens.front());
            Statement instrBody = this.newBody();
            String instrName = parseInstruction(tokens, instrBody);

            Reporter.assertElseFatalError(Tokenizer.isIdentifier(instrName),
                    "INVALID IDENTIFIER: " + instrName);

            boolean uniqueInstruction = builtContext.hasKey(instrName);

            Reporter.assertElseFatalError(!uniqueInstruction,
                    instrName + " is not a unique instruction");

            builtContext.add(instrName, instrBody);

            checkLength(tokens);
        }

        this.replaceContext(builtContext);

        /*
         * Check for and remove BEGIN
         */
        checkKeyword("BEGIN", tokens.front());
        tokens.dequeue();
        checkLength(tokens);

        /*
         * Generate new body for this and parse tokens to load
         */
        Statement buildingBody = this.newBody();

        buildingBody.parseBlock(tokens);

        this.replaceBody(buildingBody);

        checkLength(tokens);

        /*
         * Check that END is valid keyword
         */
        checkKeyword("END", tokens.front());
        tokens.dequeue();
        checkLength(tokens);

        /*
         * Get END <identifier>
         */
        String endName = tokens.dequeue();

        Reporter.assertElseFatalError(actName.equals(endName), "Beginning name "
                + actName + " does not match end name " + endName);

        /*
         * Check that no extra stuff is after program
         */
        Reporter.assertElseFatalError(
                (tokens.length() == 1
                        && tokens.front().equals(Tokenizer.END_OF_INPUT)),
                "ERROR: Extra items after program end.");

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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
