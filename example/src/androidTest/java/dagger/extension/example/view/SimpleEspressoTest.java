package dagger.extension.example.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.internal.statement.UiThreadStatement;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.linkedin.android.testbutler.TestButler;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import dagger.android.testcase.DaggerActivityTestRule;
import dagger.extension.example.R;
import dagger.extension.example.decoration.DefaultDecorations;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.stub.SearchViewModelStubDelegate;
import dagger.extension.example.stubs.DateProviderStub;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.stubs.PermissionServiceStub;
import dagger.extension.example.stubs.Responses;
import dagger.extension.example.testcase.UiAutomatorEspressoTestCase;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.search.SearchAdapter;
import io.reactivex.Observable;
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
import static dagger.extension.example.decoration.DefaultDecorations.FAKE_LATITUDE;
import static dagger.extension.example.decoration.DefaultDecorations.FAKE_LONGITUDE;
import static dagger.extension.example.decoration.DefaultDecorations.defaultWeatherApiAnswers;
import static dagger.extension.example.matcher.DrawableMatcher.hasNoDrawable;
import static dagger.extension.example.matcher.EmptyTextMatcher.emptyText;
import static dagger.extension.example.stubs.Fakes.fakeResponse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SimpleEspressoTest extends UiAutomatorEspressoTestCase
{

    @Mock WeatherApi weatherApi;
    @Mock LocationService locationService;

    PublishSubject<Location> locationSubject = PublishSubject.create();

    TestRule mockRule = new MockRule(this);
    DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);
    TestRule decoratorRule = (base, description) -> {
        new DefaultDecorations(decorate())
                .apply(
                        weatherApi, locationService,
                        locationSubject, new DateProviderStub(2017, Calendar.JANUARY, 22, 23, 0, 0)
                );
        return base;
    };

    @Rule
    public TestRule ruleChain = RuleChain.outerRule(rule).around(decoratorRule).around(mockRule);

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        if (isEmulator()) {
            TestButler.verifyAnimationsDisabled(InstrumentationRegistry.getTargetContext());
        }
    }

    @Test
    public void itShouldShowWeatherForNextDay() throws IOException
    {

        doReturn(Observable.empty()).when(weatherApi).getCurrentWeather(anyDouble(), anyDouble());
        doReturn(Observable.empty()).when(weatherApi).getTomorrowWeather(anyDouble(), anyDouble());

        when(weatherApi.getForecastWeather(eq(FAKE_LONGITUDE), eq(FAKE_LATITUDE))).thenReturn(
                Observable.just(fakeResponse(Responses.JSON.THREE_HOUR_FORECAST))
        );

        rule.launchActivity(null);
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
        defaultWeatherApiAnswers(weatherApi);
        locationSubject.onNext(Fakes.location(FAKE_LONGITUDE, FAKE_LATITUDE));
        verify(weatherApi).getCurrentWeather(FAKE_LONGITUDE, FAKE_LATITUDE);
        verify(weatherApi).getTomorrowWeather(FAKE_LONGITUDE, FAKE_LATITUDE);
    }

    @Test
    public void itShouldDisplayTemperatureFromApi() throws IOException
    {

        rule.launchActivity(null);
        allowPermissionsIfNeeded();

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText("Temperature: 3.76°C")));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText("Temperature: 7.85°C")));
    }

    @Test
    public void weatherDataIsRestoredOnOrientationChange() throws IOException, RemoteException {

        rule.launchActivity(null);
        allowPermissionsIfNeeded();

        String todayTemperature = "Temperature: 3.76°C";
        String tomorrowTemperature = "Temperature: 7.85°C";

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText(todayTemperature)));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText(tomorrowTemperature)));

        device().setOrientationLeft();
        SystemClock.sleep(800);

        onView(withIndex(withId(R.id.temperatureTextView), 0)).check(matches(withText(todayTemperature)));
        onView(withIndex(withId(R.id.temperatureTextView), 1)).check(matches(withText(tomorrowTemperature)));

        reset(weatherApi);
        defaultWeatherApiAnswers(weatherApi);

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

        rule.launchActivity(null);
        this.allowPermissionsIfNeeded();

        ViewInteraction viewPager = onView(allOf(withId(R.id.container), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction appCompatTextView = onView(allOf(withText("Search"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction actionMenuItemView = onView(allOf(withId(R.id.action_search), isDisplayed()));
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
        ViewInteraction viewPager = onView(allOf(withId(R.id.container), isDisplayed()));
        assertFalse(adapterObservable.hasObservers());
        viewPager.perform(swipeLeft());
        viewPager.perform(swipeLeft());
        assertTrue(adapterObservable.hasObservers());
    }

    @Test
    public void requestsLocationUpdatesOnStart() {
        rule.launchActivity(null);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        verify(locationService).requestLocationUpdates();
    }

    @Test
    public void removesLocationUpdatesOnStop() throws Throwable {
        final MainActivity activity = rule.launchActivity(null);
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            instrumentation.callActivityOnPause(activity);
            verify(locationService, times(0)).removeUpdates();
            instrumentation.callActivityOnStop(activity);
            verify(locationService).removeUpdates();
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
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
