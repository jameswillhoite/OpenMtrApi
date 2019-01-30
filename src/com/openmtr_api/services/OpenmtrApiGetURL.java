package com.openmtr_api.services;


import javax.imageio.ImageIO;

/*
OpenMtr-API
Authors James Willhoite, Jenny Franklin, Matt Thomas
PSTCC Capstone 2019

This class will get the URL from the user to download an image
 */

//Import required
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.servlet.ServletContext;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mattclinard.openmtr.*;



//The main api endpoint
@Path("/getURL")
public class OpenmtrApiGetURL {
	
	@Context
    UriInfo uriInfo;
	@Context
	ServletContext servletContext;
	
	public OpenmtrApiGetURL() {
		
	}
	
	
    // The Java method will process HTTP GET requests
    @GET
    
    @Produces(MediaType.APPLICATION_JSON)
    public Response processFromURL(@QueryParam("url") String url) {
        //Initialize a Response Class
        ReturnResponse rr = new ReturnResponse();

        try {
            //Check to see if a url was given
            if (url.isEmpty())
                return rr.error("No URL was given", 400);
        } catch (NullPointerException ex) {
            return rr.error("Parameter URL was not given", 400);
        }


        //Test and make sure the given url is a valid url
        //Must start with http(s):// for a valid URL
        Pattern urlReg = Pattern.compile("^((http[s]?|ftp):\\/\\/){1,1}\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$");
        Matcher m = urlReg.matcher(url);
        if(!m.find())
        	return rr.error("The given url is invalid. Please provide a format of http(s)://domain.com/image.extension", 400);
        

        String file = "";
        try {
            file = this.downloadFromURL(url);
    	} catch (Exception ex) {
           return rr.error("Download from URL: " + ex.getMessage(), 400);
        }

        rr.setData("The file was downloaded. " + file);

        return rr.success();

    }

    /**
     *
     * @param url url to download file
     * @return String The file location
     */
   
    public String downloadFromURL(String url) throws Exception {
        String DS = File.separator;
        String dirPath = servletContext.getRealPath("/upload");


        //check to make sure the images folder exsists
        File dir = new File(dirPath);

        if(!dir.exists())
            throw new Exception("Image folder doesn't exsist. " );

        //Build the File Name
        String FILE_NAME = url.split("/")[url.split("/").length-1];
        String FILE_PATH = dirPath + DS + FILE_NAME;
        

        //Get the image from the URL
        try {
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new Exception("Download from url: " + ex.getMessage());
        }
        
        
        //Make sure the file was download
        File imageFile = new File(FILE_PATH);
        if(!imageFile.exists())
        	throw new Exception("File was not download.");
                
        
        //Test this file against Matt C's library
        byte[] image = null;
        try {
        	image = this.extractBytes(FILE_PATH);
        } catch(IOException ex) {
        	//imageFile.delete();
        	throw new Exception("Extract Bytes: " + ex.getMessage());
        }
        
        
        String meterRead = "";
        try {
        	meterRead = OpenMeter.getMeterRead(image);
        } catch (Exception ex) {
        	//imageFile.delete();
        	throw new Exception("OpenMeter Error: " + ex.getMessage());
        }
        

        return meterRead;

    }
    
    //Return a byte[] from the given image
    //Thank you https://stackoverflow.com/questions/3211156/how-to-convert-image-to-byte-array-in-java
    public byte[] extractBytes(String imagePath) throws Exception {
    	//Get the image location
    	//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//URL resource = classLoader.getResource(imageName);
		//String imageLocation = resource.getPath();
		
		//Create the File variable
    	File image = null;
    	BufferedImage bufferedImage = null;
    	WritableRaster raster = null;
    	DataBufferByte data = null;
    	try {
	    	image = new File(imagePath);
	    	bufferedImage = ImageIO.read(image);
	    	
	    	raster = bufferedImage.getRaster();
	    	data = (DataBufferByte) raster.getDataBuffer();
    	} catch (Exception ex) {
    		throw new Exception(ex.getMessage());
    	}
    	
    	return data.getData();
    }
    
   

}
