package dagger.extension.example.view;

import android.location.Location;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;

import dagger.extension.example.R;
import dagger.extension.example.di.TestWeatherApplication;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.today.TodayWeather;
import dagger.extension.example.service.DateProvider;
import dagger.extension.example.service.ImageRequestManager;
import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.testcase.DaggerUiAutomatorTestCase;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.test.DaggerActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static dagger.extension.example.stubs.Responses.jsonToPojo;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import dagger.extension.example.stubs.*;
import io.reactivex.Observable;

@RunWith(AndroidJUnit4.class)
public class SimpleTest extends DaggerUiAutomatorTestCase<TestWeatherApplication>
{

    public static final double FAKE_LONGITUDE = 1.0;
    public static final double FAKE_LATITUDE = 1.0;

    @Mock
    WeatherApi weatherApi;
    @Mock
    LocationProvider locationProvider;

    private MainActivity mActivity;

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        MockitoAnnotations.initMocks(this);
        doReturn(this.fakeLocation()).when(locationProvider).lastLocation();
        doReturn(Observable.empty()).when(locationProvider).onNewLocation();

        dependencies().decorateComponentSingleton()
                      .withDateProvider(() -> new StubDateProvider(2017, Calendar.JANUARY, 22, 23, 0, 0))
                      .withWeatherApi(endpointUrl -> weatherApi)
                      .withLocationProvider(locationManager -> locationProvider);
    }

    private Location fakeLocation()
    {
        Location location = new Location("");
        location.setLongitude(FAKE_LONGITUDE);
        location.setLatitude(FAKE_LATITUDE);
        return location;
    }

    @Test
    public void itShouldShowWeatherForNextDay() throws IOException
    {

        doReturn(Observable.empty())
                .when(weatherApi).getCurrentWeather(anyDouble(), anyDouble(), anyString(), anyString(), anyString());
        doReturn(Observable.empty())
                .when(weatherApi).getTomorrowWeather(anyDouble(), anyDouble(), anyString(), anyInt(), anyString(), anyString());

        when(weatherApi.getForecastWeather(eq(FAKE_LONGITUDE), eq(FAKE_LATITUDE), eq("metric"), eq("de"), anyString())).thenReturn(
                Observable.just(jsonToPojo(ThreeHoursForecastWeather.class, Responses.THREE_HOUR_FORECAST))
        );

        mActivity = rule.launchActivity(null);
        allowPermissionsIfNeeded();

        ViewInteraction appCompatTextView = onView(
                allOf(withText(R.string.tomorrow), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imageView), isDisplayed()));
        appCompatImageView.perform(click());

        String threeHourForecastDataAsString =
                "2017-01-23 00:00:00: -11.55°C\n" +
                        "2017-01-23 03:00:00: -12.0°C\n" +
                        "2017-01-23 06:00:00: -12.15°C\n" +
                        "2017-01-23 09:00:00: -11.34°C\n" +
                        "2017-01-23 12:00:00: -8.84°C\n" +
                        "2017-01-23 15:00:00: -7.94°C\n" +
                        "2017-01-23 18:00:00: -9.35°C\n" +
                        "2017-01-23 21:00:00: -10.29°C\n";

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText(threeHourForecastDataAsString)));

        ViewInteraction textView2 = onView(withId(R.id.textView6));
        textView2.check(matches(withText("Three Hour Forecast")));

    }

    @Test
    public void itShouldDisplayTemperatureFromApi() throws IOException
    {

        doReturn(
                Observable.just(jsonToPojo(TomorrowWeather.class, Responses.TOMORROW_WEATHER))
        ).when(weatherApi)
            .getTomorrowWeather(eq(FAKE_LONGITUDE), eq(FAKE_LATITUDE), eq("metric"), eq(1), eq("de"), anyString());

        doReturn(
                Observable.just(jsonToPojo(TodayWeather.class, Responses.TODAY_WEATHER))
        ).when(weatherApi)
            .getCurrentWeather(eq(FAKE_LONGITUDE), eq(FAKE_LATITUDE), eq("metric"), eq("de"), anyString());

        mActivity = rule.launchActivity(null);
        allowPermissionsIfNeeded();

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText("3.76")));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText("7.85")));
    }

    private static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

}
