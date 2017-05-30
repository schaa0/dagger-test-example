package dagger.extension.example.di;

import dagger.Subcomponent;
import dagger.di.FragmentComponent;
import dagger.di.FragmentComponentBuilder;
import dagger.extension.example.scope.FragmentScope;
import dagger.extension.example.view.weather.TomorrowWeatherFragment;

@FragmentScope
@Subcomponent
public interface ComponentTomorrowFragment extends FragmentComponent<TomorrowWeatherFragment> {

    @Subcomponent.Builder
    interface Builder extends FragmentComponentBuilder<ComponentTomorrowFragment> { }

}
