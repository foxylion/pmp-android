package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;

/**
 * Provides all the JSonRequests that are possible between vHike and webservice
 * outside. It connects to the webservice and getting the JsonObject. After the
 * JsonObject was get, return the JsonObject to the caller class.
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

	// TODO:
	// Diese Methode muss man allgemein implementieren, so dass man
	// nur Parameter parsen muss.
	// Wird bis Freitag aufjedenfall fertig sein.
	public static JsonObject doRequest(List<ParamObject> listToParse){
		
		String getParam = "?";
		
		for(ParamObject object : listToParse){
			
			if(!(object.isPost())){
				Log.i("!isPost: " + object.getKey() + ", "+ object.getValue());
				getParam = getParam + object.getKey() + "=" + object.getValue(); 
			}
			getParam = getParam + "&";
		}
		// Cut the last '&' out
		getParam = getParam.substring(0, getParam.length()-1);
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		Log.i("Wird aufgerufen: "+Constants.WEBSERVICE_URL+getParam);
		
		HttpPost httppost = new HttpPost(Constants.WEBSERVICE_URL);
		JsonObject jsonObject = null;

		// Iterate over objects, wich have to be post to the webservice
		for (ParamObject object : listToParse) {
			if((object.isPost())){
				Log.i("isPost: " + object.getKey() +", "+ object.getValue() );
				httppost.getParams().setParameter(object.getKey(),
						object.getValue());				
			}

		}

		// Execute HTTP Post Request
		HttpResponse response;
		try {
			response = httpclient.execute(httppost);

			// for JSON:
			if (response != null) {
				InputStream is = response.getEntity().getContent();

				Reader r = new InputStreamReader(is);
				JsonParser parser = new JsonParser();

				jsonObject = parser.parse(r).getAsJsonObject();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

}
