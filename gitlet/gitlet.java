package gitlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Created by yunan on 7/15/17.
 */
/*citationhttps://maryrosecook.com/blog/post/git-from-the-inside-out
 *https://www.youtube.com/watch?v=AIPFQsd30NM
 */
public class gitlet implements Serializable {
    //all the fields and methods in this class are static
    public static CommitTree commitTree;

    /*set up dir for gitlet, which includes three sub dir
    , staged, commit and blob
     */
    public static void set() {
        File gitlet=new File(".gitlet/");
        if(gitlet.exists()) {
            commitTree = CommitTree.deserialization();
        }
    }
    public static void init() {
        File gitletDir = new File(".gitlet/");
        boolean flag = gitletDir.mkdir();
        if(flag) {
            Commit init = new Commit();
            commitTree = new CommitTree();

            File staged = new File(".gitlet/staged/");
            File commits = new File(".gitlet/commits/");
            File blobs = new File(".gitlet/blobs/");

            staged.mkdir();
            commits.mkdir();
            blobs.mkdir();

            //set branch and head. Head is now init, branch is master by default
            commitTree.setHeadID (Commit.hashCommit(init));
            commitTree.setBranches("master", commitTree.getHeadID());
            commitTree.setCurrentBranch ("master");
            commitTree.setBranchToCommit ("master", commitTree.getHeadID());
            Commit.serialization(init,commitTree.getHeadID());

        }else {
            System.out.println("A gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }
    public static void add(String fileName) {
        File file = new File(fileName);
        /*
        HashMap<String,String> nb=head.getNameToBlobID();
        if(nb.containsKey(fileName)) {
            String sha=Utils.sha1(Utils.readContents(file));
            //there should be remove case. To be added
            if(commitTree.remove.contains(fileName)){
                commitTree.remove.remove(fileName);
            }//what if we happen to include a unstaged file in the rm?
            if (!nb.get(fileName).equals(sha)){
                commitTree.rmCommit.add(fileName);
                //add fileName to staging area
                if(commitTree.remove.contains(fileName)){
                    commitTree.remove.remove(fileName);
                }
                //update the nameToBolb map
                commitTree.staged.add(fileName);
         */
        try {
            if(!file.exists() || !file.getCanonicalPath().equals(System.getProperty("user.dir") + "/"+fileName)){
                System.out.println("File does not exist.");
                return;
            }
        } catch (IOException excp) {
            excp.printStackTrace();
        }
        if(commitTree.upFile(file,fileName)) {
            if(commitTree.getRemove().contains(fileName)){
                commitTree.removeRemove(fileName);
            }
            commitTree.stagedAdd(fileName);
            try {
                Files.copy(Paths.get(fileName), Paths.get(".gitlet/staged/" + fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (commitTree.getRemove().contains(fileName)) {
            commitTree.removeRemove(fileName);
            commitTree.rmCommitRemove(fileName);
        }
    }
    public static void commit(String message){
        if(commitTree.getRmCommit().size()==0 && commitTree.getStaged().size() == 0){
            //check if there's file remove or add
            System.out.println("No changes added to the commit");
            return;
        }
        //initialize a new Commit obj and put it at the head of our current branch by assigning head ID to it
        Commit newCommit = new Commit(message,commitTree.getHeadID());
        //copy files in stage area to bolbs
        //citation:https://stackoverflow.com/questions/1146153/copying-files-from-one-directory-to-another-in-java
        for(String fileName : commitTree.getStaged()) {
            File file = new File(".gitlet/staged/" + fileName);
            byte[] byteFile = Utils.readContents(file);
            String fileID = Utils.sha1(byteFile);
            newCommit.nameToBlobID.put(fileName,fileID);
            try {
                Files.copy(Paths.get(".gitlet/staged/"+fileName),Paths.get(".gitlet/blobs/"+fileID));
            } catch (IOException e){
                if(!(e instanceof FileAlreadyExistsException)) {
                    System.out.println("Failure to copy file to bolbs");
                    return;
                }
            }
            try {
                //clear staged area
                Files.delete(Paths.get(".gitlet/staged/"+fileName));
            } catch (IOException e){
                System.out.println(fileName);
                System.out.println("Failure to delete"+fileName);
                return;
            }
        }
        Commit head=commitTree.getHead();
        for(String fileName:head.nameToBlobID.keySet()){
            if(!commitTree.getRemove().contains(fileName)) {
                File file=new File(fileName);
                newCommit.nameToBlobID.put(fileName,Utils.sha1(Utils.readContents(file)));
            }
        }
        commitTree.setHeadID(Commit.hashCommit(newCommit));
        commitTree.branchPut(commitTree.getCurrentBranch(),commitTree.getHeadID());

        //clear staged in CommitTree(in for iteration, we clear the file dir, but the array list in CommitTree remains
        commitTree.resetRemove();
        commitTree.resetStaged();
        commitTree.branchToCommitPut (commitTree.getCurrentBranch(),commitTree.getHeadID());
        Commit.serialization(newCommit,commitTree.getHeadID());
    }

    public static void commit(String... args){
        if(args.length==1&&args[1]==""){
            System.out.println("Please enter a commit message");
        }else if(args.length==2){
            commit(args[1]);
        }else{
            System.out.println("Incorrect operands.");
            return;
        }
    }
    public static void rm(String name){
        Commit head = commitTree.getHead();
        if(head.nameToBlobID.containsKey(name)){
            try{
                if (commitTree.getStaged().contains(name)){
                    commitTree.stageRemove(name);
                    Files.delete(Paths.get(".gitlet/staged/"+name));
                }
                commitTree.removeAdd(name);
                commitTree.rmCommitAdd(name);
                Files.delete(Paths.get(name));
            }catch (IOException e){
                commitTree.removeAdd(name);
            }
        }else if (commitTree.getStaged().contains(name)) {
            try {
                commitTree.stageRemove(name);
                Files.delete(Paths.get(".gitlet/staged/"+name));
            } catch (IOException e){
                System.out.println("Failure to delete file");
            }
        } else {
            //not tracked nor in stage area
            System.out.println("No reason to remove the file.");
        }
    }
    public static void log() {
        String ptrID=commitTree.getHeadID();
        Commit pointer=commitTree.getHead();
        while(pointer.parentID != null){
            System.out.println("===");
            System.out.println("Commit "+ptrID);
            System.out.println(pointer.getCommitTime());
            System.out.println(pointer.getMessage());
            System.out.println();
            ptrID=pointer.getParentID();
            pointer=Commit.hashToCommit(pointer.getParentID());
        }
        //fix a bug, after the loop above, actually the init commit is still not printed
        System.out.println("===");
        System.out.println("Commit "+ptrID);
        System.out.println(pointer.getCommitTime());
        System.out.println(pointer.getMessage());
        System.out.println();
    }

    public static void globalLog() {
        //With the help of iterator, we can easily travese every commit, however
        //we need to avoid print the same element. Thus, we can add element we've
        //printed to a set, and check if the incoming elements has been printed or not.
        HashSet<String> alreadyPrinted = new HashSet<> ();
        //we first traverse each branch, then each commit in each branch
        for(String branchID: commitTree.getBranchToCommit().values()){
            Commit pointer = Commit.hashToCommit(branchID);
            String ptrID = branchID;
            // then travese this branch,which is the same as log
            while(pointer.parentID != null&&!alreadyPrinted.contains(ptrID)) {
                System.out.println("===");
                System.out.println("Commit " + ptrID);
                System.out.println(pointer.getCommitTime());
                System.out.println(pointer.getMessage());
                System.out.println();
                ptrID=pointer.getParentID();
                pointer=Commit.hashToCommit(ptrID);
            }

            if(!alreadyPrinted.contains(ptrID)){
                System.out.println("===");
                System.out.println("Commit "+ptrID);
                System.out.println(pointer.getCommitTime());
                System.out.println(pointer.getMessage());
                System.out.println();
                alreadyPrinted.add(ptrID);
            }
        }
    }

    public static void find(String... args){
        //this is similar to global log, just add a check and also, avoid duplicate printing
        if(args.length==2) {
            String message=args[1];
            HashSet<String> alreadyPrinted = new HashSet<>();
            boolean flag = false;
            //fix a bug by adding an outer loop, the commit with required message can appear
            // in branches other than the current one
            for (String branchID : commitTree.getBranchToCommit().values()) {
                Commit pointer = Commit.hashToCommit(branchID);
                String ptrID = branchID;
                // then travese this branch,which is the same as log
                while ( !alreadyPrinted.contains(ptrID) && pointer.parentID != null) {
                    if (message.equals(pointer.getMessage())) {
                        System.out.println(pointer.getCommitID());
                        flag = true;
                    }
                    alreadyPrinted.add(ptrID);
                    ptrID=pointer.getParentID();
                    pointer = Commit.hashToCommit(ptrID);
                }

                if (!alreadyPrinted.contains(ptrID)) {
                    if (message.equals(pointer.getMessage())) {
                        System.out.println(ptrID);
                        flag = true;
                    }
                    alreadyPrinted.add(pointer.getCommitID());
                }
            }
            if (!flag)
                System.out.println("Found no commit with that message.");
        } else {
            System.out.println("Incorrect operands.");
        }
    }
    public static void status() {
        System.out.println("=== Branches ===");
        for(String sub : commitTree.getBranches().keySet()) {
            if (sub.equals(commitTree.getCurrentBranch())) {
                System.out.println("*" + sub);
            } else {
                System.out.println(sub);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for(String file : commitTree.getStaged()) {
            System.out.println(file);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for(String file : commitTree.getRemove()) {
            System.out.println(file);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        /*citation:https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
        * citation: https://www.tutorialspoint.com/java/io/file_isdirectory.htm
        * citation: https://www.tutorialspoint.com/java/io/file_ishidden.htm
        * citation:https://stackoverflow.com/questions/14853402/both-file-isfile-and-file-isdirectory-is-returning-false
        * */
        File folder = new File(System.getProperty("user.dir"));
        File[] listOfFiles = folder.listFiles();
        HashSet<String> modifiedFile = new HashSet<>();
        //we judge via the file's sha
        for(String fileName:commitTree.getHead().nameToBlobID.keySet()){
            boolean containedInDir = false;
            for(File file : listOfFiles ){
                if(!file.isDirectory() && !file.isHidden()){
                    if(file.getName().equals(fileName)){//if fileName match, compare sha
                        String sha = Utils.sha1(Utils.readContents(file));
                        if(!commitTree.getHead().nameToBlobID.get(fileName).equals(sha)&&!commitTree.getStaged().contains(fileName)){
                            modifiedFile.add(fileName+" (modified)");
                        }else if(commitTree.getStaged().contains(fileName)){
                            File newFile = new File(".gitlet/staged/"+fileName);
                            String stagedSha = Utils.sha1(Utils.readContents(newFile));
                            if(!stagedSha.equals(stagedSha)){
                                modifiedFile.add(fileName+" (modified)");
                            }
                        }
                        containedInDir=true;
                    }
                }
            }
            //there's a special case: the file is newly added to working dir, but not added to staged area
            if(!commitTree.getStaged().contains(fileName)&&!containedInDir&&!commitTree.getRemove().contains(fileName)){
                modifiedFile.add(fileName+" (deleted)");
            }
        }
        for(String fileName:modifiedFile){
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        //Untracked to be added, it seems that I miss this feature in commitTree or commit
        commitTree.upUntracked();
        for(String name:commitTree.getUntracked()) {
            if(!commitTree.getStaged().contains(name)) {
                System.out.println(name);
            }
        }
    }
    public static void checkout(String... args){
        if(args.length == 2) {
            String branch=args[1];
            commitTree.upUntracked();
            if(!commitTree.getBranches().containsKey(branch)) {
                System.out.println("No such branch exists.");
                return;
            }
            String hash=commitTree.getBranches().get(branch);
            Commit nHead=Commit.hashToCommit(hash);
            if(branch.equals(commitTree.getCurrentBranch())) {
                System.out.println("No need to checkout the current branch.");
                return;
            } else {
                for (String name : nHead.nameToBlobID.keySet()) {
                    if(commitTree.getUntracked().contains(name)) {
                        System.out.println("There is an untracked file in the way; delete it or add it first.");
                        return;
                    }
                }
            }
            commitTree.setCurrentBranch(branch);
            Commit head=commitTree.getHead();
            for(String name : nHead.nameToBlobID.keySet()) {
                try {
                    Files.copy(Paths.get(".gitlet/blobs/" + nHead.nameToBlobID.get(name)),Paths.get(name));
                } catch (IOException e) {
                    System.out.println("Error copying files");
                }
            }
            commitTree.resetStaged();
        }
        else if(args.length==3&&args[1].equals(("--"))) {
            String name = args[2];
            Commit head = commitTree.getHead();
            commitTree.searchFile(name, head);
        } else if(args.length == 4&&(args[2].equals(("--")))) {
            String commitID=args[1];
            String name=args[3];
            for(String branchID:commitTree.getBranchToCommit().values()) {
                Commit pointer = Commit.hashToCommit(branchID);
                String pointerID = branchID;
                while (pointerID != null) {
                    if (pointerID.equals(commitID) || pointerID.substring(0, commitID.length()).equals(commitID)) {
                        commitTree.searchFile(name,pointer);
                        return;
                    } else if (pointerID.length() < commitID.length()) {
                        System.out.println("No commit with that id exists.");
                        return;
                    }
                    pointerID = pointer.parentID;
                    if (pointerID == null) {
                        break;
                    }
                    pointer = Commit.hashToCommit(pointer.parentID);

                }
            }
            System.out.println("No commit with that id exits");
        }else{
            System.out.println("Incorrect operands.");
        }
    }
    public static void branch(String... args){
        if(args.length==1) {
            System.out.println("Incorrect operands.");
        } else if (args.length==2){
            String name=args[1];
            if(!commitTree.getBranches().containsKey(name)){
                commitTree.branchToCommitPut(name,commitTree.getHeadID());
                commitTree.branchPut(name,commitTree.getHeadID());
            }else {
                System.out.println("A branch with that name already exists.");
            }
        }
    }
    public static void rmbranch(String... args){
        if(args.length==1){
            System.out.println("Incorrect operands.");
            return;
        }else if(args.length==2){
            String name=args[1];
            if(commitTree.getCurrentBranch().equals(name)){
                System.out.println("Cannot remove the current branch.");
            }else if(!commitTree.getBranches().containsKey(name)){
                System.out.println("A branch with that name does not exist.");
            }else {
                commitTree.getBranches().remove(name,commitTree.getHeadID());
            }
        }
    }
    public  static void reset(String... args){
        if(args.length==1){
            System.out.println("Incorrect operands.");
            return;
        }else if(args.length==2) {
            String commitid=args[1];
            Commit head = commitTree.getHead();
            String resetBranch=null;
            String pointerID=null;
            Commit pointer=null;
            for (String branch : commitTree.getBranchToCommit().keySet()) {
                pointer = Commit.hashToCommit(commitTree.getBranchToCommit().get(branch));
               pointerID = commitTree.getBranchToCommit().get(branch);
                while (pointerID!=null) {
                    if (pointerID.equals(commitid) || pointerID.substring(0, commitid.length()).equals(commitid)) {
                        break;
                    }
                    if (pointerID.length() < commitid.length()) {
                        System.out.println("No commit with that id exists.");
                    }
                    pointerID = pointer.parentID;
                    if(pointerID==null){
                        break;
                    }
                    pointerID=pointer.getParentID();
                    pointer=Commit.hashToCommit(pointerID);

                }
                if(pointerID!=null) {
                    resetBranch=branch;
                    break;
                }
            }
            if(pointerID==null) {
                System.out.println("No commit with that id exists.");
                return;
            }
            commitTree.upUntracked();
            for(String fileName:pointer.nameToBlobID.keySet()) {
                if(commitTree.getUntracked().contains(fileName)) {
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                    return;
                }
            }
            commitTree.resetStaged();
            commitTree.resetRemove();
            for(String name:pointer.nameToBlobID.keySet()) {
               commitTree.searchFile(name,pointer);
            }
            for(String name:head.nameToBlobID.keySet()){
                if(!pointer.nameToBlobID.containsKey(name)){
                    try{
                        Files.delete(Paths.get(name));
                    }catch (IOException e){
                        System.out.println("Error deleting files");
                    }
                }
            }
            commitTree.branchPut(resetBranch,pointerID);
            commitTree.setHeadID(pointerID);
        }
    }

    public static void merge(String... args){
        if(args.length==1){
            System.out.println("Incorrect operands.");
            return;
        }else if(args.length==2){
            String name=args[1];
            commitTree.upUntracked();
            if(name.equals((commitTree.getCurrentBranch()))){
                System.out.println("Cannot merge a branch with itself.");
                return;
            }else if(!commitTree.getBranches().containsKey(name)){
                System.out.println("A branch with that name does not exist.");
                return;
            }
            Commit split=null;
            String pointerID=commitTree.getBranches().get(name);
            Commit pointer=null;
            HashSet<String> checked=new HashSet<>();
            while (pointerID!= null) {
                pointer = Commit.hashToCommit(pointerID);
                checked.add(pointerID);
                pointerID = pointer.parentID;
            }

            Commit anotherPtr;
            String anotherID=commitTree.getBranches().get(commitTree.getCurrentBranch());


            while (anotherID != null) {
                anotherPtr = Commit.hashToCommit(anotherID);
                if (checked.contains(anotherID)) {
                    split=anotherPtr;
                }
                anotherID = anotherPtr.parentID;
            }
            String splitID=Commit.hashCommit(split);
            String branchID=commitTree.getBranches().get(name);
            Commit branchHead=Commit.hashToCommit(branchID);
            Commit head=commitTree.getHead();
            String headID=commitTree.getHeadID();
            String branchName=commitTree.getCurrentBranch();
            if(commitTree.getBranches().get(name).equals(splitID)) {
                System.out.println("Given branch is an ancestor of the current branch.");
                return;
            } else if(commitTree.getStaged().size() > 0 || commitTree.getRemove().size() > 0){
                System.out.println("You have uncommitted changes.");
                return;
            } else {
                commitTree.upUntracked();
                for(String file:branchHead.nameToBlobID.keySet()){
                    if(commitTree.getUntracked().contains(file)){
                        System.out.println("There is an untracked file in the way; delete it or add it first.");
                        return;
                    }
                }
            }
            if(commitTree.getHeadID().equals(splitID)){
                String[] parm={"checkout", name};
                checkout(parm);//There's error here and other checkout(probably comes from param
                commitTree.setHeadID(headID);
                commitTree.setCurrentBranch(branchName);
                System.out.println("Current branch fast-forwarded.");
            }
            HashSet<String> conflictFiles=findConflict(headID,branchID,split,branchHead.getCommitID());
            for(String cf:conflictFiles){
                resolve(name,headID,branchHead.getCommitID());
            }
            if(conflictFiles.size()!=0){
                System.out.println("Encountered a merge conflict.");
            }else{
                gitlet.commit("Merged "+ commitTree.getCurrentBranch()+" with "+name+".");
            }

        }

    }
    //handle merge conflict

        /*citation:https://softwarecave.org/2014/03/03/git-how-to-resolve-merge-conflicts/
         *citation:http://www.w3resource.com/java-tutorial/string/string_getbytes.php
         *https://github.com/hvqzao/java-deserialize-webapp/blob/master/src/main/java/hvqzao/java/deserialize/webapp/servlet/Servlet.java
         *https://stackoverflow.com/questions/161813/how-to-resolve-merge-conflicts-in-git
         */
    public static HashSet<String> findConflict(String headID,String sourceBranchID,Commit split,String sourceID){
        HashSet<String> conflictFiles=new HashSet<>();
        Commit brHead=Commit.hashToCommit(sourceBranchID);
        Commit head=Commit.hashToCommit(headID);
        for(String name:brHead.nameToBlobID.keySet()){
            if(!split.nameToBlobID.containsKey(name)){
                String[] para={"checkout",sourceBranchID,"--",name};
                checkout(para);
                conflictFiles.add(name);
            }else if(split.nameToBlobID.containsKey(name)){
               if(!split.nameToBlobID.get(name).equals(brHead.nameToBlobID.get(name))&&!(!head.nameToBlobID.get(name).equals(split.nameToBlobID.get(name))||head.nameToBlobID.containsKey(name))){
                   conflictFiles.add(name);
               }
            }else if(head.nameToBlobID.containsKey(name)) {
                conflictFiles.add(name);
            }
        }
        for(String name:head.nameToBlobID.keySet()){
            if(split.nameToBlobID.containsKey(name)){
                if(!brHead.nameToBlobID.containsKey(name)&&split.nameToBlobID.get(name).equals(head.nameToBlobID.get(name))){
                    rm(name);
                }
            }else if(!brHead.nameToBlobID.containsKey(name)&&!split.nameToBlobID.get(name).equals(head.nameToBlobID.get(name))){
                conflictFiles.add(name);
            }else if(!split.nameToBlobID.containsKey(name)&&!brHead.nameToBlobID.get(name).equals(head.nameToBlobID.get(name))&&brHead.nameToBlobID.containsKey(name)){
                conflictFiles.add(name);
            }else if(brHead.nameToBlobID.containsKey(name)&&!split.nameToBlobID.get(name).equals(brHead.nameToBlobID.get(name))){
                conflictFiles.add(name);
            }
        }
        return  conflictFiles;
    }
    public static void resolve(String name,String headID,String branchHeadID) {
        Commit head=Commit.hashToCommit(headID);
        Commit branchHead = Commit.hashToCommit(branchHeadID);
        File current = new File(".gitlet/blobs/" + head.nameToBlobID.get(name));
        File source = new File(".gitlet/blobs/" + branchHead.nameToBlobID.get(name));
        File output = new File(name);
        try{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] realH="<<<<<<< HEAD\n".getBytes();
            outputStream.write(realH);
            if(current.exists()){
                byte[] curSer=Utils.readContents(current);
                outputStream.write(curSer);
            }
            byte[] split="=======\n".getBytes();
            outputStream.write(split);
            if(source.exists()){
                byte[] serSource=Utils.readContents(source);
                outputStream.write(serSource);
            }
            byte[] end=">>>>>>>\n".getBytes();
            outputStream.write(end);
            byte[] byteOut=outputStream.toByteArray();
            Utils.writeContents(output,byteOut);
        }catch (IOException e){
            System.out.println("Error copying the file");
        }
    }
}
