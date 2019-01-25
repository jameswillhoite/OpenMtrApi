package com.openmtrapi.api;

/*
OpenMtr-API
Authors James Willhoite, Jenny Franklin, Matt Thomas
PSTCC Capstone 2019
 */

//Import required
//import com.mattclinard.*;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


//The main api endpoint
@Path("/getURL")
public class OpenmtrApiMain {

    // The Java method will process HTTP GET requests
    @GET
    @Path("/")
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
        String dirPath = new File(".").getCanonicalPath() + DS + "apps" + DS + "expanded" + DS + "openmtr-api.war" + DS + "images";


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

        //Test that the file is an image
        File File = new File(FILE_NAME);
        String mimetype = new MimetypesFileTypeMap().getContentType(File);
        String type = mimetype.split("/")[0];




        return FILE_NAME;

    }


}
