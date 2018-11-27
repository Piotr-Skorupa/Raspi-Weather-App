package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class Units implements JSONPopulator {

    private String temperature;
    private String pressure;
    private String speed;

    @Override
    public void populate(JSONObject data)
    {
        temperature = data.optString("temperature");
        pressure = data.optString("pressure");
        speed = data.optString("speed");
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSpeed() {
        return speed;
    }
}
