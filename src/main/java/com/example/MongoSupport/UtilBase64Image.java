package com.example.MongoSupport;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
 
public class UtilBase64Image {
	public static String encoder(String imagePath) {
		
		File file= null;
		try {
	    file = new File(imagePath);
		}
		catch(Exception ex) {
			System.out.println(ex+"image not present");
			return null;
		}
	    try (FileInputStream imageInFile = new FileInputStream(file)) {
	        // Reading a Image file from file system
	    	String base64Image = "";
	        byte imageData[] = new byte[(int) file.length()];
	    
	        imageInFile.read(imageData);
	        base64Image = Base64.getEncoder().encodeToString(imageData);
	        return base64Image;
	    } catch (FileNotFoundException e) {
	        System.out.println("Image not found" + e);
	        e.printStackTrace();
	    } catch (IOException ioe) {
	        System.out.println("Exception while reading the Image " + ioe);
	    }
	    return null;
	}
	
	public static void decoder(String data, String pathFile,String name) {
		try {
			//String base64Image = data.split(",")[0];
//			System.out.println("path File "+pathFile+"  name = "+ name);
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			File outputfile = new File(pathFile);
			if(!outputfile.exists()) {
				outputfile.mkdirs();
			}
			File out = new File(pathFile,name);
			FileOutputStream oFile = new FileOutputStream(out, false); 
			ImageIO.write(img, "png", oFile);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error while decoding the image");
		}
	}
	
}