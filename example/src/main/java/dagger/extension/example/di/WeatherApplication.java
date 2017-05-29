package dagger.extension.example.di;

import javax.inject.Inject;

import dagger.di.ActivityComponentBuilder;
import dagger.di.ComponentBuilder;
import dagger.extension.example.view.forecast.ForecastActivity;
import dagger.extension.example.view.main.MainActivity;

public class WeatherApplication extends DaggerApplication {

    ComponentSingleton singletonComponent;

    @Inject ComponentBuilder<ActivityComponentBuilder> componentBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        singletonComponent = getInjector().componentSingleton(this);
        singletonComponent.inject(this);
    }

    public ComponentMainActivity inject(MainActivity activity) {
        return componentBuilder.getComponent(ComponentMainActivity.Builder.class, activity, builder -> {
            return getInjector().componentMainActivity(builder, activity);
        });
    }

    public ComponentForecastActivity inject(ForecastActivity activity, String forecastWeather) {
        return componentBuilder.getComponent(ComponentForecastActivity.Builder.class, activity, builder -> {
            return getInjector().componentForecastActivity(builder, activity, forecastWeather);
        });
    }
}
