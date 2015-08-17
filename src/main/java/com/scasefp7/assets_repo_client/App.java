package com.scasefp7.assets_repo_client;

import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Client client = ClientBuilder.newClient();
        try {
        	System.out.println("***Checking out assets repo api version***");
        	Response response = client.target(baseURL + "/version").request().get();
        	if(response.getStatus() != 200) throw new RuntimeException("Failed to get version");
        	System.out.println(response);
        	System.out.println("***Get thedemo proejct***");
        	String project = client.target(baseURL + "/project/thedemo").request().get(String.class);
        	System.out.println(project);
        	byte[] bytes = "The user must be able to create a bookmark.".getBytes(Charset.forName("UTF-8"));
        	String byteArray = new String();
        	for(byte b : bytes) {
        		byteArray += b;
        		byteArray += ",";
        	}
        	byteArray = byteArray.substring(0, byteArray.lastIndexOf(","));
//        	System.out.println(byteArray);
        	String json = "{"
        		+ "\"projectName\": \"thedemo\","
        		+ "\"type\": \"TEXTUAL\","
        		+ "\"payload\": ["
        		+ "{\"type\":\"TEXTUAL\","
        			+ "\"format\":\"TEXT_UTF8\","
        			+ "\"name\": \"functional\","
        			+ "\"payload\": ["
        				+ byteArray
        				+ "]"
        			+ "}"
        		+ "]}";
        	System.out.println(json);
        	response = client.target(baseURL + "/artefact").request().post(Entity.json(json));
        	System.out.println(response);
        } finally {
        	client.close();
        }
    }
}
