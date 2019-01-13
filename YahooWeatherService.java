package com.example.piotrskorupa.raspiweatherapp;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    final String appId = "U3kURH7g";
    final String consumerKey = "dj0yJmk9UDExc1ltMjZsemxtJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PWRj";
    final String consumerSecret = "2084845c743a2c4edb0e7ad7a11b68cde80ed2e5";
    final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

    public YahooWeatherService(WeatherServiceCallback callback, String location)
    {
        this.callback = callback;
        this.location = location;
    }

    void refreshWeather()
    {
        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                long timestamp = new Date().getTime() / 1000;
                byte[] nonce = new byte[32];
                Random rand = new Random();
                rand.nextBytes(nonce);
                String oauthNonce = new String(nonce).replaceAll("\\W", "");

                List<String> parameters = new ArrayList<>();
                parameters.add("oauth_consumer_key=" + consumerKey);
                parameters.add("oauth_nonce=" + oauthNonce);
                parameters.add("oauth_signature_method=HMAC-SHA1");
                parameters.add("oauth_timestamp=" + timestamp);
                parameters.add("oauth_version=1.0");
                // Make sure value is encoded
                try {
                    parameters.add("location=" + URLEncoder.encode(location, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                parameters.add("format=json");
                Collections.sort(parameters);

                StringBuffer parametersList = new StringBuffer();
                for (int i = 0; i < parameters.size(); i++) {
                    parametersList.append(((i > 0) ? "&" : "") + parameters.get(i));
                }

                String signatureString = null;
                try {
                    signatureString = "GET&" +
                            URLEncoder.encode(url, "UTF-8") + "&" +
                            URLEncoder.encode(parametersList.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String signature = null;
                try {
                    SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
                    Mac mac = Mac.getInstance("HmacSHA1");
                    mac.init(signingKey);
                    byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
                    signature = Base64.encodeToString(rawHMAC, Base64.NO_WRAP);
                } catch (Exception e) {
                    System.err.println("Unable to append signature");
                    System.exit(0);
                }

                String authorizationLine = "OAuth " +
                        "oauth_consumer_key=\"" + consumerKey + "\", " +
                        "oauth_nonce=\"" + oauthNonce + "\", " +
                        "oauth_timestamp=\"" + timestamp + "\", " +
                        "oauth_signature_method=\"HMAC-SHA1\", " +
                        "oauth_signature=\"" + signature + "\", " +
                        "oauth_version=\"1.0\"";

                try {
                    URL urlConn = new URL(url + "?location="+location+"&format=json");
                    HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
                    conn.addRequestProperty("Authorization", authorizationLine.trim());
                    conn.addRequestProperty("Yahoo-App-Id", appId);
                    conn.setRequestProperty("Content-Type", "application/json");

                    int status = conn.getResponseCode();

                    Log.e("URL", conn.getRequestMethod());
                    Log.e("URL", conn.getResponseMessage()+ " " + String.format("%s", status));

                    if (status != HttpURLConnection.HTTP_OK)
                    {
                        Log.e("[HTTP_ERROR]",conn.getErrorStream().toString());
                        return String.format("%s", status);
                    }


                    InputStream inputStream = conn.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = buffer.readLine()) != null)
                    {
                        builder.append(line);
                    }

                    return builder.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }
                if (s == null)
                {
                    callback.serviceFailure(new Exception("Pusty response! "));
                    return;
                }

                Log.e("STRING", s);

                try {
                    JSONObject object = new JSONObject(s);

                    Channel channel = new Channel();
                    channel.populate(object.optJSONObject("current_observation"));

                    callback.serviceSucces(channel);

                } catch (JSONException e) {
                    error = e;
                    callback.serviceFailure(new Exception("Yahoo disconnected, press Refresh button"));
                    //e.printStackTrace();1
                } catch (Exception e) {
                    error = e;
                    callback.serviceFailure(error);
                }
            }

        }.execute(location);
    }
}
