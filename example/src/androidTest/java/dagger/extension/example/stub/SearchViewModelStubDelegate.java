package dagger.extension.example.stub;

import java.util.concurrent.CountDownLatch;

import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.SearchService;
import dagger.extension.example.view.search.SearchAdapter;
import dagger.extension.example.view.search.SearchAdapterFactory;
import dagger.extension.example.view.search.SearchViewModel;
import delegates.SearchViewModelDelegate;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;

public class SearchViewModelStubDelegate implements SearchViewModelDelegate {

    private final CountDownLatch latch;

    public SearchViewModelStubDelegate(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public SearchViewModel get(NavigationController navigationController,
                               Observable<String> searchObservable,
                               PublishSubject<SearchAdapter> searchAdapterSubject,
                               SearchService searchService,
                               SearchAdapterFactory searchAdapterFactory,
                               Scheduler androidScheduler) {
        return new SearchViewModel(navigationController, searchObservable, searchAdapterSubject,
                        searchService, searchAdapterFactory, androidScheduler) {
            @Override
            public void search(String city) {
                latch.countDown();
            }
        };
    }
}
