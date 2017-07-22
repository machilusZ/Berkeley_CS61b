package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Created by yunan on 7/15/17.
 */
public class gitlet implements Serializable{
    //all the fields and methods in this class are static
    public static CommitTree commitTree;

    /*set up dir for gitlet, which includes three sub dir
    , staged, commit and blob
     */
    public static void set(){
        File gitlet=new File(".gitlet/");
        if(gitlet.exists()){
            commitTree=CommitTree.deserialization();
        }
    }
    public static void init(){
        File gitletDir=new File(".gitlet/");
        boolean flag=gitletDir.mkdir();
        if(flag){
            Commit init=new Commit();
            commitTree=new CommitTree();

            File staged=new File(".gitlet/staged/");
            File commits=new File(".gitlet/commits/");
            File blobs=new File(".gitlet/blobs/");

            staged.mkdir();
            commits.mkdir();
            blobs.mkdir();

            //set branch and head. Head is now init, branch is master by default
            commitTree.headID= Commit.hashCommit(init);
            commitTree.branches.put("master",commitTree.headID);
            commitTree.currentBranch="master";
            Commit.serialization(init,commitTree.headID);

        }else{
            System.out.println("A gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }
    public static void add(String fileName){
        File file=new File(fileName);
        try{
            if(!file.exists()||!file.getCanonicalPath().equals(System.getProperty("user.dir")+"/"+fileName)){
                System.out.println("File does not exist.");
                return;
            }
        }catch(IOException excp){
            excp.printStackTrace();
        }
        Commit head=commitTree.getHead();
        HashMap<String,String> nb=head.getNameToBlobID();
        if(nb.containsKey(fileName)) {
            String sha=Utils.sha1(Utils.readContents(file));
            //there should be remove case. To be added
            if(commitTree.remove.contains(fileName)){
                commitTree.remove.remove(fileName);
            }//what if we happen to include a unstaged file in the rm?
            if (!nb.get(fileName).equals(sha)){
                //add fileName to staging area
                commitTree.staged.add(fileName);
                //update the nameToBolb map
                nb.remove(fileName);
                nb.put(fileName,sha);
                try{
                    Files.copy(Paths.get(fileName),Paths.get(".gitlet/staged/"+fileName), StandardCopyOption.REPLACE_EXISTING);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }else if(!nb.containsKey(fileName)){
            String sha=Utils.sha1(Utils.readContents(file));
            nb.put(fileName,sha);
            commitTree.staged.add(fileName);
            try{
                Files.copy(Paths.get(fileName),Paths.get(".gitlet/staged/"+fileName), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void commit(String message){
        if(commitTree.remove.size()==0&&commitTree.staged.size()==0){
            //check if there's file remove or add
            System.out.println("No changes added to the commit");
            return;
        }
        //initialize a new Commit obj and put it at the head of our current branch by assigning head ID to it
        Commit newCommit=new Commit(message,commitTree.headID);
        //copy files in stage area to bolbs
        //citation:https://stackoverflow.com/questions/1146153/copying-files-from-one-directory-to-another-in-java
        for(String fileName: commitTree.staged){
            File file=new File(".gitlet/staged/"+fileName);
            byte[] byteFile=Utils.readContents(file);
            String fileID=Utils.sha1(byteFile);
            newCommit.nameToBlobID.put(fileName,fileID);
            try{
                Files.copy(Paths.get(".gitlet/staged/"+fileName),Paths.get(".gitlet/blobs/"+fileID));
            }catch (IOException e){
                System.out.println("Failure to copy file to bolbs");
                return;
            }
            try{
                //clear staged area
                Files.delete(Paths.get(".gitlet/staged/"+fileName));
            }catch (IOException e){
                System.out.println("Failure to delete"+fileName);
                return;
            }
        }
        Commit head=commitTree.getHead();
        for(String fileName:head.nameToBlobID.keySet()){
            if(!commitTree.remove.contains(fileName)){
                File file=new File(fileName);
                newCommit.nameToBlobID.put(fileName,Utils.sha1(Utils.readContents(file)));
            }
        }
        commitTree.headID=Commit.hashCommit(newCommit);
        commitTree.branches.put(commitTree.currentBranch,commitTree.headID);

        //clear staged in CommitTree(in for iteration, we clear the file dir, but the array list in CommitTree remains
        commitTree.staged=new ArrayList<>();
        commitTree.remove=new ArrayList<>();
        Commit.serialization(newCommit,commitTree.headID);
    }

    public static void commit(String... args){
        if(args.length==1&&args[1]==""){
            System.out.println("Please enter a commit message");
        }else if(args.length==2){
            commit(args[1]);
        }
    }
    public static void rm(String name){
        Commit head=commitTree.getHead();
        if(head.nameToBlobID.containsKey(name)){
            try{
                if(commitTree.staged.contains(name)){
                    commitTree.staged.remove(name);
                    Files.delete(Paths.get(".gitlet/staged/"+name));
                }
                commitTree.remove.add(name);
                Files.delete(Paths.get(name));
            }catch (IOException e){
                commitTree.remove.add(name);
                System.out.println("Failure to delete"+name+" from directory");
            }
        }else if (commitTree.staged.contains(name)){
            try{
                commitTree.staged.remove(name);
                Files.delete(Paths.get(".gitlet/staged/"+name));
            }catch (IOException e){
                System.out.println("Failure to delete file");
            }
        }else{
            //not tracked nor in stage area
            System.out.println("No reason to remove the file.");
        }
    }
    public static void log(){
        Commit pointer=commitTree.getHead();
        String ptrID=commitTree.headID;
        while(pointer.parentID!=null){
            System.out.println("===");
            System.out.println("Commit "+ptrID);
            System.out.println(pointer.getCommitTime());
            System.out.println(pointer.getMessage());
            System.out.println();
            ptrID=pointer.getParentID();
            pointer=Commit.hashToCommit(pointer.getParentID());
        }
    }

    public static void globalLog(){
        //With the help of iterator, we can easily travese every commit, however
        //we need to avoid print the same element. Thus, we can add element we've
        //printed to a set, and check if the incoming elements has been printed or not.
        HashSet<String> alreadyPrinted=new HashSet<>();
        //we first traverse each branch, then each commit in each branch
        for(String branchID: commitTree.branches.values()){
            Commit pointer=Commit.hashToCommit(branchID);
            String ptrID=branchID;
            // then travese this branch,which is the same as log
            while(pointer.parentID!=null&&!alreadyPrinted.contains(ptrID)){
                System.out.println("===");
                System.out.println("Commit "+ptrID);
                System.out.println(pointer.getCommitTime());
                System.out.println(pointer.getMessage());
                System.out.println();
                ptrID=pointer.getParentID();
                pointer=Commit.hashToCommit(pointer.getParentID());
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

    public static void find(String message){
        //this is similar to global log, just add a check and also, avoid duplicate printing
        HashSet<String> alreadyPrinted=new HashSet<>();
        boolean flag=false;
        for(String branchID:commitTree.branches.values()){
            Commit pointer=Commit.hashToCommit(branchID);
            String ptrID=branchID;
            // then travese this branch,which is the same as log
            while(pointer.parentID!=null&&!alreadyPrinted.contains(ptrID)){
                if(message.equals(pointer.getMessage())){
                    System.out.println(pointer.getCommitID());
                    flag=true;
                }
                alreadyPrinted.add(pointer.getCommitID());
                pointer=Commit.hashToCommit(pointer.getParentID());
            }
            if(!alreadyPrinted.contains(pointer.getCommitID())){
                if(message.equals(pointer.getMessage())){
                    System.out.println(pointer.getCommitID());
                    flag=true;
                }
                alreadyPrinted.add(pointer.getCommitID());
            }
        }
        if(!flag)
            System.out.println("Found no commit with that message.");
    }
    public static void status(){
        System.out.println("=== Branches ===");
        for(String sub:commitTree.branches.keySet()){
            if(sub.equals(commitTree.currentBranch)){
                System.out.println("*"+sub);
            }else{
                System.out.println(sub);
            }
        }
        System.out.println("\n");
        System.out.println("=== Staged Files ===");
        for(String file: commitTree.staged){
            System.out.println(file);
        }
        System.out.println("\n");
        System.out.println("=== Removed Files ===");
        for(String file:commitTree.remove){
            System.out.println(file);
        }
        System.out.println("\n");
        System.out.println("=== Modifications Not Staged For Commit ===");
        /*citation:https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
        * citation: https://www.tutorialspoint.com/java/io/file_isdirectory.htm
        * citation: https://www.tutorialspoint.com/java/io/file_ishidden.htm
        * citation:https://stackoverflow.com/questions/14853402/both-file-isfile-and-file-isdirectory-is-returning-false
        * */
        File folder=new File(System.getProperty("user.dir"));
        File[] listOfFiles=folder.listFiles();
        HashSet<String> modifiedFile=new HashSet<>();
        //we judge via the file's sha
        for(String fileName:commitTree.getHead().nameToBlobID.keySet()){
            boolean containedInDir=false;
            for(File file:listOfFiles ){
                if(!file.isDirectory()&&!file.isHidden()){
                    if(file.getName().equals(fileName)){//if fileName match, compare sha
                        String sha=Utils.sha1(Utils.readContents(file));
                        if(!commitTree.getHead().nameToBlobID.get(fileName).equals(sha)&&!commitTree.staged.contains(fileName)){
                            modifiedFile.add(fileName+" (modified)");
                        }else if(commitTree.staged.contains(fileName)){
                            File newFile=new File(".gitlet/staged/"+fileName);
                            String stagedSha=Utils.sha1(Utils.readContents(newFile));
                            if(!stagedSha.equals(sha)){
                                modifiedFile.add(fileName+" (modified)");
                            }
                        }
                        containedInDir=true;
                    }
                }
            }
            //there's a special case: the file is newly added to working dir, but not added to staged area
            if(!commitTree.staged.contains(fileName)&&!containedInDir&&!commitTree.remove.contains(fileName)){
                modifiedFile.add(fileName+" (delete)");
            }
        }
        for(String fileName:modifiedFile){
            System.out.println(fileName);
        }
        System.out.println("\n");
        System.out.println("=== Untracked Files ===");
        //Untracked to be added, it seems that I miss this feature in commitTree or commit
    }
    public static void checkout(String... args){
        if(args.length==2){
            if(!commitTree.branches.containsKey(args[1])){
                System.out.println("No such branch exists.");
                return;
            }
            String branchID=commitTree.branches.get(args[1]);
            Commit newHead=Commit.hashToCommit(branchID);
            if(args[1].equals(commitTree.currentBranch)){
                System.out.println("No need to checkout the current branch.");
                return;
            }else{
                for(String file:newHead.nameToBlobID.keySet()){
                    if(commitTree.untracked.contains(file)){
                        System.out.println("here is an untracked file in the way; delete it or add it first.");
                        return;
                    }
                }
            }
            commitTree.currentBranch=args[1];
            Commit head=commitTree.getHead();
            for(String file:head.nameToBlobID.keySet()){
                try{
                    Files.delete(Paths.get(file));
                }catch(IOException e){
                    System.out.println("Cannot delete current branch");
                }
            }
            commitTree.headID=branchID;
            for(String file:newHead.nameToBlobID.keySet()){
                try{
                    Files.copy(Paths.get(".gitlet/blobs/"+newHead.nameToBlobID.get(file)),Paths.get(file));
                }catch (IOException e){
                    System.out.println("Cannot copy branch ");
                }
            }
            commitTree.staged=new ArrayList<>();
        }
        else if(args.length==3&&args[1].equals(("--"))){
            Commit head=commitTree.getHead();
            String name=args[2];
            if(head.nameToBlobID.containsKey(name)){
                try{
                    Files.copy(Paths.get(".gitlet/blobs/"+head.nameToBlobID.get(name)),Paths.get(name),StandardCopyOption.REPLACE_EXISTING);
                }catch (IOException e){
                    System.out.println("Error copying file");
                }
            }else{
                System.out.println("File does not exist in that commit.");
            }
        }else if(args.length==4&&(args[2].equals(("--")))){
            String commitID=args[1];
            String name=args[3];
            for(String branchID:commitTree.branchToCommit.values()){
                Commit pointer=Commit.hashToCommit(branchID);
                String pointerID=branchID;
                if(pointerID.equals(commitID)||pointerID.substring(0,commitID.length()).equals(commitID)){
                    if(pointer.nameToBlobID.containsKey(name)){
                        try{
                            Files.copy(Paths.get(".gitlet/blobs/" + pointer.nameToBlobID.get(name)),
                                    Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
                        }catch (IOException e){
                            System.out.println("Error copying file");
                        }
                    }else{
                        System.out.println("File does not exist in that commit.");
                    }
                    return;
                }else if(pointerID.length()<commitID.length()){
                    System.out.println("No commit with that id exits");
                    return;
                }
                pointerID=pointer.parentID;
                if(pointerID==null) {
                    break;
                }
                pointer=Commit.hashToCommit(pointer.parentID);

            }
            System.out.println("No commit with that id exits");
        }else{
            System.out.println("Incorrect operands.");
        }
    }
    public static void branch(String... args){
        if(args.length==1){
            System.out.println("Please enter a name for the branch");
        }else if(args.length==2){
            String name=args[1];
            if(!commitTree.branches.containsKey(name)){
                commitTree.branchToCommit.put(name,commitTree.headID);
                commitTree.branches.put(name,commitTree.headID);
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
            if(commitTree.currentBranch.equals(name)){
                System.out.println("Cannot remove the current branch.");
            }else if(!commitTree.branches.containsKey(name)){
                System.out.println("A branch with that name does not exist.");
            }else {
                commitTree.branches.remove(name,commitTree.headID);
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
            for (String branch : commitTree.branchToCommit.keySet()) {
                pointer = Commit.hashToCommit(commitTree.branchToCommit.get(branch));
               pointerID = commitTree.branchToCommit.get(branch);
                while (true) {
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
                    pointer=Commit.hashToCommit(pointerID);
                }
                if(pointerID!=null){
                    resetBranch=branch;
                    break;
                }
            }
            if(pointerID==null){
                System.out.println("No commit with that id exists.");
                return;
            }
            setUntracked();
            for(String fileName:pointer.nameToBlobID.keySet()){
                if(commitTree.untracked.contains(fileName)){
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                    return;
                }
            }
            commitTree.staged=new ArrayList<>();
            commitTree.remove=new ArrayList<>();
            for(String name:pointer.nameToBlobID.keySet()){
                if(pointer.nameToBlobID.containsKey(name)){
                    try{
                        Files.copy(Paths.get(".gitlet/blobs/" + pointer.nameToBlobID.get(name)),
                                Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
                    }catch (IOException e){
                        System.out.println("Error copying file");
                    }
                }else{
                    System.out.println("File does not exist in that commit.");
                }
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
            commitTree.branches.put(resetBranch,pointerID);
            commitTree.headID=pointerID;
        }
    }
    public static void  setUntracked(){
        commitTree.untracked=new HashSet<String>();
        Commit headC=commitTree.getHead();
        File folder=new File(System.getProperty(("user.dir")));
        File[] listOfFiles=folder.listFiles();
        for(File file:listOfFiles){
            if(!file.isHidden()||!file.isDirectory()||headC.nameToBlobID.containsKey(file.getName())||commitTree.staged.contains(file.getName())){
                commitTree.untracked.add(file.getName());
            }
        }
    }
    public static void merge(String... args){
        if(args.length==1){
            System.out.println("Cannot merge a branch with itself.");
            return;
        }else if(args.length==2){
            String name=args[1];
            setUntracked();
            if(name.equals((commitTree.currentBranch))){
                System.out.println("Cannot merge a branch with itself.");
                return;
            }else if(!commitTree.branches.containsKey(name)){
                System.out.println("A branch with that name does not exist.");
                return;
            }
        }
        String name=args[1];
        Commit split=null;
        String pointerID=commitTree.branches.get(name);
        Commit pointer=null;
        HashSet<String> checked=new HashSet<>();
        while (name!= null) {
            pointer = Commit.hashToCommit(pointerID);
            checked.add(pointerID);
            pointerID = pointer.parentID;
        }
        String anotherID=commitTree.branches.get(commitTree.currentBranch);
        Commit anotherPtr;


        while (anotherID != null) {
            anotherPtr = Commit.hashToCommit(anotherID);
            if (checked.contains(anotherID)) {
               split=anotherPtr;
            }
            anotherID = anotherPtr.parentID;
        }
        String splitID=Commit.hashCommit(split);
        String branchID=commitTree.branches.get(name);
        Commit branchHead=Commit.hashToCommit(pointerID);
        Commit head=commitTree.getHead();
        String headID=commitTree.headID;
        String branchName=commitTree.currentBranch;
        if(commitTree.branches.get(name).equals(splitID)){
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }else if(commitTree.remove.size()>0||commitTree.staged.size()!=0){
            System.out.println("You have uncommitted changes.");
            return;
        }else{
            setUntracked();
            for(String file:branchHead.nameToBlobID.keySet()){
                if(commitTree.untracked.contains(file)){
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                    return;
                }
            }
        }
        if(commitTree.headID.equals(splitID)){
            checkout(name);
            commitTree.headID=headID;
            commitTree.currentBranch=branchName;
            System.out.println("Current branch fast-forwarded.");
        }
        //handle merge conflict
    }

}
