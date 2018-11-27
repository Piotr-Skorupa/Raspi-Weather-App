package com.example.piotrskorupa.raspiweatherapp;

/**
 * Created by Piotr Skorupa on 2018-11-27.
 */

public interface WeatherServiceCallback {
    void serviceSucces(Channel channel);
    void serviceFailure(Exception exception);
}
