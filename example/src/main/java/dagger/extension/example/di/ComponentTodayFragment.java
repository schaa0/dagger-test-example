package dagger.extension.example.di;

import dagger.Subcomponent;
import dagger.di.FragmentComponent;
import dagger.di.FragmentComponentBuilder;
import dagger.extension.example.di.qualifier.FragmentScope;
import dagger.extension.example.view.weather.TodayWeatherFragment;

@FragmentScope
@Subcomponent
public interface ComponentTodayFragment extends FragmentComponent<TodayWeatherFragment> {

    @Subcomponent.Builder
    interface Builder extends FragmentComponentBuilder<ComponentTodayFragment> { }
}
