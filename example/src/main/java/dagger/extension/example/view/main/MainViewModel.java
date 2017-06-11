package dagger.extension.example.view.main;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.extension.example.di.scope.ActivityScope;
import dagger.extension.example.service.NavigationController;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@ActivityScope
public class MainViewModel extends NavigationViewModel {

    private PublishSubject<String> publishSubject = PublishSubject.create();

    @AllowStubGeneration
    @Inject
    public MainViewModel(NavigationController navigation){
        super(navigation);
    }

    public void onSearchQuery(String query) {
        publishSubject.onNext(query);
    }

    public Observable<String> onNewQuery() {
        return publishSubject;
    }
}
