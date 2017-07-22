package gitlet;
import jdk.nashorn.internal.runtime.URIUtils;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.ObjectInputStream;
import  java.io.ObjectOutputStream;


/**
 * Created by yunan on 7/15/17.
 */
/*
 *citation:https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
 *
 */
public class Commit implements Serializable{
  //Fields
    String parentID;
    String commitTime;
    String commitID;
    String message;
    HashMap<String,String> nameToBlobID;//first string is file name, second is hashcode hashed from contents of bolb
    HashMap<String,File> BlobIDtoFile;//first is ID hashed from file content, second is the real file
    ArrayList<File>untracked;//for rm command
    /*idComponents hashInfo;
    public class idComponents implements Serializable{
      String messageCopy;
      HashMap<String,String> nameToBlobIDCopy;
      public idComponents(){
        messageCopy=message;
        nameToBlobIDCopy=nameToBlobID;
      }
    }
    */
    //Constructors
    //Init Commit, which has no parent
    public Commit(){
      parentID=null;
      /*
      Get time of commit
       */
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      commitTime=sdf.format(new Date());
      message="initial commit";
      //intialize file references
      nameToBlobID=new HashMap<>();

      nameToBlobID=new HashMap<>();

      untracked=new ArrayList<>();
    }
    public Commit(String message,String parent){
        if(message==""){
            System.out.println("Please enter a commit message");
            return;
        }
        this.message=message;
        parentID=parent;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        commitTime=sdf.format(new Date());

    }

    //getter methods
    public String getParentID(){
        return this.parentID;
    }
    public String getCommitTime(){
        return this.commitTime;
    }
    public String getCommitID(){return this.commitID;}
    public String getMessage(){return this.message;}
    public HashMap<String,String>getNameToBlobID(){return this.getNameToBlobID();}
    /*public idComponents getHashInfo(){return this.hashInfo;}*/

    //setter methods
    public void setParent(String parent){
      this.parentID=parent;
    }
    public boolean equals(Commit com){
      boolean result=false;
      if(this.commitID==com.getCommitID()){
        result=true;
      }
      return result;
    }
    /*convert obj to byte array, mainly used to prepare for the serilization of commitHahsinfo*/
    /* This code is inspired from the project page of cs61bl http://www.cs61bl.org/su17/materials/proj/proj2/proj2.html*/
    public static byte[] convertToBytes(Object obj)throws IOException{
        byte[] result=null;
      try{
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        ObjectOutputStream objectStream=new ObjectOutputStream(stream);
        objectStream.writeObject(obj);
        objectStream.close();
        return stream.toByteArray();
      }catch (IOException e){
        System.out.println("Internal error serializing commit.");
      }
      return result;
    }
    public static String hashCommit(Commit commit){
        String result=null;
        try{
            byte[] byteArray=convertToBytes(commit);
            result= Utils.sha1(byteArray);
        }catch (IOException e){
            System.out.println("Failure to convert commit to hash code");
        }
        return result;
    }
    public static Commit hashToCommit(String hash){
        return deserialization(hash);
    }
    //The following two methods are also inspired from project webpage
    public static void serialization(Commit commit, String fileName){
        File outFile=new File(".gitlet/commits/"+fileName+".ser");
        try{
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
        }catch(IOException e){
            System.out.println("Failure to serialize commit");
        }
    }
    public static Commit deserialization(String fileName){
        Commit commit;
        File inFile=new File(fileName);
        try{
            ObjectInputStream inp=new ObjectInputStream(new FileInputStream(inFile));
            commit=(Commit) inp.readObject();
            inp.close();
        }catch (IOException |ClassNotFoundException e){
            System.out.println("Failure to deserialize class");
            commit=null;
        }
        return commit;
    }

}
