package com.weather.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.backend.model.DailyForecast;
import com.weather.backend.model.HourlyForecast;
import com.weather.backend.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Weather service that fetches data from Open-Meteo API.
 * Open-Meteo is free and requires no API key!
 */
@Service
public class WeatherService {

    private final WebClient webClient;
    
    @Value("${openmeteo.api.forecast}")
    private String forecastUrl;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Get current weather for a location
     */
    public WeatherResponse getCurrentWeather(double lat, double lon) {
        String url = forecastUrl + "?latitude=" + lat + "&longitude=" + lon +
                "&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code," +
                "pressure_msl,wind_speed_10m,wind_direction_10m,cloud_cover,uv_index" +
                "&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset" +
                "&timezone=auto";

        JsonNode response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return mapToWeatherResponse(response, lat, lon);
    }

    /**
     * Get hourly forecast
     */
    public List<HourlyForecast> getHourlyForecast(double lat, double lon, int hours) {
        String url = forecastUrl + "?latitude=" + lat + "&longitude=" + lon +
                "&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code," +
                "wind_speed_10m,wind_direction_10m,cloud_cover,precipitation_probability" +
                "&forecast_hours=" + hours +
                "&timezone=auto";

        JsonNode response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return mapToHourlyForecast(response, hours);
    }

    /**
     * Get daily forecast
     */
    public List<DailyForecast> getDailyForecast(double lat, double lon, int days) {
        String url = forecastUrl + "?latitude=" + lat + "&longitude=" + lon +
                "&daily=temperature_2m_max,temperature_2m_min,weather_code," +
                "wind_speed_10m_max,wind_direction_10m_dominant," +
                "precipitation_probability_max,precipitation_sum,uv_index_max" +
                "&forecast_days=" + days +
                "&timezone=auto";

        JsonNode response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return mapToDailyForecast(response, days);
    }

    private WeatherResponse mapToWeatherResponse(JsonNode response, double lat, double lon) {
        WeatherResponse weather = new WeatherResponse();
        
        weather.setLatitude(lat);
        weather.setLongitude(lon);
        weather.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Extract location name from timezone (e.g., "Asia/Kolkata" -> "Kolkata")
        String timezone = response.path("timezone").asText("UTC");
        String locationName = timezone.contains("/") ? 
                timezone.substring(timezone.lastIndexOf("/") + 1).replace("_", " ") : timezone;
        weather.setLocationName(locationName);

        // Current weather data
        JsonNode current = response.path("current");
        weather.setTemperature(current.path("temperature_2m").asDouble());
        weather.setFeelsLike(current.path("apparent_temperature").asDouble());
        weather.setHumidity(current.path("relative_humidity_2m").asInt());
        weather.setPressure(current.path("pressure_msl").asDouble());
        weather.setWindSpeed(current.path("wind_speed_10m").asDouble());
        weather.setWindDirection(current.path("wind_direction_10m").asInt());
        weather.setWindDirectionText(getWindDirectionText(current.path("wind_direction_10m").asInt()));
        weather.setCloudCoverage(current.path("cloud_cover").asInt());
        weather.setUvIndex(current.path("uv_index").asInt());
        weather.setUvIndexLevel(getUvIndexLevel(current.path("uv_index").asInt()));
        
        // Weather condition from WMO code
        int weatherCode = current.path("weather_code").asInt();
        weather.setWeatherCondition(getWeatherCondition(weatherCode));
        weather.setWeatherDescription(getWeatherDescription(weatherCode));
        weather.setWeatherIcon(getWeatherIcon(weatherCode));
        
        // Visibility (not in Open-Meteo basic, using estimate)
        weather.setVisibility(10.0); // Default 10km

        // Daily data for min/max and sunrise/sunset
        JsonNode daily = response.path("daily");
        if (daily.has("temperature_2m_max") && daily.path("temperature_2m_max").size() > 0) {
            weather.setTempMax(daily.path("temperature_2m_max").get(0).asDouble());
            weather.setTempMin(daily.path("temperature_2m_min").get(0).asDouble());
        }
        if (daily.has("sunrise") && daily.path("sunrise").size() > 0) {
            weather.setSunrise(formatTime(daily.path("sunrise").get(0).asText()));
            weather.setSunset(formatTime(daily.path("sunset").get(0).asText()));
        }

        // Air quality (not in basic Open-Meteo, using moderate default)
        weather.setAirQualityIndex(50);
        weather.setAirQualityLevel("Moderate");

        return weather;
    }

    private List<HourlyForecast> mapToHourlyForecast(JsonNode response, int hours) {
        List<HourlyForecast> forecasts = new ArrayList<>();
        JsonNode hourly = response.path("hourly");
        
        int count = Math.min(hours, hourly.path("time").size());
        for (int i = 0; i < count; i++) {
            HourlyForecast forecast = new HourlyForecast();
            
            forecast.setTime(hourly.path("time").get(i).asText());
            forecast.setTemperature(hourly.path("temperature_2m").get(i).asDouble());
            forecast.setFeelsLike(hourly.path("apparent_temperature").get(i).asDouble());
            forecast.setHumidity(hourly.path("relative_humidity_2m").get(i).asInt());
            forecast.setWindSpeed(hourly.path("wind_speed_10m").get(i).asDouble());
            forecast.setWindDirection(hourly.path("wind_direction_10m").get(i).asInt());
            forecast.setCloudCoverage(hourly.path("cloud_cover").get(i).asInt());
            forecast.setPrecipitationProbability(hourly.path("precipitation_probability").get(i).asDouble());
            
            int weatherCode = hourly.path("weather_code").get(i).asInt();
            forecast.setWeatherCondition(getWeatherCondition(weatherCode));
            forecast.setWeatherDescription(getWeatherDescription(weatherCode));
            forecast.setWeatherIcon(getWeatherIcon(weatherCode));
            
            forecasts.add(forecast);
        }
        
        return forecasts;
    }

