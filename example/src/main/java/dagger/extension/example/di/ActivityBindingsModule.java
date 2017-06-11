package dagger.extension.example.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.extension.example.di.scope.ActivityScope;
import dagger.extension.example.view.forecast.ForecastActivity;
import dagger.extension.example.view.forecast.ForecastActivityModule;
import dagger.extension.example.view.main.MainActivity;
import dagger.extension.example.view.main.MainActivityModule;

@Module
public abstract class ActivityBindingsModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    public abstract MainActivity mainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = ForecastActivityModule.class)
    public abstract ForecastActivity forecastActivity();
}
