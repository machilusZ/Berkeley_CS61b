package gitlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by yunan on 7/16/17.
 */
public class CommitTree implements Serializable {
    public String headID;
    public String currentBranch;
    public HashMap<String,String> branches=new HashMap<>();
    public ArrayList<String> staged=new ArrayList<>();
    public ArrayList<String>remove=new ArrayList<>();
    public HashMap<String,String> branchToCommit=new HashMap<>();
    public HashSet<String> untracked=new HashSet<>();

    //getter method
    public Commit getHead(){
        return Commit.hashToCommit(headID);
    }

    //Serialization methods
    public static void serialization(CommitTree t){
        try{
            ObjectOutput output=new ObjectOutputStream(new FileOutputStream(".gitlet/tree.ser"));
            output.writeObject(t);
            output.close();;
        }catch (IOException e){
            System.out.println("Failure to serialize commit tree.");
        }
    }
    public static CommitTree deserialization(){
        CommitTree t;
        try{
            ObjectInput input=new ObjectInputStream(new FileInputStream(".gitlet/tree.ser"));

            t=(CommitTree) input.readObject();
            input.close();

        }catch(IOException|ClassNotFoundException e){
            System.out.println("Failure to deserialization");
            t=null;
        }
        return t;
    }

}
