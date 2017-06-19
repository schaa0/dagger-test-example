package dagger.extension.example.view.main;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.AllowStubGeneration;
import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.di.scope.ActivityScope;
import dagger.extension.example.service.NavigationController;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@ActivityScope
public class MainViewModel extends NavigationViewModel {

    private final PublishSubject<String> publishSubject;
    private final PublishSubject<Integer> publishPageChange;

    @Inject @AllowStubGeneration
    public MainViewModel(NavigationController navigation,
                         @RxObservable("search") PublishSubject<String> publishSubject,
                         @RxObservable("page") PublishSubject<Integer> publishPageChange){
        super(navigation);
        this.publishSubject = publishSubject;
        this.publishPageChange = publishPageChange;
    }

    public void onSearchQuery(String query) {
        publishSubject.onNext(query);
    }

    public Observable<String> onNewQuery() {
        return publishSubject;
    }

    public Observable<Integer> onPageChanged() {
        return publishPageChange;
    }

    public void onPageChanged(int position) {
        publishPageChange.onNext(position);
    }
}
