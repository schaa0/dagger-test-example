package dagger.extension.example.stubs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
        FORECAST_WITH_YEAR_CHANGE;

        private static Map<JSON, String> map = new HashMap<>();
        static {
            map.put(TODAY_WEATHER, "response/today_weather.json");
            map.put(TOMORROW_WEATHER, "response/tomorrow_weather.json");
            map.put(FORECAST_RESULT, "response/forecast_result.json");
            map.put(FORECAST_WITH_YEAR_CHANGE, "response/forecast_with_year_change.json");
            map.put(THREE_HOUR_FORECAST, "response/three_hour_forecast.json");
        }

        public static InputStream load(JSON type) {
            String name = map.get(type);
            return JSON.class.getClassLoader().getResourceAsStream(name);
        }
    }

    static <T> T jsonToPojo(Class<T> clazz, String response) throws IOException {
        return new WeatherCall<>(clazz, response).execute().body();
    }

    static String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private static byte[] readFullyAsBytes(InputStream inputStream)
            throws IOException {
        return readFully(inputStream).toByteArray();
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
