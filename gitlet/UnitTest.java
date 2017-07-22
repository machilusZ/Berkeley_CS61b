package gitlet;

import org.junit.Before;
import ucb.junit.textui;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @Yunan Zhang
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    static  final String GITLET_DIR=".gitlet/";
    static final String STAGED_DIR=".gitlet/staged/";
    static final String COMMITS=".gitlet/commits/";
    //citation:https://stackoverflow.com/questions/7455931/java-before-and-test-annotation



}


