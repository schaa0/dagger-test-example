package dagger.extension.example.view.search;

import android.databinding.ObservableField;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.AllowStubGeneration;
import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.SearchService;
import dagger.extension.example.view.main.NavigationViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class SearchViewModel extends NavigationViewModel {

    public final ObservableField<String> city = new ObservableField<>("");

    private final PublishSubject<SearchAdapter> searchAdapterSubject;
    private final SearchService searchService;
    private final SearchAdapterFactory searchAdapterFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SearchAdapter adapter;

    @Inject @AllowStubGeneration
    public SearchViewModel(NavigationController navigationController,
                           @RxObservable("search") Observable<String> searchObservable,
                           @RxObservable("adapter") PublishSubject<SearchAdapter> searchAdapterSubject,
                           SearchService searchService, SearchAdapterFactory searchAdapterFactory) {
        super(navigationController);
        this.searchAdapterSubject = searchAdapterSubject;
        this.searchService = searchService;
        this.searchAdapterFactory = searchAdapterFactory;
        compositeDisposable.add(searchObservable.subscribe(this::search));
    }

    public Observable<SearchAdapter> onNewAdapterAvailable() {
        return searchAdapterSubject;
    }

    public void onAttach() {
        String lastSearch = searchService.getLastSearch();
        if (!lastSearch.isEmpty())
        {
            search(lastSearch);
        }
    }

    public void search(String city) {
        compositeDisposable.add(searchService.searchByCity(city)
            .take(1)
            .doOnNext(searchModel -> SearchViewModel.this.city.set(searchModel.getCity().getName()))
            .map(ThreeHoursForecastWeather::getWeatherInfo)
            .flatMap(Observable::fromIterable)
            .map(weatherInfo -> new SearchItemViewModel(weatherInfo.getDtTxt(), weatherInfo.temperature(), weatherInfo.humidity()))
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((searchItemViewModels) -> {
                if (adapter != null) {
                    adapter.updateWeatherInfos(searchItemViewModels);
                }else
                {
                    adapter = searchAdapterFactory.create(searchItemViewModels);
                    searchAdapterSubject.onNext(adapter);
                }
            }, this::onError));
    }

    private void onError(Throwable throwable) {
        this.showError(throwable.getMessage(), throwable.toString());
    }

    public void onDetach() {
        compositeDisposable.clear();
    }

}
