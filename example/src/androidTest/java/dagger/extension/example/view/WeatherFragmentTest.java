package dagger.extension.example.view;

import android.app.Instrumentation;
import android.location.Location;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import dagger.android.testcase.Apply;
import dagger.android.testcase.DaggerActivityTestRule;
import dagger.android.testcase.Replace;
import dagger.annotation.InComponentSingleton;
import dagger.annotation.InTodayWeatherFragmentSubcomponent;
import dagger.annotation.InTomorrowWeatherFragmentSubcomponent;
import dagger.extension.example.R;
import dagger.extension.example.service.DateProvider;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.stubs.DateProviderStub;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.testcase.UiAutomatorEspressoTestCase;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.weather.TodayWeatherViewModel;
import dagger.extension.example.view.weather.TomorrowWeatherViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static dagger.extension.example.decoration.DefaultDecorations.FAKE_LATITUDE;
import static dagger.extension.example.decoration.DefaultDecorations.FAKE_LONGITUDE;
import static dagger.extension.example.decoration.DefaultDecorations.defaultWeatherApiAnswers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class WeatherFragmentTest extends UiAutomatorEspressoTestCase {

    @Mock @InComponentSingleton WeatherApi weatherApi;
    @Mock @InComponentSingleton LocationService locationService;
    @Mock @InTodayWeatherFragmentSubcomponent TodayWeatherViewModel todayVm;
    @Mock @InTomorrowWeatherFragmentSubcomponent TomorrowWeatherViewModel tomorrowVm;
    @Replace @InComponentSingleton
    DateProvider dateProviderStub = new DateProviderStub(2017, Calendar.JANUARY, 22, 23, 0, 0);
    PublishSubject<Location> locationSubject = PublishSubject.create();

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Apply.decorationsOf(this, app());
        defaultWeatherApiAnswers(weatherApi);
        Location fakeLocation = Fakes.location(FAKE_LONGITUDE, FAKE_LATITUDE);
        doReturn(true).when(locationService).isGpsProviderEnabled();
        doReturn(fakeLocation).when(locationService).lastLocation();
        doReturn(locationSubject).when(locationService).onNewLocation();
    }

    @Override
    public void tearDown() throws Exception {
        device().unfreezeRotation();
        super.tearDown();
    }

    @Test
    public void todayWeatherFragmentShouldActuallyCallItsViewModelOnFragmentStart() {
        rule.launchActivity(null);
        verify(todayVm).onViewAttached();
    }

    @Test
    public void tomorrowWeatherFragmentShouldActuallyCallItsViewModelOnFragmentStart() {
        rule.launchActivity(null);
        verify(tomorrowVm).onViewAttached();
    }

    @Test
    public void todayWeatherFragmentShouldNotifyViewModelAboutFragmentDestruction() throws Exception {
        rule.launchActivity(null);
        reset(todayVm);
        device().setOrientationLeft();
        SystemClock.sleep(300);
        verify(todayVm).onViewDetached();
    }

    @Test
    public void tomorrowWeatherFragmentShouldNotifyViewModelAboutFragmentDestruction() throws Exception {
        rule.launchActivity(null);
        reset(tomorrowVm);
        device().setOrientationLeft();
        SystemClock.sleep(400);
        verify(tomorrowVm).onViewDetached();
    }

    @Test
    public void searchFragmentShouldCollectDisposablesAndReleaseThemInOnDestroy() {
        CompositeDisposable composite = new CompositeDisposable();
        decorate().searchFragmentSubcomponent().withCompositeDisposable(() -> composite);
        final MainActivity activity = rule.launchActivity(null);
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() ->
        {
            ((ViewPager)activity.findViewById(R.id.container)).setCurrentItem(2);
            assertEquals(1, composite.size());
            activity.finish();
        });
        instrumentation.waitForIdleSync();
        SystemClock.sleep(500);
        assertTrue(composite.isDisposed());
    }
}
