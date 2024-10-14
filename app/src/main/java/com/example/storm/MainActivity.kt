package com.example.storm
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storm.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation()
        } else {
            requestLocationPermission()
        }


        Searchcity()
    }

    private fun Searchcity() {
        val searchView= binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                        fetchweatherdata(query)
                }

                    return true

                }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
            })

    }

    private fun fetchweatherdata(cityName:String) {

        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Apiinterface::class.java)
        val response=retrofit.getWeatherData(cityName,"9e470df2858d288300c1530f03469594","metric")


        response.enqueue(object : retrofit2.Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
            val responseBody=response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature=responseBody.main.temp.toString()
                    val humidity=responseBody.main.humidity
                    val sunrise=responseBody.sys.sunrise.toLong()
                    val windspeed=responseBody.wind.speed
                    val sunset=responseBody.sys.sunset.toLong()
                    val sealevel=responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp=responseBody.main.temp_max
                    val minTemp=responseBody.main.temp_min
                    val feelslike=responseBody.main.feels_like


                    binding.temp.text="$temperature°C"
                    binding.weather.text=condition
                    binding.maxTemp.text="Max Temp:$maxTemp°C"
                    binding.minTemp.text="Min Temp:$minTemp°C"
                    binding.humidity.text="$humidity%"
                    binding.windSpeed.text="$windspeed m/s"
                    binding.condition.text=condition
                    binding.sunRise.text="${time(sunrise)}"
                    binding.sunSet.text="${time(sunset)}"
                    binding.sea.text="$sealevel hpa"
                    binding.feelslike.text="Feels Like:$feelslike°C"
                    binding.cityName.text="$cityName"
                    binding.date.text= SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(Date())
                    binding.day.text=SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())

changeweatherbackground(condition)

                }


            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Location not found", Toast.LENGTH_SHORT).show()


            }

           })
    }

    private fun changeweatherbackground(condition: String) {


        when(condition){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.newsun)
                binding.lottieAnimationView.setAnimation(R.raw.sun2)
            }
                "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                    binding.lottieAnimationView.setAnimation(R.raw.cloud)

                }
            "Light Rain","Drizzle","Moderate Rain","Show","Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)

            }
            else->{
                binding.root.setBackgroundResource(R.drawable.newsun)
                binding.lottieAnimationView.setAnimation(R.raw.sun2)
            }

        }
        binding.lottieAnimationView.playAnimation()


    }

    private fun time(timestamp: Long):String{
     val sdf=SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }


    private fun requestLocationPermission() {
        // Request location permission at runtime
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            val location = task.result
            if (location != null) {
                Log.d("WeatherApp", "Location fetched: Lat=${location.latitude}, Lon=${location.longitude}")

                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses?.get(0)?.locality ?: "Unknown"
                Log.d("WeatherApp", "City name fetched: $cityName")

                fetchweatherdata(cityName)
            } else {
                Log.e("WeatherApp", "Location is null")

                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                fetchweatherdata("Jaipur") // Default to Jaipur if location cannot be fetched

            }
        }
    }
}