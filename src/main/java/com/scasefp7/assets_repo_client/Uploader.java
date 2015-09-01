package com.scasefp7.assets_repo_client;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class Uploader 
{
	private static String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";
	
	private static String projectName = "ent";
	
    public static void main( String[] args )
    {
        System.out.println( "Starting Retrieval" );
        Client client = ClientBuilder.newClient();
        try {
        	System.out.println("***Checking out assets repo api version***");
        	Response response = client.target(baseURL + "/version").request().get();
        	if(response.getStatus() != 200) throw new RuntimeException("Failed to get version");
        	else System.out.println(response.readEntity(String.class));
        	System.out.println("***Create the project: " + projectName + "***");
        	String json = "{"
            		+ "\"name\": \"" + projectName + "\""
            		+ "}";
        	response = client.target(baseURL + "/project").request().post(Entity.json(json));
        	System.out.println("Create project status " + response.getStatus());
        	System.out.println("***Uploading requirements***");
        	File f = new File("requirements/" + projectName + ".txt");
        	Scanner scan = new Scanner(f);
            String str = new String();
            while (scan.hasNext()) {
                str = scan.nextLine();
	        	byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
	        	String byteArray = new String();
	        	for(byte b : bytes) {
	        		byteArray += b;
	        		byteArray += ",";
	        	}
	        	byteArray = byteArray.substring(0, byteArray.lastIndexOf(","));
	        	json = "{"
	        		+ "\"projectName\": \""+ projectName +"\","
	        		+ "\"type\": \"TEXTUAL\","
	        		+ "\"description\": \"" + str + "\","
	        		+ "\"payload\": ["
	        		+ "{\"type\":\"TEXTUAL\","
	        			+ "\"format\":\"TEXT_UTF8\","
	        			+ "\"name\": \"functional\","
	        			+ "\"payload\": ["
	        				+ byteArray
	        				+ "]"
	        			+ "}"
	        		+ "]}";
	        	response = client.target(baseURL + "/artefact").request().post(Entity.json(json));
	        	System.out.println("Create artefact status: " + response.getStatus());
            }
        	scan.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
        	client.close();
        }
    }
}
