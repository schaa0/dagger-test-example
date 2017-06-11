package dagger.extension.example.view.search;

import android.databinding.ObservableField;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.SearchService;
import dagger.extension.example.view.main.MainViewModel;
import dagger.extension.example.view.main.NavigationViewModel;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class SearchViewModel extends NavigationViewModel {

    public final ObservableField<String> city = new ObservableField<>("");

    private final SearchService searchService;
    private final SearchAdapterFactory searchAdapterFactory;

    private PublishSubject<SearchAdapter> publishSubject = PublishSubject.create();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SearchAdapter adapter;

    @AllowStubGeneration
    @Inject
    public SearchViewModel(NavigationController navigationController, MainViewModel mainViewModel,
                           SearchService searchService, SearchAdapterFactory searchAdapterFactory) {
        super(navigationController);
        this.searchService = searchService;
        this.searchAdapterFactory = searchAdapterFactory;
        compositeDisposable.add(mainViewModel.onNewQuery().subscribe(this::search));
    }

    public Observable<SearchAdapter> onNewAdapterAvailable() {
        return publishSubject;
    }

    public void onAttached() {
        String lastSearch = searchService.getLastSearch();
        if (!lastSearch.isEmpty())
        {
            search(lastSearch);
        }
    }

    public void search(String city) {
        compositeDisposable.add(searchService.searchByCity(city)
            .toObservable()
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
                    publishSubject.onNext(adapter);
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
