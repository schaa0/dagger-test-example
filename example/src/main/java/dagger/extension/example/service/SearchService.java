package dagger.extension.example.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.extension.example.model.search.SearchModel;
import io.reactivex.Scheduler;
import io.reactivex.Single;

@Singleton
public class SearchService {

    private final WeatherApi api;
    private final Scheduler scheduler;
    private final String apiKey;

    private String lastCity = "";

    @Inject
    public SearchService(WeatherApi api, Scheduler scheduler, @Named("apiKey") String apiKey) {
        this.api = api;
        this.scheduler = scheduler;
        this.apiKey = apiKey;
    }

    public Single<SearchModel> searchByCity(String city) {
        lastCity = city;
        return api.searchWeather(city, "metric", "de", apiKey)
                .subscribeOn(this.scheduler);
    }

    public String getLastSearch() {
        return lastCity;
    }

}
