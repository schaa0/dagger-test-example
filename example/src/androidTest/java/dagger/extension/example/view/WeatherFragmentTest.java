package dagger.extension.example.view;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.linkedin.android.testbutler.TestButler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;

import dagger.android.testcase.DaggerActivityTestRule;
import dagger.extension.example.decoration.DefaultDecorations;
import dagger.extension.example.di.GraphDecorator;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.stubs.DateProviderStub;
import dagger.extension.example.testcase.UiAutomatorEspressoTestCase;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.weather.TodayWeatherViewModel;
import dagger.extension.example.view.weather.TomorrowWeatherViewModel;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class WeatherFragmentTest extends UiAutomatorEspressoTestCase {

    @Mock WeatherApi weatherApi;
    @Mock LocationService locationService;
    @Mock TodayWeatherViewModel todayVm;
    @Mock TomorrowWeatherViewModel tomorrowVm;

    PublishSubject<Location> locationSubject = PublishSubject.create();

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        new DefaultDecorations(decorate()).apply(weatherApi, locationService, locationSubject,
                new DateProviderStub(2017, Calendar.JANUARY, 22, 23, 0, 0));
        decorate().todayWeatherFragmentSubcomponent().withTodayWeatherViewModel(() -> todayVm)
                  .and()
                  .tomorrowWeatherFragmentSubcomponent().withTomorrowWeatherViewModel(() -> tomorrowVm);
        if (isEmulator()) {
            TestButler.verifyAnimationsDisabled(InstrumentationRegistry.getTargetContext());
        }
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
        verify(todayVm).onViewDetached();
        verify(todayVm).onViewAttached();
    }

    @Test
    public void tomorrowWeatherFragmentShouldNotifyViewModelAboutFragmentDestruction() throws Exception {
        rule.launchActivity(null);
        reset(tomorrowVm);
        device().setOrientationLeft();
        verify(tomorrowVm).onViewDetached();
        verify(tomorrowVm).onViewAttached();
    }
}
