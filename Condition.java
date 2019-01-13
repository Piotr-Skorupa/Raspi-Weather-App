package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class Condition implements JSONPopulator{

    private int code;
    private int temperature;
    private String description;

    @Override
    public void populate(JSONObject data)
    {
        code = data.optInt("code");
        temperature = data.optInt("temperature");
        description = data.optString("text");

    }

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }
}
