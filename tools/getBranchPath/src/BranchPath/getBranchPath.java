package BranchPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class getBranchPath {

    private static String fileName = "build_info.properties";
    private static Properties props = new Properties();
	
    public static void main(String[] args) {    	

        String s;
        File currentDir = new File(".");
    	int position = -1; 
    	String lastPathName = "";
    			
        try {
        	
          String Path = currentDir.getCanonicalPath();
          String FilePath = Path + "/buildVersion/" + fileName;
         
          Process p = Runtime.getRuntime().exec("svn info");
         
          BufferedReader stdInput = new BufferedReader(new
          InputStreamReader(p.getInputStream()));
         
          BufferedReader stdError = new BufferedReader(new
          InputStreamReader(p.getErrorStream()));
         
          while ((s = stdInput.readLine()) != null) {
            System.out.println(s);        	  
            if (s.indexOf("URL:") == 0) {
              position = s.lastIndexOf("/");
              int i = 92;
              String aChar = new Character((char) i).toString();        
        	  if (position == -1) {
                position = s.lastIndexOf(aChar);
        	  }        
        	  //System.out.println("position= "+String.valueOf(position));
        	  lastPathName = s.substring(position+1, s.length());
        	  //System.out.println("lastPathName= "+lastPathName);
           }
  
         }
          
      	FileInputStream ins = new FileInputStream(FilePath);
        
      	props.load(ins);
      
      	props.setProperty("build.branch.number", lastPathName);
      	System.out.println("build.branch.number="+props.getProperty("build.branch.number"));
      
      	props.store(new FileOutputStream(FilePath), null);          
         
        while ((s = stdError.readLine()) != null) {
          System.out.println(s);
        }
         
        System.exit(0);
        }
        catch (Exception e) {
          System.out.println("Exception: "+e.getMessage());
          e.printStackTrace();
          System.exit(-1);
        }
        
       /* File currentDir = new File(".");
        
        try {
        
        	String Path = currentDir.getCanonicalPath();
        	String FilePath = Path + "/buildVersion/" + fileName;
        	
        	int position = Path.lastIndexOf("/");
            int i = 92;
            String aChar = new Character((char) i).toString();        
        	if (position == -1) {
              position = Path.lastIndexOf(aChar);
        	}  
        	
        	String lastPathName = Path.substring(position+1, Path.length());

        	FileInputStream ins = new FileInputStream(FilePath);
        
        	props.load(ins);
        
        	props.setProperty("build.branch.number", lastPathName);
        	System.out.println("build.branch.number="+props.getProperty("build.branch.number"));
        
        	props.store(new FileOutputStream(FilePath), null);
        
        } catch (Exception e) {
     
          System.out.println("Exception "+e.getMessage());
     
          e.printStackTrace();
     
        }        */
        
    }
}
