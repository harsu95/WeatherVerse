# WeatherVerse

A modern Android weather application built with Java, featuring real-time weather data, hourly and daily forecasts, and a beautiful Material Design 3 UI.

## Features

- 🌤️ Current weather conditions
- 📊 Hourly and daily forecasts
- 🔍 Location search with autocomplete
- 📍 GPS-based automatic location detection
- 🎨 Material Design 3 UI
- 🌙 Dynamic weather icons
- 💾 Offline caching with Room database

## Tech Stack

### Android App
- **Language**: Java
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM with Hilt DI
- **Networking**: Retrofit + OkHttp
- **Database**: Room
- **UI**: Material Design 3 + ViewBinding

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Weather API**: Open-Meteo (Free, no API key required)

## Project Structure

```
app/                    # Android application module
├── src/main/java/     # Java source files
│   └── com/weather/app/
│       ├── di/        # Dependency Injection
│       ├── data/      # Data layer (repositories, APIs, database)
│       ├── domain/    # Domain models and DTOs
│       ├── ui/        # UI layer (fragments, viewmodels, adapters)
│       └── location/  # Location services
└── src/main/res/      # Resources (layouts, drawables, values)

backend/               # Spring Boot backend
├── src/main/java/    # Java source files
│   └── com/weather/backend/
│       ├── controller/  # REST controllers
│       ├── service/     # Business logic
│       ├── dto/         # Data transfer objects
│       └── config/      # Configuration classes
└── src/main/resources/  # Application properties
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Running the Backend
```bash
cd backend
./gradlew bootRun
```
The backend will start at `http://localhost:8080`

### Building the Android App
```bash
./gradlew assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

### Running in Android Studio
1. Open the project in Android Studio
2. Sync Gradle files
3. Start the backend server
4. Run the app on an emulator or device

## API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /api/v1/weather/current?lat={lat}&lon={lon}` | Get current weather |
| `GET /api/v1/weather/forecast/hourly?lat={lat}&lon={lon}` | Get hourly forecast |
| `GET /api/v1/weather/forecast/daily?lat={lat}&lon={lon}` | Get daily forecast |
| `GET /api/v1/location/search?query={query}` | Search locations |
| `GET /api/v1/location/autocomplete?query={query}` | Location autocomplete |


## License

This project is licensed under the MIT License.
