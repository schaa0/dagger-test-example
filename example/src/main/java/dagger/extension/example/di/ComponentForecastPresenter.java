package dagger.extension.example.di;

import android.app.Application;

import dagger.Binds;

import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.extension.example.view.ForecastActivity;
import injector.Injector;

@ViewModelScope
@Subcomponent(modules = ComponentForecastPresenter.Bind.class)
public interface ComponentForecastPresenter
{

    void inject(ForecastActivity activity);

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance Builder String(String weatherForecast);
        ComponentForecastPresenter build();
    }

    @Module
    abstract class Bind {

    }

}
