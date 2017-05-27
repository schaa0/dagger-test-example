package dagger.extension.example.di;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;

import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.extension.example.service.ViewPagerFragmentFactory;
import dagger.extension.example.view.MainActivity;
import dagger.extension.example.view.TodayWeatherFragment;
import dagger.extension.example.view.TomorrowWeatherFragment;
import injector.Injector;

@ActivityScope
@Subcomponent(modules = { ComponentActivity.ModuleActivity.class })
public interface ComponentActivity {

    void inject(MainActivity mainActivity);

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance Builder activity(AppCompatActivity activity);
        ComponentActivity build();
    }

    @Module(subcomponents = {ComponentTodayPresenter.class, ComponentTomorrowPresenter.class})
    abstract class ModuleActivity {

        @Provides
        @ActivityScope
        public static ViewPagerFragmentFactory fragmentFactory(Context context) {
            return new ViewPagerFragmentFactory(context);
        }

        @Provides
        public static FragmentManager fragmentManager(AppCompatActivity activity)
        {
            return activity.getSupportFragmentManager();
        }

    }


}
