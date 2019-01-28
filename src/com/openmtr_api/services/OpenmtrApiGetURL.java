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
import javax.ws.rs.core.Response;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.mattclinard.openmtr.*;



//The main api endpoint
@Path("/getURL")
public class OpenmtrApiGetURL {
	
	
    // The Java method will process HTTP GET requests
    @GET
    
    @Produces("application/json")
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


        //ToDo regex to validate a url

        String file;
        try {
            file = this.downloadFromURL(url);
        } catch (Exception ex) {
            return rr.error(ex.getMessage(), 400);
        }

        rr.setData("The file was downloaded. " + file);

        return Response.status(200).entity(rr).build();

    }

    /**
     *
     * @param url url to download file
     * @return String The file location
     */
    public String downloadFromURL(String url) throws Exception {
        String DS = File.separator;
        String dirPath = "/images";


        //check to make sure the images folder exsists
        File dir = new File(dirPath);

        if(!dir.exists())
            throw new Exception("Image folder doesn't exsist. ");

        //Build the File Name

        String FILE_NAME = dirPath + DS + url.split("/")[url.split("/").length-1];



        try {
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        
        //Test this file against Matt C's libaray
        byte[] image;
        try {
        	image = this.extractBytes(FILE_NAME);
        } catch(IOException ex) {
        	throw new Exception(ex.getMessage());
        }
        
        String meterRead = OpenMeter.getMeterRead(image);
        

        return meterRead;

    }
    
    //Return a byte[] from the given image
    //Thank you https://stackoverflow.com/questions/3211156/how-to-convert-image-to-byte-array-in-java
    public byte[] extractBytes(String imageName) throws IOException {
    	//Open the Image
    	File imagePath = new File(imageName);
    	BufferedImage bufferedImage = ImageIO.read(imagePath);
    	
    	WritableRaster raster = bufferedImage.getRaster();
    	DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
    	
    	return data.getData();
    }


}
