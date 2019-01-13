package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class Channel implements JSONPopulator {

    private Atmosphere atmosphere;
    private Wind wind;
    private Condition condition;

    public Wind getWind() {
        return wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Condition getCondition() {return condition;}
    @Override
    public void populate(JSONObject data)
    {
        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
