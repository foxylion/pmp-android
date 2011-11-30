package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;

/**
 * Provides all the JSonRequests that are possible between vHike and webservice
 * outside. 
 * It connects to the webservice and getting the JsonObject. After the JsonObject
 * was get, return the JsonObject to the caller class.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class JSonRequestProvider {

	
	/**
	 * private constructor cause of singleton
	 */
	public JSonRequestProvider() {

	}
	
	/**
	 * Get the JSONData from the webservice
	 * 
	 * @param url indicates which script and which parameters will be
	 * send to the webservice
	 * @return JsonObject to the caller class
	 */
	public static JsonObject getJSONData(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		URI uri;
		InputStream data = null;
		try {
			uri = new URI(url);
			HttpGet method = new HttpGet(uri);
			HttpResponse response = httpClient.execute(method);
			data = response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Reader r = new InputStreamReader(data);
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(r).getAsJsonObject();
		
		return jsonObject;
	}
	
	
	// TODO: 
	// Diese Methode muss man allgemein implementieren, so dass man 
	// nur Parameter parsen muss.
	// Wird bis Freitag aufjedenfall fertig sein.
	public void postData() throws JSONException{  
		String text;
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Constants.WEBSERVICE_URL +"");
		JSONObject json = new JSONObject();

		try {
			// JSON data:
			json.put("name", "Fahmi Rahman");
			json.put("position", "sysdev");

			JSONArray postjson=new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json",json.toString());
			httppost.getParams().setParameter("jsonpost",postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if(response != null)
			{
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				text = sb.toString();
			}

		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
    		// TODO Auto-generated catch block
    	}
	}
	
}
