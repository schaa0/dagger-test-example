package dagger.extension.example.view.forecast;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, application = TestApplicationForRobolectric.class, constants = BuildConfig.class)
public class TestForecastActivityWithRobolectricExample extends RobolectricTestCase {

    @Mock ForecastViewModel forecastViewModel;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        decorate().forecastActivitySubcomponent()
                .withForecastViewModel(() -> forecastViewModel);
    }

    @Test
    public void mockForecastViewModelGetsInjected() {
        Intent intent = new Intent(app(), ForecastActivity.class);
        intent.putExtra(ForecastActivity.INTENT_KEY_FORECAST, "some value");
        final ActivityController<ForecastActivity> controller =
                Robolectric.buildActivity(ForecastActivity.class);
        controller.create(null);
        final ForecastActivity activity = controller.get();
        assertEquals(activity.vm, forecastViewModel);
    }

}
