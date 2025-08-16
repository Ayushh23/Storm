# Storm 🌤️

Storm is a sleek and modern Android weather application built with Kotlin. It provides real-time weather data, beautiful animations, and an intuitive user interface to keep you informed about the weather conditions anywhere in the world.

## 🌟 Features

- **Real-Time Weather Data:** Get up-to-the-minute weather details for any city or for your current location.
- **Dynamic UI:** The application's background and animations change dynamically to reflect the current weather conditions (e.g., sunny, cloudy, rainy, or snowy).
- **Comprehensive Details:** Displays essential information including temperature, humidity, wind speed, sunrise/sunset times, atmospheric pressure, and max/min temperatures.
- **Location-Based Forecasts:** Utilizes the Fused Location API to automatically fetch and display weather for the user's current location upon granting permission.
- **City Search Functionality:** A clean and simple search bar allows users to look up weather information for any city worldwide.

## 📸 Screenshots
<p align="center">
  <img src="screenshot/storm1.jpg" width="300px" style="display: inline-block; margin-right:10px;" />
<img src="screenshot/storm2.jpg" width="300px" style="display: inline-block; margin-right:10px;" />
<img src="screenshot/storm3.jpg" width="300px" style="display: inline-block;" />

</p>

## 🛠️ Tech Stack & Dependencies

- **Kotlin:** The primary programming language for building the app.
- **Android SDK:** For native Android development.
- **OpenWeatherMap API:** Used as the source for all weather data.
- **Retrofit:** A type-safe HTTP client for Android to handle API requests.
- **Gson:** For parsing JSON data returned by the OpenWeatherMap API.
- **Lottie:** Renders beautiful, high-quality vector animations for different weather conditions.
- **Fused Location Provider API:** For efficiently retrieving the device's location.
- **ViewBinding:** To access views in the layout files with null safety.

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- Android Studio
- An API key from [OpenWeatherMap](https://openweathermap.org/api)

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/ayushh23/storm.git
   ```
2. **Open the project in Android Studio.**

3. **Add your OpenWeatherMap API Key:**
   - Navigate to `app/src/main/java/com/example/storm/MainActivity.kt`.
   - Locate the `fetchweatherdata` function.
   - Replace the placeholder API key in the following line with your own key:
     ```kotlin
     val response = retrofit.getWeatherData(cityName, "YOUR_API_KEY", "metric")
     ```

4. **Build and run the application on an Android device or emulator.**


