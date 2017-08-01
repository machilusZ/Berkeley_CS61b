package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by yunan on 7/16/17.
 */
public class CommitTree implements Serializable {
    private String headID;
    private String currentBranch;
    private HashMap<String, String> branches= new HashMap<>();
    private ArrayList<String> staged = new ArrayList<>();
    private ArrayList<String> remove =new ArrayList<>();
    private HashMap<String, String> branchToCommit= new HashMap<>();
    private HashSet<String> rmCommit = new HashSet<>();
    private HashSet<String> untracked = new HashSet<>();

    //getter method
    public Commit getHead() {
        return Commit.hashToCommit(headID);
    }
    public String getCurrentBranch() {return this.currentBranch;}
    public String getHeadID(){return  this.headID;}
    public HashMap<String, String> getBranches() {return this.branches;}
    public ArrayList<String> getStaged() {return  this.staged;}
    public HashMap<String, String> getBranchToCommit() {return this.branchToCommit;}
    public ArrayList<String> getRemove() {return this.remove;}
    public HashSet<String> getRmCommit() {return  this.rmCommit;}
    public HashSet<String> getUntracked() {return  this.untracked;}


    public void setHeadID(String hi) {this.headID = hi;}
    public void setBranches(String str1, String str2) {this.branches.put(str1, str2);}
    public void setCurrentBranch(String str) {this.currentBranch = str;}
    public void setBranchToCommit(String str1, String str2) {this.branchToCommit.put(str1,str2);}

    //Manipulations
    public void removeRemove(String name) {this.remove.remove(name);}
    public void removeAdd(String name) {this.remove.add(name);}
    public void rmCommitAdd(String name) {this.rmCommit.add(name);}
    public void rmCommitRemove(String name) {this.rmCommit.remove(name);}
    public void stagedAdd (String name) {this.staged.add(name); }
    public void stageRemove (String name) {this.staged.remove(name);}
    public void branchPut(String str1, String str2) {this.branches.put(str1, str2);}
    public void branchToCommitPut(String str1, String str2){branchToCommit.put(str1, str2);}
    public void resetStaged() {this.staged=new ArrayList<>();}
    public void resetRemove() {this.remove=new ArrayList<>();}
    public void resetUntracked(){this.untracked=new HashSet<>();}
    public void untrackedAdd(String name) {this.untracked.add(name);}
    //Serialization methods
    public static void serialization(CommitTree t) {
        try {
            ObjectOutput output= new ObjectOutputStream(new FileOutputStream(".gitlet/tree.ser"));
            output.writeObject(t);
            output.close();
        } catch (IOException e){
            System.out.println("Failure to serialize commit tree.");
        }
    }
    public static CommitTree deserialization() {
        CommitTree t;
        try {
            ObjectInput input= new ObjectInputStream(new FileInputStream(".gitlet/tree.ser"));

            t=(CommitTree) input.readObject();
            input.close();

        }catch(IOException | ClassNotFoundException e){
            System.out.println("Failure to deserialization");
            t=null;
        }
        return t;
    }
    public void upUntracked(){
        this.untracked = new HashSet<>();
        Commit head = this.getHead();
        File folder = new File(System.getProperty("user.dir"));
        File[] fileLists=folder.listFiles();
        for(File file : fileLists) {
            if(!(file.isDirectory() || file.isHidden()
                    || head.nameToBlobID.containsKey(file.getName())
                    || this.staged.contains(file.getName()))) {
                this.untracked.add(file.getName());

            }
        }
    }

    public void searchFile(String name, Commit commit){
        if(commit.nameToBlobID.containsKey(name)){
            try {
                Files.copy(Paths.get(".gitlet/blobs/"+commit.nameToBlobID.get(name)), Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
            } catch(IOException e) {
                System.out.println("Error copying files");
            }
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }
    public boolean upFile(File file, String name) {
        String hash=Utils.sha1(Utils.readContents(file));
        Commit head=this.getHead();
        if(head.nameToBlobID.containsKey(name)) {
            if(head.nameToBlobID.get(name).equals(hash)) {
                return false;
            } else {
                this.rmCommit.add(name);
                return true;
            }
        }
        return true;
    }

}
