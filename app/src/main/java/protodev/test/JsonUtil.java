package protodev.test;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jimmy on 24/04/2016.
 */
public class JsonUtil {

    public String getJSONData(String urlString) {
        HttpURLConnection conn = null;
        try {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            // Make a connection
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            // Read data from url
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                                                                             "UTF-8"));
            String jsonString;
            StringBuilder builder = new StringBuilder();
            while ((jsonString = reader.readLine()) != null) {
                builder.append(jsonString);
            }
            jsonString = builder.toString();
            reader.close();
            Log.v("MSG",jsonString);
            // Get json
            return jsonString;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public JSONArray getJSONArray(String urlString, String name) {
        String     jsonData   = getJSONData(urlString);
        JSONObject jsonObject = null;
        JSONArray  jsonArray  = new JSONArray();
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray(name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }



    public JSONObject getJSONObject (String urlString){
        JSONObject jsonObject =null;
        try{
            jsonObject = new JSONObject(getJSONData(urlString));
        } catch (Exception e){}
        return jsonObject;
    }
}
