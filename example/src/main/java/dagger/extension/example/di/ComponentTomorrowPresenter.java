package dagger.extension.example.di;

import android.app.Application;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.extension.example.view.TomorrowWeatherFragment;
import injector.Injector;

@ViewModelScope
@Subcomponent(modules = ComponentTomorrowPresenter.Bind.class)
public interface ComponentTomorrowPresenter
{
    void inject(TomorrowWeatherFragment fragment);

    @Subcomponent.Builder
    interface Builder {
        ComponentTomorrowPresenter build();
    }

    @Module
    abstract class Bind {

    }
}
