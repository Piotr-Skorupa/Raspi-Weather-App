package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-28.
 */

public class Wind implements JSONPopulator {

    public int getSpeed() {
        return speed;
    }

    private int speed;
    @Override
    public void populate(JSONObject data) {
        speed = data.optInt("speed");
    }
}
