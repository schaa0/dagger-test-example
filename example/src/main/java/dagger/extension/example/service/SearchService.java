package dagger.extension.example.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.extension.example.di.qualifier.RxScheduler;
import dagger.extension.example.model.search.SearchModel;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static dagger.extension.example.di.qualifier.RxScheduler.Type.NETWORK;

@Singleton
public class SearchService {

    private final WeatherApi api;
    private final Scheduler scheduler;

    private String lastCity = "";

    @Inject
    public SearchService(WeatherApi api, @RxScheduler(NETWORK) Scheduler scheduler) {
        this.api = api;
        this.scheduler = scheduler;
    }

    public Observable<SearchModel> searchByCity(String city) {
        lastCity = city;
        return api.searchWeather(city).subscribeOn(this.scheduler);
    }

    public String getLastSearch() {
        return lastCity;
    }

}
