package dagger.extension.example.di;

import dagger.AllowStubGeneration;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.di.scope.FragmentScope;
import dagger.extension.example.view.error.ComponentErrorDialogFragment;
import dagger.extension.example.view.search.SearchAdapter;
import dagger.extension.example.view.search.SearchAdapterFactory;
import dagger.extension.example.view.search.SearchFragment;
import dagger.extension.example.view.weather.TodayWeatherFragment;
import dagger.extension.example.view.weather.TomorrowWeatherFragment;
import dagger.extension.example.view.weather.WeatherModule;
import io.reactivex.subjects.PublishSubject;

@Module(subcomponents = {ComponentErrorDialogFragment.class})
public abstract class FragmentBindingsModule {

    @Module abstract static class TodayWeatherModule extends WeatherModule<TodayWeatherFragment> { }

    @FragmentScope
    @ContributesAndroidInjector(modules = {TodayWeatherModule.class})
    public abstract TodayWeatherFragment todayWeatherFragment();

    @Module abstract static class TomorrowWeatherModule extends WeatherModule<TomorrowWeatherFragment> { }

    @FragmentScope
    @ContributesAndroidInjector(modules = {TomorrowWeatherModule.class})
    public abstract TomorrowWeatherFragment tomorrowWeatherFragment();

    @Module
    abstract static class SearchModule {
        @Provides
        public static SearchAdapterFactory searchAdapterFactory() {
            return SearchAdapter::new;
        }

        @Provides @RxObservable("adapter") @AllowStubGeneration
        public static PublishSubject<SearchAdapter> providesSearchAdapterSubject() {
            return PublishSubject.create();
        }
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = {SearchModule.class})
    public abstract SearchFragment searchFragment();

}
