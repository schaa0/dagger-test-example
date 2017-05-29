package dagger.extension.example.di;

import java.util.Map;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.di.ComponentBuilder;
import dagger.di.FragmentComponentBuilder;
import dagger.extension.example.view.error.ErrorDialogFragment;
import dagger.extension.example.view.weather.TodayWeatherFragment;
import dagger.extension.example.view.weather.TomorrowWeatherFragment;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {ComponentErrorDialogFragment.class, ComponentTodayFragment.class, ComponentTomorrowFragment.class})
public abstract class FragmentBindingsModule {
    @Binds
    @IntoMap
    @ClassKey(ErrorDialogFragment.class)
    public abstract FragmentComponentBuilder errorDialog(ComponentErrorDialogFragment.Builder impl);

    @Binds
    @IntoMap
    @ClassKey(TodayWeatherFragment.class)
    public abstract FragmentComponentBuilder todayWeather(ComponentTodayFragment.Builder impl);

    @Binds
    @IntoMap
    @ClassKey(TomorrowWeatherFragment.class)
    public abstract FragmentComponentBuilder tomorrowWeather(ComponentTomorrowFragment.Builder impl);

    @Provides
    public static ComponentBuilder<FragmentComponentBuilder> componentBuilder(Map<Class<?>, Provider<FragmentComponentBuilder>> builderMap) {
        return new ComponentBuilder<>(builderMap);
    }
}
