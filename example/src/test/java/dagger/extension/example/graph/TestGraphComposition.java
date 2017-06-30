package dagger.extension.example.graph;

import android.content.Context;

import com.bumptech.glide.RequestManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import dagger.extension.example.di.TestDaggerComponentSingleton;
import dagger.extension.example.di.TestWeatherApplication;
import dagger.extension.example.scheduler.CurrentThreadExecutor;
import dagger.extension.example.service.DateProvider;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.weather.TodayWeatherFragment;
import dagger.extension.example.view.weather.TodayWeatherViewModel;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestGraphComposition {


    @Test
    public void testComponent() {

        final TestWeatherApplication app = new TestWeatherApplication();

        app.componentSingleton()
           .withRequestManager(() -> mock(RequestManager.class))
           .withWeatherApi(() -> mock(WeatherApi.class))
           .withRxSchedulerMain(() -> Schedulers.from(new CurrentThreadExecutor()));

        TestDaggerComponentSingleton singleton = (TestDaggerComponentSingleton)
                TestDaggerComponentSingleton.builder(app)
                                            .context(mock(Context.class))
                                            .seedInstance(app).build();
        final TodayWeatherViewModel vm = mock(TodayWeatherViewModel.class);

        app.todayWeatherFragmentSubcomponent().withTodayWeatherViewModel(() -> vm);
        final TestDaggerComponentSingleton.TestMainActivitySubcomponentImpl mainActivitySubcomponent =
                singleton.getMainActivitySubcomponent(mock(MainActivity.class));

        final TodayWeatherViewModel viewModel = mainActivitySubcomponent
                .getTodayWeatherFragmentSubcomponent(mock(TodayWeatherFragment.class))
                .getTodayWeatherViewModel().get();


        assertEquals(vm, viewModel);
    }
}
