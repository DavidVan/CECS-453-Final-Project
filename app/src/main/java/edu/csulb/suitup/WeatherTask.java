package edu.csulb.suitup;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David on 11/18/2016.
 */

public class WeatherTask extends AsyncTask<String, Void, String> {

    JsonResponse delegate;
    String json;
    String location;
    String weather;

    public WeatherTask(JsonResponse delegate, String location) {
        this.delegate = delegate;
        this.location = location;
        weather = "";
    }

    public String getWeather() {
        return weather;
    }

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Exception e) {

                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processJSON(result);
    }

}