    private List<DailyForecast> mapToDailyForecast(JsonNode response, int days) {
        List<DailyForecast> forecasts = new ArrayList<>();
        JsonNode daily = response.path("daily");
        
        int count = Math.min(days, daily.path("time").size());
        for (int i = 0; i < count; i++) {
            DailyForecast forecast = new DailyForecast();
            
            String dateStr = daily.path("time").get(i).asText();
            forecast.setDate(dateStr);
            
            // Get day name
            LocalDate date = LocalDate.parse(dateStr);
            forecast.setDayName(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            
            forecast.setTempMin(daily.path("temperature_2m_min").get(i).asDouble());
            forecast.setTempMax(daily.path("temperature_2m_max").get(i).asDouble());
            
            // Estimate temperatures at different times of day
            double tempMin = forecast.getTempMin();
            double tempMax = forecast.getTempMax();
            forecast.setTempMorning(tempMin + (tempMax - tempMin) * 0.3);
            forecast.setTempDay(tempMax);
            forecast.setTempEvening(tempMin + (tempMax - tempMin) * 0.6);
            forecast.setTempNight(tempMin);
            
            forecast.setWindSpeed(daily.path("wind_speed_10m_max").get(i).asDouble());
            forecast.setWindDirection(daily.path("wind_direction_10m_dominant").get(i).asInt());
            forecast.setPrecipitationProbability(daily.path("precipitation_probability_max").get(i).asDouble());
            forecast.setPrecipitationAmount(daily.path("precipitation_sum").get(i).asDouble());
            forecast.setUvIndex(daily.path("uv_index_max").get(i).asInt());
            
            // Estimate humidity and cloud coverage
            forecast.setHumidity(60);
            forecast.setCloudCoverage(30);
            
            int weatherCode = daily.path("weather_code").get(i).asInt();
            forecast.setWeatherCondition(getWeatherCondition(weatherCode));
            forecast.setWeatherDescription(getWeatherDescription(weatherCode));
            forecast.setWeatherIcon(getWeatherIcon(weatherCode));
            
            forecasts.add(forecast);
        }
        
        return forecasts;
    }

    private String formatTime(String isoDateTime) {
        try {
            LocalDateTime dt = LocalDateTime.parse(isoDateTime);
            return dt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return isoDateTime;
        }
    }

    private String getWindDirectionText(int degrees) {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                               "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(degrees / 22.5) % 16;
        return directions[index];
    }

    private String getUvIndexLevel(int uvIndex) {
        if (uvIndex <= 2) return "Low";
        if (uvIndex <= 5) return "Moderate";
        if (uvIndex <= 7) return "High";
        if (uvIndex <= 10) return "Very High";
        return "Extreme";
    }

    /**
     * Maps WMO Weather Codes to human-readable conditions
     * See: https://open-meteo.com/en/docs
     */
    private String getWeatherCondition(int code) {
        switch (code) {
            case 0: return "Clear";
            case 1: return "Mainly Clear";
            case 2: return "Partly Cloudy";
            case 3: return "Overcast";
            case 45: case 48: return "Foggy";
            case 51: case 53: case 55: return "Drizzle";
            case 56: case 57: return "Freezing Drizzle";
            case 61: case 63: case 65: return "Rain";
            case 66: case 67: return "Freezing Rain";
            case 71: case 73: case 75: return "Snow";
            case 77: return "Snow Grains";
            case 80: case 81: case 82: return "Rain Showers";
            case 85: case 86: return "Snow Showers";
            case 95: return "Thunderstorm";
            case 96: case 99: return "Thunderstorm with Hail";
            default: return "Unknown";
        }
    }

    private String getWeatherDescription(int code) {
        switch (code) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear sky";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: return "Fog";
            case 48: return "Depositing rime fog";
            case 51: return "Light drizzle";
            case 53: return "Moderate drizzle";
            case 55: return "Dense drizzle";
            case 56: return "Light freezing drizzle";
            case 57: return "Dense freezing drizzle";
            case 61: return "Slight rain";
            case 63: return "Moderate rain";
            case 65: return "Heavy rain";
            case 66: return "Light freezing rain";
            case 67: return "Heavy freezing rain";
            case 71: return "Slight snow fall";
            case 73: return "Moderate snow fall";
            case 75: return "Heavy snow fall";
            case 77: return "Snow grains";
            case 80: return "Slight rain showers";
            case 81: return "Moderate rain showers";
            case 82: return "Violent rain showers";
            case 85: return "Slight snow showers";
            case 86: return "Heavy snow showers";
            case 95: return "Thunderstorm";
            case 96: return "Thunderstorm with slight hail";
            case 99: return "Thunderstorm with heavy hail";
            default: return "Unknown weather";
        }
    }

    private String getWeatherIcon(int code) {
        // Return icon names matching common weather icon sets
        switch (code) {
            case 0: return "clear_day";
            case 1: return "mostly_clear";
            case 2: return "partly_cloudy";
            case 3: return "cloudy";
            case 45: case 48: return "fog";
            case 51: case 53: case 55: return "drizzle";
            case 56: case 57: return "freezing_drizzle";
            case 61: case 63: case 65: return "rain";
            case 66: case 67: return "freezing_rain";
            case 71: case 73: case 75: return "snow";
            case 77: return "snow_grains";
            case 80: case 81: case 82: return "rain_showers";
            case 85: case 86: return "snow_showers";
            case 95: return "thunderstorm";
            case 96: case 99: return "thunderstorm_hail";
            default: return "unknown";
        }
    }
}
