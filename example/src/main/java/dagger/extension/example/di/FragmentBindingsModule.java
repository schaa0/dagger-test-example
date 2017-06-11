package dagger.extension.example.di;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.extension.example.di.scope.FragmentScope;
import dagger.extension.example.view.error.ComponentErrorDialogFragment;
import dagger.extension.example.view.error.ErrorDialogFragment;
import dagger.extension.example.view.search.SearchAdapter;
import dagger.extension.example.view.search.SearchAdapterFactory;
import dagger.extension.example.view.search.SearchFragment;
import dagger.extension.example.view.weather.TodayWeatherFragment;
import dagger.extension.example.view.weather.TomorrowWeatherFragment;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {ComponentErrorDialogFragment.class})
public abstract class FragmentBindingsModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TodayWeatherFragment todayWeatherFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TomorrowWeatherFragment tomorrowWeatherFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = {SearchModule.class})
    public abstract SearchFragment searchFragment();

    @Module
    abstract static class SearchModule {
        @Provides
        public static SearchAdapterFactory searchAdapterFactory() {
            return SearchAdapter::new;
        }
    }

}
