package dagger.extension.example.view.main;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import dagger.extension.example.BuildConfig;
import dagger.extension.example.di.RobolectricTestCase;
import dagger.extension.example.di.TestApplicationForRobolectric;
import dagger.extension.example.service.LocationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, application = TestApplicationForRobolectric.class, constants = BuildConfig.class)
public class TestMainActivityWithRobolectricExample extends RobolectricTestCase {

    @Mock SectionsPagerAdapter pagerAdapter;
    @Mock LocationService locationService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        decorate().mainActivitySubcomponent().withSectionsPagerAdapter(() -> pagerAdapter);
    }

    @Test
    public void mockPagerAdapterGetsInjected() {
        final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        controller.create(null);
        final MainActivity activity = controller.get();
        assertEquals(activity.mSectionsPagerAdapter, pagerAdapter);
    }

    @Test
    public void requestsLocationUpdatesInOnStart() {
        decorate().componentSingleton().withLocationService(() -> locationService);
        app().onCreate();
        final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        controller.create(null);
        verify(locationService, times(0)).requestLocationUpdates();
        controller.start();
        verify(locationService).requestLocationUpdates();
        reset(locationService);
        controller.resume();
        controller.visible();
        verifyNoMoreInteractions(locationService);
    }

    @Test
    public void removesLocationUpdatesInOnStop() {
        decorate().componentSingleton().withLocationService(() -> locationService);
        app().onCreate();
        final ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        controller.setup();
        reset(locationService);
        controller.pause();
        verify(locationService, times(0)).removeUpdates();
        controller.stop();
        verify(locationService).removeUpdates();
        controller.destroy();
        verifyNoMoreInteractions(locationService);
    }

}
