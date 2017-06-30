package dagger.extension.example.stubs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.search.SearchModel;
import dagger.extension.example.model.today.TodayWeather;

import static dagger.extension.example.stubs.Responses.RESULT.FILTERED_RESULT;
import static dagger.extension.example.stubs.Responses.RESULT.getResult;

public class Responses
{

    public static String createExpectedFilteredResult() {
        return getResult(FILTERED_RESULT);
    }

    public enum RESULT {
        FILTERED_RESULT;
        static String getResult(RESULT type) {
            StringBuilder sb = new StringBuilder();
            switch (type) {
                case FILTERED_RESULT:
                    sb.append("2016-12-23 15:00:00: 3.53°C").append("\n");
                    sb.append("2016-12-23 18:00:00: 1.91°C").append("\n");
                    sb.append("2016-12-23 21:00:00: 2.61°C").append("\n");
                    break;
                default:
                    throw new IllegalArgumentException(
                            String.format("Unknown result type: " + type.name())
                    );
            }
            return sb.toString();
        }
    }

    public enum JSON {

        TODAY_WEATHER,
        TOMORROW_WEATHER,
        THREE_HOUR_FORECAST,
        FORECAST_RESULT,
        FORECAST_WITH_YEAR_CHANGE,
        SEARCH;

        private static Map<JSON, Object[]> map = new HashMap<>();
        static {
            map.put(TODAY_WEATHER, new Object[] {"response/today_weather.json", TodayWeather.class });
            map.put(TOMORROW_WEATHER, new Object[] { "response/tomorrow_weather.json", TomorrowWeather.class});
            map.put(FORECAST_RESULT, new Object[] {"response/forecast_result.json", ThreeHoursForecastWeather.class });
            map.put(FORECAST_WITH_YEAR_CHANGE, new Object[] {"response/forecast_with_year_change.json", ThreeHoursForecastWeather.class });
            map.put(THREE_HOUR_FORECAST, new Object[] {"response/three_hour_forecast.json", ThreeHoursForecastWeather.class });
            map.put(SEARCH, new Object[] {"response/three_hour_forecast.json", SearchModel.class });
        }

        public static InputStream load(JSON type) {
            String name = map.get(type)[0].toString();
            return JSON.class.getClassLoader().getResourceAsStream(name);
        }

        public static <T> Class<T> targetClass(JSON type) {
            return (Class<T>) map.get(type)[1];
        }
    }

    static <T> T jsonToPojo(Class<T> clazz, String response) throws IOException {
        return new WeatherCall<>(clazz, response).execute().body();
    }

    static String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private static ByteArrayOutputStream readFully(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
