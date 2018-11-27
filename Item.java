package com.example.piotrskorupa.raspiweatherapp;

import org.json.JSONObject;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data)
    {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
