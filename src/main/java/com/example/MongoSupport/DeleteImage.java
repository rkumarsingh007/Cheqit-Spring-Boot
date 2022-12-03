package com.example.MongoSupport;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths; 
  
public class DeleteImage 
{ 
	    public static void deleteIfExist(String imageUri) 
	    { 
	            try {
					Files.deleteIfExists(Paths.get(imageUri));
				} catch (Exception e) {
					
				}
	    }
	    public static void deleteWholeFolder(String path) {
	    	File file = new File(path);
			File[] contents = file.listFiles();
		    if (contents != null) {
		        for (File f : contents) {
		            deleteWholeFolder(f.getPath());
		        }
		    }
		    file.delete();
	    }
}