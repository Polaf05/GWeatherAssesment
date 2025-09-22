# Weather App 🌦️

An Android app that shows the current weather and weather history using the [OpenWeather API](https://openweathermap.org/api).  
Built with **Kotlin**, **Jetpack Compose**, **Hilt**, **Coroutines**, and **Retrofit**.

---

## 🔑 API Key Setup

This project requires an OpenWeather API key to run.  
Follow these steps to configure it securely:

1. Open your project’s `local.properties` file (located at the root of your project).
2. Add the following line
     **OPEN_WEATHER_API_KEY="b1777c931de8cad83e6420c855548818"**
This is already configured to be a buildConfigField so as long as you put it in the **local.propeties** it will work.

🚀 Features

Firebase Authentication (Sign In & Registration)

Fetch current weather by location

Displays:

City & Country

Temperature (Celsius)

Sunrise & Sunset time

Weather icons (day/night handling)

Saves weather history locally

Offline support for past data

🛠️ Tech Stack

Kotlin

Jetpack Compose

Hilt (DI)

Retrofit

Coroutines + Flow

Room Database

Firebase Auth

🧪 Testing

Unit tests are included for:

Repository

ViewModels

Flow testing with Turbine

Mocking with MockK
