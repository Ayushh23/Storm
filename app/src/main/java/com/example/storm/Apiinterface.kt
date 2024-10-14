package com.example.storm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiinterface {
@GET("weather")
fun getWeatherData(
    @Query("q") cityName: String,
    @Query("appid") apiKey: String,
    @Query("units") units: String):
        Call<WeatherApp>




}