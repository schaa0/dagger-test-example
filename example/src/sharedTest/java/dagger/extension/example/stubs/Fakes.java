package dagger.extension.example.stubs;

import android.location.Location;

import java.io.IOException;
import java.io.InputStream;

import static dagger.extension.example.stubs.Responses.jsonToPojo;
import static dagger.extension.example.stubs.Responses.readFullyAsString;

public class Fakes {

    public static Location location(double lng, double lat) {
        Location location = new Location("");
        location.setLongitude(lng);
        location.setLatitude(lat);
        return location;
    }

    public static DateProviderStub dateProvider(int year, int month, int day, int hour, int minutes, int seconds) {
        return new DateProviderStub(year, month, day, hour, minutes, seconds);
    }

    public static <T> T fakeResponse(Responses.JSON type) throws IOException {
        InputStream is = Responses.JSON.load(type);
        return jsonToPojo(Responses.JSON.targetClass(type), readFullyAsString(is, "UTF-8"));
    }
}
