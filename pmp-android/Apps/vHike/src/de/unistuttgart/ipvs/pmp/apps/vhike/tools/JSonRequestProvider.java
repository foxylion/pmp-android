package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	
}
