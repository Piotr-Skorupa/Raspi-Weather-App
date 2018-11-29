package com.example.piotrskorupa.raspiweatherapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public YahooWeatherService(WeatherServiceCallback callback, String location)
    {
        this.callback = callback;
        this.location = location;
    }

    void refreshWeather()
    {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", location);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = buffer.readLine()) != null)
                    {
                        builder.append(line);
                    }

                    return builder.toString();

                }catch(MalformedURLException e){
                    error = e;
                }catch (IOException e)
                {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                if (s == null && error != null)
                {
                    callback.serviceFailure(error);
                    return;
                }

                try{
                    JSONObject object = new JSONObject(s);

                    JSONObject queryResult = object.optJSONObject("query");
                    int count =queryResult.optInt("count");

                    if (count == 0)
                    {
                        Exception exceptionBadLocation = new Exception("Your town doesn't exsist!");
                        callback.serviceFailure(exceptionBadLocation);
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResult.optJSONObject("results").optJSONObject("channel"));

                    callback.serviceSucces(channel);

                }catch (JSONException e)
                {
                    error = e;
                    callback.serviceFailure(error);
                    //e.printStackTrace();
                }catch (Exception e)
                {
                    error = e;
                    callback.serviceFailure(error);
                }
            }

        }.execute(location);
    }
}
