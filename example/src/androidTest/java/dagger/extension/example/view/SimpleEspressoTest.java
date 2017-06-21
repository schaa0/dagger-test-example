package dagger.extension.example.view;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.internal.runner.TestRequestBuilder;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.linkedin.android.testbutler.TestButler;

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
import java.util.concurrent.CountDownLatch;

import dagger.android.testcase.DaggerActivityTestRule;
import dagger.extension.example.R;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.today.TodayWeather;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.SearchService;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.stub.SearchViewModelStubDelegate;
import dagger.extension.example.stubs.DateProviderStub;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.stubs.PermissionServiceStub;
import dagger.extension.example.stubs.Responses;
import dagger.extension.example.testcase.UiAutomatorEspressoTestCase;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.search.SearchAdapter;
import dagger.extension.example.view.search.SearchAdapterFactory;
import dagger.extension.example.view.search.SearchViewModel;
import delegates.SearchViewModelDelegate;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static dagger.extension.example.matcher.DrawableMatcher.hasNoDrawable;
import static dagger.extension.example.matcher.EmptyTextMatcher.emptyText;
import static dagger.extension.example.stubs.Fakes.fakeResponse;
import static dagger.extension.example.stubs.Fakes.location;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SimpleEspressoTest extends UiAutomatorEspressoTestCase
{

    public static final double FAKE_LONGITUDE = 1.0;
    public static final double FAKE_LATITUDE = 1.0;

    @Mock WeatherApi weatherApi;
    @Mock LocationService locationService;

    PublishSubject<Location> locationSubject = PublishSubject.create();

    private MainActivity mActivity;

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        TestButler.verifyAnimationsDisabled(InstrumentationRegistry.getTargetContext());
        MockitoAnnotations.initMocks(this);
        this.defaultWeatherApiAnswers();
        Location fakeLocation = Fakes.location(FAKE_LONGITUDE, FAKE_LATITUDE);
        doReturn(true).when(locationService).isGpsProviderEnabled();
        doReturn(fakeLocation).when(locationService).lastLocation();
        doReturn(locationSubject).when(locationService).onNewLocation();

        decorate().componentSingleton()
                  .withDateProvider(() -> new DateProviderStub(2017, Calendar.JANUARY, 22, 23, 0, 0))
                  .withWeatherApi(() -> weatherApi)
                  .withLocationService(() -> locationService);

    }

    @Test
    public void itShouldShowWeatherForNextDay() throws IOException
    {

        doReturn(Observable.empty()).when(weatherApi).getCurrentWeather(anyDouble(), anyDouble());
        doReturn(Observable.empty()).when(weatherApi).getTomorrowWeather(anyDouble(), anyDouble());

        when(weatherApi.getForecastWeather(eq(FAKE_LONGITUDE), eq(FAKE_LATITUDE))).thenReturn(
                Observable.just(fakeResponse(ThreeHoursForecastWeather.class, Responses.JSON.THREE_HOUR_FORECAST))
        );

        mActivity = rule.launchActivity(null);
        allowPermissionsIfNeeded();

        ViewInteraction appCompatTextView = onView(
                allOf(withText(R.string.tomorrow), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imageView), isDisplayed()));
        appCompatImageView.perform(click());

        final String threeHourForecastDataResult =
                "2017-01-23 00:00:00: -11.55°C\n" +
                        "2017-01-23 03:00:00: -12.0°C\n" +
                        "2017-01-23 06:00:00: -12.15°C\n" +
                        "2017-01-23 09:00:00: -11.34°C\n" +
                        "2017-01-23 12:00:00: -8.84°C\n" +
                        "2017-01-23 15:00:00: -7.94°C\n" +
                        "2017-01-23 18:00:00: -9.35°C\n" +
                        "2017-01-23 21:00:00: -10.29°C\n";

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText(threeHourForecastDataResult)));

        ViewInteraction textView2 = onView(withId(R.id.textView6));
        textView2.check(matches(withText("Three Hour Forecast")));

    }

    @Test
    public void itShouldRefreshTheViewWhenTheLocationChanged() throws IOException {
        rule.launchActivity(null);
        reset(weatherApi);
        this.defaultWeatherApiAnswers();
        locationSubject.onNext(Fakes.location(FAKE_LONGITUDE, FAKE_LATITUDE));
        verify(weatherApi).getCurrentWeather(FAKE_LONGITUDE, FAKE_LATITUDE);
        verify(weatherApi).getTomorrowWeather(FAKE_LONGITUDE, FAKE_LATITUDE);
    }

    @Test
    public void itShouldDisplayTemperatureFromApi() throws IOException
    {

        mActivity = rule.launchActivity(null);
        allowPermissionsIfNeeded();

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText("Temperature: 3.76°C")));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText("Temperature: 7.85°C")));
    }

    private void defaultWeatherApiAnswers() throws IOException {
        doReturn(Observable.just(fakeResponse(TomorrowWeather.class, Responses.JSON.TOMORROW_WEATHER)))
                .when(weatherApi).getTomorrowWeather(anyDouble(), anyDouble());

        doReturn(Observable.just(fakeResponse(TodayWeather.class, Responses.JSON.TODAY_WEATHER)))
                .when(weatherApi).getCurrentWeather(anyDouble(), anyDouble());
    }

    @Test
    public void weatherDataIsRestoredOnOrientationChange() throws IOException, RemoteException {

        mActivity = rule.launchActivity(null);
        allowPermissionsIfNeeded();

        String todayTemperature = "Temperature: 3.76°C";
        String tomorrowTemperature = "Temperature: 7.85°C";

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText(todayTemperature)));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText(tomorrowTemperature)));

        device().setOrientationLeft();
        SystemClock.sleep(500);

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText(todayTemperature)));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText(tomorrowTemperature)));

        reset(weatherApi);
        this.defaultWeatherApiAnswers();

        device().unfreezeRotation();

        SystemClock.sleep(500);

        verify(weatherApi, never()).getCurrentWeather(anyDouble(), anyDouble());
        verify(weatherApi, never()).getTomorrowWeather(anyDouble(), anyDouble());
        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText(todayTemperature)));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText(tomorrowTemperature)));

    }

    @Test
    public void errorDialogIsShownWhenRequestFails() {
        final String message = "some exception message";
        when(weatherApi.getCurrentWeather(FAKE_LONGITUDE, FAKE_LATITUDE)).thenReturn(
                Observable.error(new HttpException(
                        Response.error(500, ResponseBody.create(MediaType.parse("text/plain"), message))
                ))
        );
        when(weatherApi.getTomorrowWeather(FAKE_LONGITUDE, FAKE_LATITUDE)).thenReturn(
                Observable.empty()
        );
        rule.launchActivity(null);
        this.allowPermissionsIfNeeded();

        onView(withText(message)).check(matches(isDisplayed()));
    }

    @Test
    public void viewIsClearedWhenARequestFailed() {
        NavigationController controller = mock(NavigationController.class);
        decorate().mainActivitySubcomponent().withNavigationController(() -> controller);
        doNothing().when(controller).showErrorIfNotAlreadyShowing(anyString(), anyString());
        final String message = "some exception message";
        when(weatherApi.getCurrentWeather(FAKE_LONGITUDE, FAKE_LATITUDE)).thenReturn(
                Observable.error(new HttpException(
                        Response.error(500, ResponseBody.create(MediaType.parse("text/plain"), message))
                ))
        );
        when(weatherApi.getTomorrowWeather(FAKE_LONGITUDE, FAKE_LATITUDE)).thenReturn(
                Observable.empty()
        );

        rule.launchActivity(null);
        this.allowPermissionsIfNeeded();

        onView(withIndex(withId(R.id.imageView), 0)).check(matches(hasNoDrawable()));
        final int ids[] = new int[]{R.id.cityTextView, R.id.temperatureTextView,
                R.id.humidityTextView, R.id.descriptionTextView};
        for (int id : ids) {
            onView(withIndex(withId(id), 0)).check(matches(emptyText()));
        }
    }

    @Test
    public void testSearchIsInvokedWhenSubmittingInSearchView() {
        CountDownLatch latch = new CountDownLatch(1);
        decorate().mainActivitySubcomponent()
                    .withPermissionService(activity -> new PermissionServiceStub(activity, false))
                  .and().searchFragmentSubcomponent()
                    .withSearchViewModel(new SearchViewModelStubDelegate(latch));

        mActivity = rule.launchActivity(null);
        this.allowPermissionsIfNeeded();

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Search"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_search), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("AAA"));

        SystemClock.sleep(1500);

        assertEquals(0, latch.getCount());
    }

    @Test
    public void searchFragmentSubscribesToAdapterObservable() throws IOException {
        final PublishSubject<SearchAdapter> adapterObservable = PublishSubject.create();
        decorate().searchFragmentSubcomponent().withRxObservableAdapter(() -> adapterObservable);
        rule.launchActivity(null);
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());
        viewPager.perform(swipeLeft());
        assertTrue(adapterObservable.hasObservers());
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
