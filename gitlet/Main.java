package gitlet;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if(args.length==0){
            System.out.println("Please enter a command");
            System.exit(0);
        }
        gitlet.set();
        switch (args[0]){
            case "init":
                gitlet.init();
                break;
            case  "commit":
                gitlet.commit(args);
                break;
            case  "rm":
                gitlet.rm(args[1]);
                break;
            case "log":
                gitlet.log();
                break;
            case "global-log":
                gitlet.globalLog();
                break;
            case "find":
                gitlet.find(args[1]);
                break;
            case "status":
                gitlet.status();
                break;
            case  "checkout":
                gitlet.checkout(args);
                break;
            case "branch":
                gitlet.branch(args);
                break;
            case "rm-branch":
                gitlet.rmbranch(args);
                break;
            case "reset":
                gitlet.reset(args);
                break;
            case "merge":
                gitlet.merge(args);
                break;
            default:
                System.out.println("No command with that name exists.");
                return;
        }
        CommitTree.serialization(gitlet.commitTree);
    }

}
