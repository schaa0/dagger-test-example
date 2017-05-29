package dagger.extension.example.di;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.di.ActivityComponent;
import dagger.di.ActivityComponentBuilder;
import dagger.extension.example.di.qualifier.ActivityScope;
import dagger.extension.example.view.forecast.ForecastActivity;

@ActivityScope
@Subcomponent(modules = {ComponentForecastActivity.ForecastActivityModule.class})
public interface ComponentForecastActivity extends ActivityComponent<ForecastActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ComponentForecastActivity> {
        @BindsInstance Builder activity(AppCompatActivity activity);
        @BindsInstance Builder weatherForecastString(@Named("forecastWeatherData")String weatherForecast);
    }

    @Module(includes = FragmentBindingsModule.class)
    abstract class ForecastActivityModule {
        @Provides
        public static FragmentManager fragmentManager(AppCompatActivity activity) {
            return activity.getSupportFragmentManager();
        }
    }
}
