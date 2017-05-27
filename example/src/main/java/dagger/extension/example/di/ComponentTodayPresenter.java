package dagger.extension.example.di;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.extension.example.view.TodayWeatherFragment;
import dagger.extension.example.view.TomorrowWeatherFragment;
import injector.Injector;

@ViewModelScope
@Subcomponent(modules = ComponentTodayPresenter.Bind.class)
public interface ComponentTodayPresenter
{

    void inject(TodayWeatherFragment fragment);

    @Subcomponent.Builder
    interface Builder {
        ComponentTodayPresenter build();
    }

    @Module
    abstract class Bind {
        @Provides
        public static ComponentTodayPresenter componentTodayPresenter(Application app, ComponentTodayPresenter.Builder builder) {
            return Injector.get(app).componentTodayPresenter(builder);
        }
    }
}
