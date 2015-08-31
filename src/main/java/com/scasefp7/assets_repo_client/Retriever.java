package com.scasefp7.assets_repo_client;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class Retriever 
{
	private static String baseURL = "http://109.231.121.125:8080/s-case/assetregistry";
	
	private static String projectName = "eng";
	
    public static void main( String[] args )
    {
        System.out.println( "Starting Retrieval" );
        Client client = ClientBuilder.newClient();
        try {
        	System.out.println("***Checking out assets repo api version***");
        	Response response = client.target(baseURL + "/version").request().get();
        	if(response.getStatus() != 200) throw new RuntimeException("Failed to get version");
        	else System.out.println(response.readEntity(String.class));
        	System.out.println("***Get " + projectName + " ***");
        	String project = client.target(baseURL + "/project/" + projectName).request().get(String.class);
        	System.out.println(project);
        	System.out.println("***Parse and display the requirements***");
        	JSONObject obj = new JSONObject(project);
        	JSONArray arr = obj.getJSONArray("artefacts");
        	for(int i = 0; i < arr.length(); i++) {
        		JSONObject artefact = arr.getJSONObject(i);
        		if(artefact.getString("type").equalsIgnoreCase("TEXTUAL")) {
        			JSONArray payloads = artefact.getJSONArray("payload");
        			for(int j = 0; j < payloads.length(); j++) {
        				if(payloads.getJSONObject(j).getString("type").equalsIgnoreCase("TEXTUAL")) {
        					String requirement = new String(Base64.getDecoder().decode(payloads.getJSONObject(j).getString("payload"))); 
        					System.out.println(requirement);
        				}
        			}
        		}
        	}
        	System.out.println("***Make a search by type***");
        	String search = client.target(baseURL + "/artefact/search?type='TEXTUAL'").request().get(String.class);
        	arr = new JSONArray(search);
        	System.out.println("Artefacts found: " + arr.length());
        	System.out.println("***Make a search by query***");
        	byte[] bytes = "user".getBytes(Charset.forName("UTF-8"));
//        	String byteArray = new String();
//        	for(byte b : bytes) {
//        		byteArray += b;
//        		byteArray += ",";
//        	}
//        	byteArray = byteArray.substring(0, byteArray.lastIndexOf(","));
        	search = client.target(baseURL + "/artefact/search?query=" + Base64.getEncoder().encodeToString(bytes)).request().get(String.class);
        	System.out.println(search);
        } finally {
        	client.close();
        }
    }
}
