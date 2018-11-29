package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-28.
 */

public class Atmosphere implements JSONPopulator {

    private int pressure;
    private int humidity;

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    @Override
    public void populate(JSONObject data) {
        pressure = data.optInt("pressure");
        humidity = data.optInt("humidity");
    }
}
